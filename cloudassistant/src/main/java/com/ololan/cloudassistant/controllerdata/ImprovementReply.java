package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "improvementcmdreply")
public class ImprovementReply {
    private int impRId;
    private String impRAnswer;
    private String impRCommandCode;

    @Id
    @GeneratedValue
    public int getId() {
        return impRId;
    }

    @Column(name = "reply", nullable = false)
    public String getReply() {
        return impRAnswer;
    }

    @Column(name = "commandcode", nullable = false)
    public String getCommandCode() {
        return impRCommandCode;
    }

    public void setId(int id) {
        impRId = id;
    }

    public void setReply(String reply) {
        impRAnswer = reply;
    }

    public void setCommandCode(String commandcode) {
        impRCommandCode = commandcode;
    }
}
