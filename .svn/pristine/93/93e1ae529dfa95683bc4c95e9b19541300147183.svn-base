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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Булат
 */
public class VerifySignature {

    static {
        try {
            Path p = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir") + "/PZ_DLL");
            try {
                Files.createDirectory(p);
            } catch (IOException ex) {
            }
            File fileOut = new File(p + "/verifier_VerifySignature.dll");
            try (InputStream is = VerifySignature.class.getResourceAsStream("/resources/verifier_VerifySignature.dll");
                    OutputStream os = new FileOutputStream(fileOut)) {
                byte[] b = new byte[1024];
                int count;
                while ((count = is.read(b)) != -1) {
                    os.write(b, 0, count);
                }
            } catch (IOException ex) {
                Logger.getLogger(VerifySignature.class.getName()).log(Level.ERROR, null, ex);
            }
            System.load(fileOut.toString());
        } catch (UnsatisfiedLinkError ex) {
            Logger.getLogger(VerifySignature.class.getName()).log(Level.ERROR, "библиотека не найдена", ex);
        }
    }

    native public String verify(byte[] data, byte[] sign);
}
