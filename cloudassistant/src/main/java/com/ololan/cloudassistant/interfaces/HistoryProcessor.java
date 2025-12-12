package com.ololan.cloudassistant.interfaces;

import java.util.List;

import com.ololan.cloudassistant.controllerdata.HistoryAnswer;
import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;

public interface HistoryProcessor {
    public HistoryAnswer processHistoryInput(String input);

    public HistoryAnswer processHistQuestionReply(String questionReply, String cmdCode);

    public List<String> tokenizeHistoryInput(String input);

    public String detectHistorySentiment(String input);

    public String detectHistoryKeyword(List<String> dataList);

    public List<String> detectHistoryCommand(String keyword, List<String> dataList);

    public List<String> detectHistQuestionReply(String code, List<String> dataList);

    public List<StringBuilder> getHistoryCommand(List<String> dataList, StringBuilder queryCmd,
            StringBuilder endSubject, StringBuilder middleSubject, List<String> tokenizedCommand,
            int cmdIndex);

    public List<HistoryAnswer> getHistoryKeywordList();

    public List<HistoryAnswer> getHistoryCommandList(String keyword);

    public List<ImprovementQueReply> getHistQuestionReplyList(String commandCode);

    public HistoryAnswer getHistoryAnswer(String sentiment, List<String> command, String cmdSentiment);

    public List<String> sendHistorySuggestion(String typingText);
}
