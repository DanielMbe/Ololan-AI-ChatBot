package com.ololan.cloudassistant.interfaces;

import java.util.List;

import com.ololan.cloudassistant.controllerdata.GeneralAnswer;
import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;

public interface GeneralProcessor {
    public GeneralAnswer processGeneralInput(String input);

    public GeneralAnswer processGnlQuestionReply(String questionReply, String cmdCode);

    public List<String> tokenizeGeneralInput(String input);

    public String detectGeneralSentiment(String input);

    public String detectGeneralKeyword(List<String> dataList);

    public List<String> detectGeneralCommand(String keyword, List<String> dataList);

    public List<String> detectGnlQuestionReply(String code, List<String> dataList);

    public List<StringBuilder> getGeneralCommand(List<String> dataList, StringBuilder queryCmd,
            StringBuilder endSubject, StringBuilder middleSubject, List<String> tokenizedCommand,
            int cmdIndex);

    public List<GeneralAnswer> getGeneralKeywordList();

    public List<GeneralAnswer> getGeneralCommandList(String keyword);

    public List<ImprovementQueReply> getGnlQuestionReplyList(String commandCode);

    public GeneralAnswer getGeneralAnswer(String sentiment, List<String> command, String cmdSentiment);

    public List<String> sendGeneralSuggestion(String typingText);
}
