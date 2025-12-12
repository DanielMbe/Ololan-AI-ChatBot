package com.ololan.cloudassistant.controllerdto;

import java.io.Serializable;

public class ImprovementAnswerDTO implements Serializable {
    private static final long serialVersionUID = 2682046985632747474L;
    private String impQuestion;
    private String impCommandCode;
    private String impReply;
    private String impSubject;

    public ImprovementAnswerDTO() {
        // Empty constructor
    }

    public String getQuestion() {
        return impQuestion;
    }

    public String getCommandCode() {
        return impCommandCode;
    }

    public String getReply() {
        return impReply;
    }

    public String getSubject() {
        return impSubject;
    }

    public void setQuestion(String question) {
        impQuestion = question;
    }

    public void setCommandCode(String commandCode) {
        impCommandCode = commandCode;
    }

    public void setReply(String reply) {
        impReply = reply;
    }

    public void setSubject(String subject) {
        impSubject = subject;
    }
}
