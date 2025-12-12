package com.ololan.cloudassistant.interfaces;

import java.util.List;

import com.ololan.cloudassistant.controllerdto.ImprovementAnswerDTO;

public interface ImprovementProcessor {
    public ImprovementAnswerDTO processNodesData(String sentiment, List<String> command, String cmdSentiment);

    public String getQuestion(String cmdCode);

    public List<String> detectReply(String commandCode);

    public String getCommandCode(String value);

    public String detectCognitiveReply(String sentiment, String cmdSentiment, List<String> replyList);
}
