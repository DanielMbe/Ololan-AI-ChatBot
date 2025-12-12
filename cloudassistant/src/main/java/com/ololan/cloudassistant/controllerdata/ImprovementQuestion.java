package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "improvementcmdquestion")
public class ImprovementQuestion {
    private int impQId;
    private String impQQuestion;
    private String impQCommandCode;

    @Id
    @GeneratedValue
    public int getId() {
        return impQId;
    }

    @Column(name = "question", nullable = false)
    public String getQuestion() {
        return impQQuestion;
    }

    @Column(name = "commandcode", nullable = false)
    public String getCommandCode() {
        return impQCommandCode;
    }

    public void setId(int id) {
        impQId = id;
    }

    public void setQuestion(String question) {
        impQQuestion = question;
    }

    public void setCommandCode(String commandcode) {
        impQCommandCode = commandcode;
    }
}
