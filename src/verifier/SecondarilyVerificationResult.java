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
public class SecondarilyVerificationResult {

    private final int id;
    private String verifyException;
    private String verifyConas;
    private String verifyCheckXML;

    protected SecondarilyVerificationResult(int id) {
        this.id = id;
        verifyConas = null;
        verifyCheckXML = null;
    }

    protected void setVerifyException(String exception) {
        verifyException = exception;
    }

    protected void setVerifyConas(String result) {
        verifyConas = result;
    }

    protected void setVerifyCheckXML(String result) {
        verifyCheckXML = result;
    }

    public int getId() {
        return id;
    }
    
    public String getVerifyException() {
        return verifyException;
    }

    public String getConasResult() {
        return verifyConas;
    }

    public String getCheckXMLResult() {
        return verifyCheckXML;
    }
}
