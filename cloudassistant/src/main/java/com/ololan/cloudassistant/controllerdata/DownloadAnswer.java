package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "downloadanswers")
public class DownloadAnswer {
    private int dwnId;
    private String dwnKeyword;
    private String dwnCommand;
    private int dwnHasMiddleSubject;
    private int dwnHasEndSubject;

    @Id
    @GeneratedValue
    public int getId() {
        return dwnId;
    }

    @Column(name = "keyword", nullable = false)
    public String getKeyword() {
        return dwnKeyword;
    }

    @Column(name = "command", nullable = false)
    public String getCommand() {
        return dwnCommand;
    }

    @Column(name = "middlesubjectstate", nullable = false)
    public int getHasMiddleSubject() {
        return dwnHasMiddleSubject;
    }

    @Column(name = "endsubjectstate", nullable = false)
    public int getHasEndSubject() {
        return dwnHasEndSubject;
    }

    public void setId(int id) {
        dwnId = id;
    }

    public void setKeyword(String keyword) {
        dwnKeyword = keyword;
    }

    public void setCommand(String command) {
        dwnCommand = command;
    }

    public void setHasMiddleSubject(int middlesubjectstate) {
        dwnHasMiddleSubject = middlesubjectstate;
    }

    public void setHasEndSubject(int endsubjectstate) {
        dwnHasEndSubject = endsubjectstate;
    }
}
