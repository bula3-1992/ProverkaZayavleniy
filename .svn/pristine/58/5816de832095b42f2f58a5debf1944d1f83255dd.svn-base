/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import verifier.utils.ExecUtils;

/**
 *
 * @author 003-0823
 */
public class VerifyConAS {

    private final static String EXE_FILE = "Conas400Bpi.exe";
    private final static String DLL_FILE_1 = "cwbco.dll";
    private final static String DLL_FILE_2 = "prchdbl.dll";
    private final static String INI_FILE = "Conas400Bpi.ini";
    private final static long TIMEOUT = 30000L;//Таймаут (30 sec) ожидания проверки одного запроса из 100МАКС ConAS-ом

    private final String SYSNAME;
    private final String USER;
    private final String PASSWORD;
    private final static int MAX_PORTION = 100;

    public VerifyConAS(String sysnameAS, String userAS, String passwordAS) {
        SYSNAME = sysnameAS;
        USER = userAS;
        PASSWORD = passwordAS;
    }

    //Специально созданный класс-поток для запуска проверки ConAS
    private class VerifyRun implements Runnable {

        private final List<Conas> buffer;
        public List<SecondarilyVerificationResult> secondarilyVerificationResults;

        public VerifyRun(List<Conas> buffer) {
            this.buffer = buffer;
            secondarilyVerificationResults = new ArrayList<>();
        }

        @Override
        public void run() {
            try {
                Path p = ExecUtils.createTempDirectory("PZ_CC_");
                makeFile(p, EXE_FILE);
                makeFile(p, DLL_FILE_1);
                makeFile(p, DLL_FILE_2);
                File request = new File(p + "\\request.xml");
                byte[] xml = conasToXML(buffer);
                try (FileOutputStream fos = new FileOutputStream(request)) {
                    fos.write(xml);
                }
                try (FileWriter fw = new FileWriter(new File(p + "\\" + INI_FILE))) {
                    fw.write("[credentials]\n");
                    fw.write("sysname=" + SYSNAME + "\n");
                    fw.write("user=" + USER + "\n");
                    fw.write("password=" + PASSWORD + "\n");
                    fw.write("[files]\n"
                            + "inputfile=request.xml\n"
                            + "outputfile=response.xml\n"
                            + "[error_messages]\n"
                            + "right_val_desc=Правильное значение:\n"
                            + "msg_fio_all=Ошибка в проверке ФИО-СНИЛС.\n"
                            + "msg_fio_snils=Не соответствие ФИО.\n"
                            + "msg_fam=Не соответствие фамилии.\n"
                            + "msg_nam=Не соответствие имени.\n"
                            + "msg_famnam=Не соответствие имени и фамилии.\n"
                            + "msg_namptr=Не соответствие имени и отчества.\n"
                            + "msg_ot=Не соответствие отчества.\n"
                            + "msg_pol=Не соответствие Пола.\n"
                            + "msg_dob=Не соответствие даты рождения.");
                }
                ExecUtils.exec(p, EXE_FILE);
                DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
                f.setValidating(false);
                DocumentBuilder builder;
                builder = f.newDocumentBuilder();
                try (FileInputStream fis = new FileInputStream(new File(p + "\\response.xml"))) {
                    Document doc = builder.parse(new InputSource(new InputStreamReader(fis, "UTF-8")));
                    StringWriter sw = new StringWriter();
                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer transformer = tf.newTransformer();
                    transformer.transform(new DOMSource(doc), new StreamResult(sw));
                    XPathFactory xPathfactory = XPathFactory.newInstance();
                    XPath xpath = xPathfactory.newXPath();
                    XPathExpression expr = xpath.compile("/ConasOutput/Responses/ConasResponse");
                    NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                    for (int i = 0; i < nodes.getLength(); i++) {
                        SecondarilyVerificationResult secondarilyVerificationResult;
                        Node node = nodes.item(i);
                        String id = node.getAttributes().getNamedItem("Id").getTextContent();
                        int intid = Integer.parseInt(id);
                        String result = node.getAttributes().getNamedItem("Result").getTextContent();
                        int iiresult = Integer.parseInt(result);
                        secondarilyVerificationResult = new SecondarilyVerificationResult(intid);
                        if (iiresult == 0) {
                            secondarilyVerificationResult.setVerifyConas("Успешно");
                        } else {
                            String error = node.getFirstChild().getTextContent();
                            secondarilyVerificationResult.setVerifyConas(error);
                        }
                        secondarilyVerificationResults.add(secondarilyVerificationResult);
                    }
                }
                sleep(500);
                //Подчищаемся
                File[] listOfFile = p.toFile().listFiles();
                for (File tempFile : listOfFile) {
                    Files.delete(tempFile.toPath());
                }
                Files.delete(p);
            } catch (Exception ex) {
                Logger.getLogger(VerifyCheckXML.class.getName()).log(Level.ERROR, null, ex);
            }
        }
    }

    //Провека
    public List<SecondarilyVerificationResult> verify(List<Conas> conasList) {
        List<SecondarilyVerificationResult> result = new ArrayList<>();
        List<Conas> buffer = new ArrayList<>();
        for (int i = 0; i < conasList.size(); i++) {
            if (i % MAX_PORTION == 0 && i != 0) {
                List<SecondarilyVerificationResult> temp = verifyBuffer(buffer);
                result.addAll(temp);
                buffer.clear();
            }
            buffer.add(conasList.get(i));
        }
        if (!buffer.isEmpty()) {
            List<SecondarilyVerificationResult> temp = verifyBuffer(buffer);
            result.addAll(temp);
            buffer.clear();
        }
        return result;
    }

    private List<SecondarilyVerificationResult> verifyBuffer(List<Conas> buffer) {
        VerifyConAS.VerifyRun vr = new VerifyConAS.VerifyRun(buffer);
        Thread t = new Thread(vr);
        t.start();
        try {
            t.join(TIMEOUT);
            if (t.isAlive()) {
                t.interrupt();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(VerifyCheckXML.class.getName()).log(Level.ERROR, "Истек таймаут проверки Conas", ex);
        }
        List<SecondarilyVerificationResult> result = vr.secondarilyVerificationResults;
        for (Conas conas : buffer) {
            boolean exist = false;
            for (SecondarilyVerificationResult svr : result) {
                if (svr.getId() == conas.getId()) {
                    exist = true;
                }
            }
            if (exist == false) {
                SecondarilyVerificationResult svr = new SecondarilyVerificationResult(conas.getId());
                svr.setVerifyConas("Не получилось подключиться к серверу AS400");
                result.add(svr);
            }
        }
        return result;
    }

    private byte[] conasToXML(List<Conas> buffer) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("ConasInput");
        Element requestElement = doc.createElement("Requests");
        rootElement.appendChild(requestElement);
        for (Conas pers : buffer) {
            try {
                Element persElement = doc.createElement("ConasRequest");
                persElement.setAttribute("Id", Long.toString(pers.getId()));
                persElement.setAttribute("SNILS", toAS400snils(pers.getNpers()));
                persElement.setAttribute("LastName", pers.getLastName());
                persElement.setAttribute("FirstName", pers.getFirstName());
                persElement.setAttribute("MiddleName", pers.getMiddleName());
                persElement.setAttribute("Sex", pers.getSex());
                persElement.setAttribute("BirthDate", pers.getBirthday());
                requestElement.appendChild(persElement);
            } catch (DOMException e) {
            }
        }
        doc.appendChild(rootElement);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(doc), new StreamResult(baos));
            return baos.toByteArray();
        }
    }

    private String toAS400snils(String npers) {
        String out = "";
        if (npers.charAt(0) == '0') {
            out += npers.substring(1, 3);
        } else {
            out += npers.substring(0, 3);
        }
        out += npers.substring(4, 7) + npers.substring(8, 11);
        return out;
    }

    private void makeFile(Path directory, String fileName) throws IOException {
        File file = new File(directory + "\\" + fileName);
        try (InputStream is = this.getClass().getResourceAsStream("/resources/" + fileName);
                OutputStream os = new FileOutputStream(file)) {
            byte[] b = new byte[1024];
            int count;
            while ((count = is.read(b)) != -1) {
                os.write(b, 0, count);
            }
        }
    }
}
