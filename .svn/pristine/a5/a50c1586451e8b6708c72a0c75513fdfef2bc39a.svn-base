/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifier.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import verifier.Conas;

/**
 *
 * @author 003-0823
 */
public class XMLUtils {

    public static String getFio(byte[] xml) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder;
            builder = f.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml));
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//ФИО[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/Фамилия/text()");
            String fam = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//ФИО[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/Имя/text()");
            String im = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//ФИО[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/Отчество/text()");
            String ot = (String) expr.evaluate(doc, XPathConstants.STRING);
            String fio = fam + " " + im;
            if (!ot.equals("")) {
                fio += " " + ot;
            }
            return fio;
        } catch (IOException | ParserConfigurationException | TransformerException | XPathExpressionException | SAXException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.ERROR, null, ex);
        }
        return "";
    }
    public static String getSnils(byte[] xml) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder;
            builder = f.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml));
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("//ПачкаВходящихДокументов/ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ/СтраховойНомер/text() | //ПачкаВходящихДокументов/ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ/СтраховойНомер/text()");
            String snils = (String) expr.evaluate(doc, XPathConstants.STRING);
            return snils;
        } catch (IOException | ParserConfigurationException | TransformerException | XPathExpressionException | SAXException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.ERROR, null, ex);
        }
        return "";
    }
    public static String getDate(byte[] xml) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder;
            builder = f.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml));
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("//ПачкаВходящихДокументов/ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ/ДатаЗаполнения/text() | //ПачкаВходящихДокументов/ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ/ДатаЗаполнения/text()");
            String date = (String) expr.evaluate(doc, XPathConstants.STRING);
            
            return date;
        } catch (IOException | ParserConfigurationException | TransformerException | XPathExpressionException | SAXException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.ERROR, null, ex);
        }
        return "";
    }
        
    public static String getSender(byte[] xml) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder;
            builder = f.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml));
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("//ПачкаВходящихДокументов/ВХОДЯЩАЯ_ОПИСЬ/СоставительПачки/РегистрационныйНомер/text()");
            String senderregnumber = (String) expr.evaluate(doc, XPathConstants.STRING);
            return senderregnumber;
        } catch (IOException | ParserConfigurationException | TransformerException | XPathExpressionException | SAXException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.ERROR, null, ex);
        }
        return "";
    }
    public static Conas getConas(int id, byte[] xml) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder;
            builder = f.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml));
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//ФИО[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/Фамилия/text()");
            String fam = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//ФИО[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/Имя/text()");
            String im = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//ФИО[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/Отчество/text()");
            String ot = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//СтраховойНомер[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/text()");
            String npers = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//Пол[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/text()");
            String sex = (String) expr.evaluate(doc, XPathConstants.STRING);
            expr = xpath.compile("/ФайлПФР/ПачкаВходящихДокументов//ДатаРождения[parent::ЗАЯВЛЕНИЕ_О_СМЕНЕ_НПФ|parent::ЗАЯВЛЕНИЕ_О_ВЫБОРЕ_НПФ]/text()");
            String birthday = (String) expr.evaluate(doc, XPathConstants.STRING);
            return new Conas(id, fam, im, ot, npers, birthday, sex);
        } catch (Exception ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.ERROR, null, ex);
        }
        return null;
    }
}
