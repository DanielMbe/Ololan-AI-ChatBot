package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "generalanswers")
public class GeneralAnswer {
    private int gnlId;
    private String gnlKeyword;
    private String gnlCommand;
    private int gnlHasMiddleSubject;
    private int gnlHasEndSubject;

    @Id
    @GeneratedValue
    public int getId() {
        return gnlId;
    }

    @Column(name = "keyword", nullable = false)
    public String getKeyword() {
        return gnlKeyword;
    }

    @Column(name = "command", nullable = false)
    public String getCommand() {
        return gnlCommand;
    }

    @Column(name = "middlesubjectstate", nullable = false)
    public int getHasMiddleSubject() {
        return gnlHasMiddleSubject;
    }

    @Column(name = "endsubjectstate", nullable = false)
    public int getHasEndSubject() {
        return gnlHasEndSubject;
    }

    public void setId(int id) {
        gnlId = id;
    }

    public void setKeyword(String keyword) {
        gnlKeyword = keyword;
    }

    public void setCommand(String command) {
        gnlCommand = command;
    }

    public void setHasMiddleSubject(int middlesubjectstate) {
        gnlHasMiddleSubject = middlesubjectstate;
    }

    public void setHasEndSubject(int endsubjectstate) {
        gnlHasEndSubject = endsubjectstate;
    }
}
