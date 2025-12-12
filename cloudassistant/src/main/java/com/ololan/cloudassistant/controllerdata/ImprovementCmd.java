package com.ololan.cloudassistant.controllerdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "improvementcommand")
public class ImprovementCmd {
    private int impCId;
    private String impCCommand;
    private String impCCommandCode;

    @Id
    @GeneratedValue
    public int getId() {
        return impCId;
    }

    @Column(name = "command", nullable = false)
    public String getCommand() {
        return impCCommand;
    }

    @Column(name = "commandcode", nullable = false)
    public String getCommandCode() {
        return impCCommandCode;
    }

    public void setId(int id) {
        impCId = id;
    }

    public void setCommand(String command) {
        impCCommand = command;
    }

    public void setCommandCode(String commandcode) {
        impCCommandCode = commandcode;
    }
}
