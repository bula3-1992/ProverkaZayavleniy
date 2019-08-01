package verifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import sun.security.pkcs.PKCS7;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 003-0823
 */
public class MyVerify {

    static String filename = "PFR-700-Y-2016-ORG-076-030-032748-DCK-00001-DPT-004028-DCK-16258";//данный конкретный файл показывает что при использовании
    //старой версии JRE до 1.7.0_72 возникает ошибка при построении P7S файла

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //System.out.println(System.getProperty("java.library.path"));
        RandomAccessFile f1 = new RandomAccessFile(filename + ".XML", "r");
        byte[] xml = new byte[(int) f1.length()];
        f1.read(xml);
        RandomAccessFile f2 = new RandomAccessFile(filename + ".p7s", "r");
        byte[] p7s = new byte[(int) f2.length()];
        f2.read(p7s);
        VerifyApp va = new VerifyApp("c:\\PriemZayavleniy\\CheckXML\\", "R0030000", "PR030015", "WDJSPT9K");
        PrimarilyVerificationResult primarilyVerificationResult = va.primarilyVerify(xml, p7s);
        System.out.println(primarilyVerificationResult.getVerifyCertificate());
        System.out.println(primarilyVerificationResult.getVerifySignature());
        System.out.println(primarilyVerificationResult.getVerifyFIO());
        va.secondarilyVerify(1, xml, filename + ".XML");
        List<SecondarilyVerificationResult> secondarilyVerificationResults = va.getSecondarilyVerificationResults();
        SecondarilyVerificationResult secondarilyVerificationResult = secondarilyVerificationResults.get(0);
        System.out.println(secondarilyVerificationResult.getConasResult());
        System.out.println(secondarilyVerificationResult.getCheckXMLResult());
        if (primarilyVerificationResult.getVerifyFIO().equals("Успешно")
                && primarilyVerificationResult.getVerifySignature().equals("Успешно")
                && primarilyVerificationResult.getVerifyCertificate().equals("Успешно")
                && secondarilyVerificationResult.getCheckXMLResult().equals("Успешно")
                && secondarilyVerificationResult.getConasResult().equals("Успешно")) {
            System.out.println("Общий результат проверки: Успешно");
        }  
        else{
            System.out.println("Общий результат проверки: Ошибка");            
        }
    }
}
