package com.ololan.cloudassistant.interfaces;

import java.util.List;

import com.ololan.cloudassistant.controllerdata.DownloadAnswer;
import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;

public interface DownloadProcessor {
    public DownloadAnswer processDownloadInput(String input);

    public DownloadAnswer processDwnQuestionReply(String questionReply, String cmdCode);

    public List<String> tokenizeDownloadInput(String input);

    public String detectDownloadSentiment(String input);

    public String detectDownloadKeyword(List<String> dataList);

    public List<String> detectDownloadCommand(String keyword, List<String> dataList);

    public List<String> detectDwnQuestionReply(String code, List<String> dataList);

    public List<StringBuilder> getDownloadCommand(List<String> dataList, StringBuilder queryCmd,
            StringBuilder endSubject, StringBuilder middleSubject, List<String> tokenizedCommand,
            int cmdIndex);

    public List<DownloadAnswer> getDownloadKeywordList();

    public List<DownloadAnswer> getDownloadCommandList(String keyword);

    public List<ImprovementQueReply> getDwnQuestionReplyList(String commandCode);

    public DownloadAnswer getDownloadAnswer(String sentiment, List<String> command, String cmdSentiment);

    public List<String> sendDownloadSuggestion(String typingText);
}
