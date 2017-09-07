package com.chat.beans;

public class DialogRequest {
    private String oneWhoCalls;
    private String theCallee;
    private String requestAnswer;
    private String whos;

    public DialogRequest(String oneWhoCalls, String theCallee, String whos) {
        this.oneWhoCalls = oneWhoCalls;
        this.theCallee = theCallee;
        this.requestAnswer = "false";
        this.whos = whos;
    }

    public String getOneWhoCalls() {
        return oneWhoCalls;
    }

    public String getTheCallee() {
        return theCallee;
    }

    public String getRequestAnswer() {
        return requestAnswer;
    }

    public String getWhos() {
        return whos;
    }
}
