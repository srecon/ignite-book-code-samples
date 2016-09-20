package com.blu.imdg.example3;

/**
 * Created by mikl on 12.07.16.
 */
public class ValidateMessage {

    private String id;
    private String msg;
    private byte[] xsd ;
    private String js;

    public ValidateMessage(String id, String msg, byte[] xsd, String js) {
        this.id = id;
        this.msg = msg;
        this.xsd = xsd;
        this.js = js;
    }

    public String getMsg() {
        return msg;
    }

    public byte[] getXsd() {
        return xsd;
    }

    public String getJs() {
        return js;
    }

    public String getId() {
        return id;
    }
}
