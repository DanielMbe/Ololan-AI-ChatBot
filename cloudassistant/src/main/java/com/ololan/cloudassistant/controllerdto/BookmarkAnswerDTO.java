package com.ololan.cloudassistant.controllerdto;

import java.io.Serializable;

public class BookmarkAnswerDTO implements Serializable {
    private static final long serialVersionUID = 2682046985632747474L;
    private String bmkReply;
    private String bmkCommandCode;
    private String bmkSubject;
    private String bmkQuestion;

    public BookmarkAnswerDTO(ImprovementAnswerDTO improvementAnswer) {
        bmkReply = improvementAnswer.getReply();
        bmkCommandCode = improvementAnswer.getCommandCode();
        bmkSubject = improvementAnswer.getSubject();
        bmkQuestion = improvementAnswer.getQuestion();
    }

    public String getReply() {
        return bmkReply;
    }

    public String getSubject() {
        return bmkSubject;
    }

    public String getCommandCode() {
        return bmkCommandCode;
    }

    public String getQuestion() {
        return bmkQuestion;
    }
}
