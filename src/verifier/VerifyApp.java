/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifier;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import sun.security.pkcs.PKCS7;
import verifier.utils.XMLUtils;

/**
 *
 * @author 003-0823
 */
public class VerifyApp {

    //буфер проверочных данных для запуска отложенной проверки
    private List<Conas> conasList = null;
    private Map<Integer, CheckXML> checkXMLMap = null;

    private final VerifyCheckXML vcXML;
    private final VerifyConAS vcAS;

    public VerifyApp(String checkxml_dir, String sysnameAS, String userAS, String passwordAS) {
        conasList = new ArrayList<>();
        checkXMLMap = new HashMap<>();
        vcXML = new VerifyCheckXML(checkxml_dir);
        vcAS = new VerifyConAS(sysnameAS, userAS, passwordAS);
    }

    /**
     * Первичная проверка xml-файла и подписи, производятся только те проверки,
     * которые выполняются моментально, т.е. ФИО, ЭЦП, СКПЭП
     *
     * @param xmlFile проверяемые данные
     * @param p7sFile цифровая подпись p7s на проверку
     * @return PrimarilyVerificationResult - класс содержащий результаты текущих проверок
     */
    public PrimarilyVerificationResult primarilyVerify(byte[] xmlFile, byte[] p7sFile) {
        PrimarilyVerificationResult primarilyVerificationResult = new PrimarilyVerificationResult();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(p7sFile);
            PKCS7 p7 = new PKCS7(bais);            
            primarilyVerificationResult.setVerifyCertCount(p7.getCertificates().length);//Проверка количества сертификатов
            X509Certificate cert = p7.getCertificates()[0];            
            primarilyVerificationResult.setVerifyFIO(fioVerify(cert, xmlFile));//Проверка ФИО в сертификате и в файле            
            primarilyVerificationResult.setVerifySignature(signatureVerify(xmlFile, p7sFile));//Проверка соответствия файла и его подписи  
            Date currentTime = Calendar.getInstance().getTime();
            primarilyVerificationResult.setVerifyCertificate(certificateVerify(cert.getEncoded(), currentTime));//Проверка валидности сертификата подписанта
        } catch (Exception ex) {
            primarilyVerificationResult.setVerifyException(ex.getMessage());
        }
        return primarilyVerificationResult;
    }

    /**
     * Проверка подписи на открытом ключе через MS CryptoAPI, Криптопровайдер -
     * Vipnet CSP
     *
     * @param xml проверяемые данные
     * @param sign цифровая подпись p7s на проверку
     * @return "true" - верна, иначе иначе сообщение об ошибке
     */
    private String signatureVerify(byte[] xml, byte[] sign) {
        try {
            VerifySignature vs = new VerifySignature();
            return vs.verify(xml, sign);
        } catch (Exception ex) {
            Logger.getLogger(VerifyApp.class.getName()).log(Level.ERROR, "отказ проверки VerifySignature.verify()", ex);
        }
        return "Не удалось проверить подпись файла";
    }

    /**
     * Проверка сертификата в хранилище сертификатов Windows через MS CryptoAPI,
     * Криптопровайдер - Vipnet CSP
     *
     * @param cert сертификат подписанта
     * @return "No error found for this certificate or chain." - верна, иначе
     * сообщение об ошибке на английском
     */
    private String certificateVerify(byte[] cert, Date verificationTime) {
        try {
            VerifyCertificate vc = new VerifyCertificate();
            long signingTimeFormatted = (verificationTime.getTime() + 11644473600000L) * 10000L;
            return vc.verify(cert, signingTimeFormatted);
        } catch (Exception ex) {
            Logger.getLogger(VerifyApp.class.getName()).log(Level.ERROR, "отказ проверки VerifyCertificate.verify()", ex);
        }
        return "Не удалось проверить сертификат";
    }

    /**
     * Проверка соответствия ФИО подписи и сертификата
     *
     * @param cert сертификат подписанта
     * @param xml проверяемые данные
     * @return "Успешно" - верна, "Ошибка" - не верна
     */
    private boolean fioVerify(X509Certificate cert, byte[] xml) {
        X500Name x500name = new X500Name(cert.getSubjectX500Principal().getName(X500Principal.RFC1779));
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];
        String certFIO = IETFUtils.valueToString(cn.getFirst().getValue()).toUpperCase().replace(" ", "");
        String xmlFIO = XMLUtils.getFio(xml).toUpperCase().replace(" ", "");
        return certFIO.equals(xmlFIO);
    }

    /**
     * Вторичная проверка xml-файла, производятся только CheckXML и ConAS400,
     * Проверка нуждается в ID проверяемых файлов
     *
     * @param id идентификатор записи в Filestore
     * @param xml проверяемые данные
     * @param filename имя файла
     */
    public void secondarilyVerify(int id, byte[] xml, String filename) {
        CheckXML checkXML = new CheckXML(xml, filename);
        checkXMLMap.put(id, checkXML);
        vcXML.verify(checkXML);
        //Проверка CheckXML
        Conas conas = XMLUtils.getConas(id, xml);
        if (conas != null) {
            conasList.add(conas);//Проверка ConAS БПИ        
        }
    }

    /**
     * Выполнение отложенной проверки, возвращает результат всех проверок
     * запущенных в secondarilyVerify(), буфер очищается
     *
     * @return массив результатов проверок, отдельный класс
     */
    public List<SecondarilyVerificationResult> getSecondarilyVerificationResults() {
        vcXML.waitFor();
        List<SecondarilyVerificationResult> secondarilyVerificationResults = vcAS.verify(conasList);
        conasList.clear();
        for (SecondarilyVerificationResult svResult : secondarilyVerificationResults) {
            svResult.setVerifyCheckXML(checkXMLMap.get(svResult.getId()).getVerifyСheckXML());
        }
        checkXMLMap.clear();
        return secondarilyVerificationResults;
    }
}
