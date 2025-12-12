package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "historyanswers")
public class HistoryAnswer {
    private int hstId;
    private String hstKeyword;
    private String hstCommand;
    private int hstHasMiddleSubject;
    private int hstHasEndSubject;

    @Id
    @GeneratedValue
    public int getId() {
        return hstId;
    }

    @Column(name = "keyword", nullable = false)
    public String getKeyword() {
        return hstKeyword;
    }

    @Column(name = "command", nullable = false)
    public String getCommand() {
        return hstCommand;
    }

    @Column(name = "middlesubjectstate", nullable = false)
    public int getHasMiddleSubject() {
        return hstHasMiddleSubject;
    }

    @Column(name = "endsubjectstate", nullable = false)
    public int getHasEndSubject() {
        return hstHasEndSubject;
    }

    public void setId(int id) {
        hstId = id;
    }

    public void setKeyword(String keyword) {
        hstKeyword = keyword;
    }

    public void setCommand(String command) {
        hstCommand = command;
    }

    public void setHasMiddleSubject(int middlesubjectstate) {
        hstHasMiddleSubject = middlesubjectstate;
    }

    public void setHasEndSubject(int endsubjectstate) {
        hstHasEndSubject = endsubjectstate;
    }
}
