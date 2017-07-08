package com.blu.imdg.example10.dto;

/**
 * Created by mikl on 08.01.17.
 */
public class ValidateResponse {
    public static ValidateResponse OK =new ValidateResponse(true);
    public static ValidateResponse DENIED =new ValidateResponse(false);


    private Boolean result;
    private String error;

    public ValidateResponse() {
    }

    public ValidateResponse(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public ValidateResponse(Boolean result) {
        this.result = result;
    }

    public ValidateResponse(String error) {
        this.error = error;
    }

    public static ValidateResponse error(String errorText) {
        return new ValidateResponse(errorText);
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ValidateResponse{" +
                "result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
