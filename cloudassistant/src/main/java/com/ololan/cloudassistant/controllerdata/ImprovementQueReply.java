package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "improvementquestionreply")
public class ImprovementQueReply {
    private int impQRId;
    private String impQRAnswer;
    private String impQRCommandCode;
    private int impHasMiddleSubject;
    private int impHasEndSubject;

    @Id
    @GeneratedValue
    public int getId() {
        return impQRId;
    }

    @Column(name = "questionreply", nullable = false)
    public String getQuestionReply() {
        return impQRAnswer;
    }

    @Column(name = "commandcode", nullable = false)
    public String getCommandCode() {
        return impQRCommandCode;
    }

    @Column(name = "middlesubjectstate", nullable = false)
    public int getHasMiddleSubject() {
        return impHasMiddleSubject;
    }

    @Column(name = "endsubjectstate", nullable = false)
    public int getHasEndSubject() {
        return impHasEndSubject;
    }

    public void setId(int id) {
        impQRId = id;
    }

    public void setQuestionReply(String questionReply) {
        impQRAnswer = questionReply;
    }

    public void setCommandCode(String commandcode) {
        impQRCommandCode = commandcode;
    }

    public void setHasMiddleSubject(int middlesubjectstate) {
        impHasMiddleSubject = middlesubjectstate;
    }

    public void setHasEndSubject(int endsubjectstate) {
        impHasEndSubject = endsubjectstate;
    }
}
