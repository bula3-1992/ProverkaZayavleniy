
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


public class VerifyCertificate {

    static {
        try {
            Path p = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir") + "/PZ_DLL");
            try {
                Files.createDirectory(p);
            } catch (IOException ex) {
            }
            File fileOut = new File(p + "/verifier_VerifyCertificate.dll");
            try (InputStream is = VerifyCertificate.class.getResourceAsStream("/resources/verifier_VerifyCertificate.dll");
                    OutputStream os = new FileOutputStream(fileOut)) {
                byte[] b = new byte[1024];
                int count;
                while ((count = is.read(b)) != -1) {
                    os.write(b, 0, count);
                }
            } catch (IOException ex) {
                Logger.getLogger(VerifyCertificate.class.getName()).log(Level.ERROR, null, ex);
            }
            System.load(fileOut.toString());
        } catch (UnsatisfiedLinkError ex) {
            Logger.getLogger(VerifyCertificate.class.getName()).log(Level.ERROR, "библиотека не найдена", ex);
        }
    }

    native public String verify(byte[] cert, long signingTimeFormatted);
    
    public static String translateErrorMessage(String result) {
        switch (result) {
            case "No error found for this certificate or chain.":
                return "Успешно";
            case "The certificate chain is not complete and One of the certificates in the chain was issued by a certification authority that the original certificate had certified.":
                return "Не удалось построить цепочку до корневого сертификата";
            case "This certificate or one of the certificates in the certificate chain is not time-valid.":
                return "Срок действия одного или нескольких сертификатов цепочки истек или еще не наступил, при проверке по системным часам или по отметке времени в подписанном файле.";
            case "Trust for this certificate or one of the certificates in the certificate chain has been revoked.":
                return "Один или несколько сертификатов цепочки были отованы";
            case "The revocation status of the certificate or one of the certificates in the certificate chain is either offline or stale and The revocation status of the certificate or one of the certificates in the certificate chain is unknown.":
                return "Не удалось проверить на отзыв один или несколько сертификатов цепочки";
            default:
                return "Не удалоcь построить цепочку";
        }
    }
}
