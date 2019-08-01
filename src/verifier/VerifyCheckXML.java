/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import verifier.utils.ExecResult;
import verifier.utils.ExecUtils;

/**
 *
 * @author 003-0823
 */
public class VerifyCheckXML {

    private String CHECKXML_DIR = null;
    private final static long TIMEOUT = 60L;//Таймаут (60 sec) ожидания проверки одного сообщения CheckXML-ем

    private final static int POOL_SIZE = 10;//!importamnt = 10
    private final static ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);//Пул потоков
    private final List<Future> futures;

    public VerifyCheckXML(String checkxml_dir) {
        CHECKXML_DIR = checkxml_dir;
        futures = new ArrayList<>();
    }

    //Специально созданный класс-поток для запуска проверки CheckXML
    private class VerifyRun implements Runnable {

        private final CheckXML checkXML;

        public VerifyRun(CheckXML checkXML) {
            this.checkXML = checkXML;
        }

        @Override
        public void run() {
            Long threadId = Thread.currentThread().getId() % POOL_SIZE + 1;
            try {
                Path p = ExecUtils.createTempDirectory("PZ_CX_");
                File exe = File.createTempFile("checkxml_cmd", ".exe", p.toFile());
                try (InputStream is = this.getClass().getResourceAsStream("/resources/checkxml_cmd.exe");
                        OutputStream os = new FileOutputStream(exe)) {
                    byte[] b = new byte[1024];
                    int count;
                    while ((count = is.read(b)) != -1) {
                        os.write(b, 0, count);
                    }
                }
                File xml = new File(p + "\\" + checkXML.getFilename());
                try (FileOutputStream fos = new FileOutputStream(xml)) {
                    fos.write(checkXML.getXml());
                }
                ExecResult execResult = ExecUtils.exec(p, exe.getName(), CHECKXML_DIR + "\\" +threadId, CHECKXML_DIR + "\\kladr", checkXML.getFilename());
                String result;
                if (execResult.getReturnValue() == 0) {
                    result = "Успешно";
                } else {
                    if (execResult.getReturnValue() > 0) {
                        result = "Отказ проверки CheckXML";
                    } else {
                        result = "Ошибка";
                    }
                }
                checkXML.setVerifyСheckXML(result);
                sleep(500);
                //Подчищаемся
                exe.delete();
                xml.delete();
                Files.delete(p);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(VerifyCheckXML.class.getName()).log(Level.ERROR, null, ex);
            }
        }
    }

    //Пуск провеки файла из структуры CheckXML
    public void verify(CheckXML checkXML) {
        VerifyRun vr = new VerifyRun(checkXML);
        futures.add(executor.submit(vr));

    }

    //Ожидание завершения всех потоков из пула потоков, инициализированных в текущем экземпляре класса
    public void waitFor() {
        for (Future f : this.futures) {
            try {
                f.get(TIMEOUT, TimeUnit.SECONDS);
            } catch (TimeoutException ex) {
                Logger.getLogger(VerifyCheckXML.class.getName()).log(Level.ERROR, null, ex);
                f.cancel(true);
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(VerifyCheckXML.class.getName()).log(Level.ERROR, null, ex);
            } 
        }
        futures.clear();
    }
}
