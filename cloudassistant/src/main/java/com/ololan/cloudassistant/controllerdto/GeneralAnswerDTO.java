package com.ololan.cloudassistant.controllerdto;

import java.io.Serializable;

public class GeneralAnswerDTO implements Serializable {
    private static final long serialVersionUID = 2682046985632747474L;
    private String gnlReply;
    private String gnlCommandCode;
    private String gnlSubject;
    private String gnlQuestion;

    public GeneralAnswerDTO(ImprovementAnswerDTO improvementAnswer) {
        gnlReply = improvementAnswer.getReply();
        gnlCommandCode = improvementAnswer.getCommandCode();
        gnlSubject = improvementAnswer.getSubject();
        gnlQuestion = improvementAnswer.getQuestion();
    }

    public String getReply() {
        return gnlReply;
    }

    public String getSubject() {
        return gnlSubject;
    }

    public String getCommandCode() {
        return gnlCommandCode;
    }

    public String getQuestion() {
        return gnlQuestion;
    }
}
