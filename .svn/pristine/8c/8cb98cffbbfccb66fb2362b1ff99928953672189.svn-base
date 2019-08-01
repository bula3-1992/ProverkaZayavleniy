/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifier.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author timofeevan
 */
public class ExecUtils {

    private static final String TEMP_DIR = "C:\temp";
    private static final String CHARSET = "CP866";

    //Потоки для чтения выводящихся сообщений/ошибок из консоли
    private static class StreamGobbler extends Thread {

        List<String> lst = new ArrayList<>();
        InputStream is;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lst.add(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(ExecUtils.class.getName()).log(Level.ERROR, null, ex);
            }
        }
    }

    private static String getTempDirectory() {
        String temp = System.getProperty("java.io.tmpdir");
        if (temp == null || temp.isEmpty()) {
            temp = TEMP_DIR;
        }
        if (temp != null) {
            if (temp.charAt(temp.length() - 1) != '\\') {
                temp += "\\";
            }
        }
        return temp;
    }

    private static String getRandomName(String prefix) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return prefix + sb.toString();
    }

    public static Path createTempDirectory(String prefix) throws IOException {
        Path dir = FileSystems.getDefault().getPath(getTempDirectory() + getRandomName(prefix));
        return Files.createDirectory(dir);
    }

    public static void copy(String sourceFileName, String destinationFileName) throws IOException {
        Path pathFile = Paths.get(sourceFileName);
        Path destFile = Paths.get(destinationFileName);
        Files.copy(pathFile, destFile, StandardCopyOption.REPLACE_EXISTING);
    }

    //Не трогать - тут все заебись
    public static ExecResult exec(Path filePath, String fileExe, String... arguments) throws IOException {
        ExecResult result = new ExecResult();
        List<String> called = new ArrayList<>();
        called.add(filePath + "\\" + fileExe);
        called.addAll(Arrays.asList(arguments));
        ProcessBuilder builder = new ProcessBuilder(called);
        builder.directory(filePath.toFile());
        Process p = builder.start();
        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream());
        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream());
        errorGobbler.start();
        outputGobbler.start();
        try {
            result.setReturnValue(p.waitFor());
        } catch (InterruptedException ex) {
            result.setReturnValue(1);//Код - ошибка
            Logger.getLogger(ExecUtils.class.getName()).log(Level.ERROR, null, ex);
            p.destroy();
        }
        for (String out : outputGobbler.lst) {
            result.addOut(out);
        }
        for (String err : errorGobbler.lst) {
            result.addOut(err);
        }
        return result;
    }
}
