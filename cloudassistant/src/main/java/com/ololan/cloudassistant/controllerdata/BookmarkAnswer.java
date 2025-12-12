package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "bookmarkanswers")
public class BookmarkAnswer {
    private int bmkId;
    private String bmkKeyword;
    private String bmkCommand;
    private int bmkHasMiddleSubject;
    private int bmkHasEndSubject;

    @Id
    @GeneratedValue
    public int getId() {
        return bmkId;
    }

    @Column(name = "keyword", nullable = false)
    public String getKeyword() {
        return bmkKeyword;
    }

    @Column(name = "command", nullable = false)
    public String getCommand() {
        return bmkCommand;
    }

    @Column(name = "middlesubjectstate", nullable = false)
    public int getHasMiddleSubject() {
        return bmkHasMiddleSubject;
    }

    @Column(name = "endsubjectstate", nullable = false)
    public int getHasEndSubject() {
        return bmkHasEndSubject;
    }

    public void setId(int id) {
        bmkId = id;
    }

    public void setKeyword(String keyword) {
        bmkKeyword = keyword;
    }

    public void setCommand(String command) {
        bmkCommand = command;
    }

    public void setHasMiddleSubject(int middlesubjectstate) {
        bmkHasMiddleSubject = middlesubjectstate;
    }

    public void setHasEndSubject(int endsubjectstate) {
        bmkHasEndSubject = endsubjectstate;
    }
}
