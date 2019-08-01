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
public class PrimarilyVerificationResult {

    private String verifyException;
    private String verifySignature;
    private String verifyCertificate;
    private String verifyFIO;

    protected PrimarilyVerificationResult() {
        verifyException = null;
        verifyFIO = "Ошибка";
        verifySignature = "Ошибка";
        verifyCertificate = "Ошибка";
    }

    protected void setVerifyCertCount(int count) throws Exception {
        if (count > 1) {
            //verifyException = "У подписи должен быть ровно один подписант";
            throw new Exception("У подписи должен быть ровно один подписант");
        }
        if (count == 0) {
            //verifyException = "В подписи не найден сертификат";
            throw new Exception("В подписи не найден сертификат");
        }
    }

    protected void setVerifyException(String exception) {
        verifyException = exception;
    }

    protected void setVerifySignature(String result) {
        if (result.equals("true")) {
            verifySignature = "Успешно";
        } else {
            verifySignature = result;
        }
    }

    protected void setVerifyCertificate(String result) {
        verifyCertificate = VerifyCertificate.translateErrorMessage(result);
    }

    protected void setVerifyFIO(boolean result) {
        if (result) {
            verifyFIO = "Успешно";
        } else {
            verifyFIO = "Ошибка";
        }
    }

    public String getVerifyException() {
        return verifyException;
    }

    public String getVerifySignature() {
        return verifySignature;
    }

    public String getVerifyCertificate() {
        return verifyCertificate;
    }

    public String getVerifyFIO() {
        return verifyFIO;
    }
}
