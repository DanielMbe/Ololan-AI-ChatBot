package com.ololan.cloudassistant.controllerdto;

import java.io.Serializable;

public class DownloadAnswerDTO implements Serializable {
    private static final long serialVersionUID = 2682046985632747474L;
    private String dwnReply;
    private String dwnCommandCode;
    private String dwnSubject;
    private String dwnQuestion;

    public DownloadAnswerDTO(ImprovementAnswerDTO improvementAnswer) {
        dwnReply = improvementAnswer.getReply();
        dwnCommandCode = improvementAnswer.getCommandCode();
        dwnSubject = improvementAnswer.getSubject();
        dwnQuestion = improvementAnswer.getQuestion();
    }

    public String getReply() {
        return dwnReply;
    }

    public String getSubject() {
        return dwnSubject;
    }

    public String getCommandCode() {
        return dwnCommandCode;
    }

    public String getQuestion() {
        return dwnQuestion;
    }
}
