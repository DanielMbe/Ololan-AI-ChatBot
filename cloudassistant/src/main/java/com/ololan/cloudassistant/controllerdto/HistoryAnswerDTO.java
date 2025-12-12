package com.ololan.cloudassistant.controllerdto;

import java.io.Serializable;

public class HistoryAnswerDTO implements Serializable {
    private static final long serialVersionUID = 2682046985632747474L;
    private String hstReply;
    private String hstCommandCode;
    private String hstSubject;
    private String hstQuestion;

    public HistoryAnswerDTO(ImprovementAnswerDTO improvementAnswer) {
        hstReply = improvementAnswer.getReply();
        hstCommandCode = improvementAnswer.getCommandCode();
        hstSubject = improvementAnswer.getSubject();
        hstQuestion = improvementAnswer.getQuestion();
    }

    public String getReply() {
        return hstReply;
    }

    public String getSubject() {
        return hstSubject;
    }

    public String getCommandCode() {
        return hstCommandCode;
    }

    public String getQuestion() {
        return hstQuestion;
    }
}
