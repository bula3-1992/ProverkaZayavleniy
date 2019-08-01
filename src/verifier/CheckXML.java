/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifier;

/**
 *
 * @author 003-0823
 */
public class CheckXML{

    private final byte[] xml;
    private final String filename;
    private String result;
    
    public CheckXML(byte[] xml,
            String filename) {
        this.xml = xml;
        this.filename = filename;
        result = null;
    }

    public byte[] getXml() {
        return xml;
    }

    public String getFilename() {
        return filename;
    }

    protected void setVerifyСheckXML(String result) {
        this.result = result;
    }

    public String getVerifyСheckXML() {
        return result;
    }
}
