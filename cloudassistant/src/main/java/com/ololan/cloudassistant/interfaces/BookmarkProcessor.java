package com.ololan.cloudassistant.interfaces;

import java.util.List;

import com.ololan.cloudassistant.controllerdata.BookmarkAnswer;
import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;

public interface BookmarkProcessor {
    public BookmarkAnswer processBookmarkInput(String input);

    public BookmarkAnswer processBmkQuestionReply(String questionReply, String cmdCode);

    public List<String> tokenizeBookmarkInput(String input);

    public String detectBookmarkSentiment(String input);

    public String detectBookmarkKeyword(List<String> dataList);

    public List<String> detectBookmarkCommand(String keyword, List<String> dataList);

    public List<String> detectBmkQuestionReply(String code, List<String> dataList);

    public List<StringBuilder> getBookmarkCommand(List<String> dataList, StringBuilder queryCmd,
            StringBuilder endSubject, StringBuilder middleSubject, List<String> tokenizedCommand,
            int cmdIndex);

    public List<BookmarkAnswer> getBookmarkKeywordList();

    public List<BookmarkAnswer> getBookmarkCommandList(String keyword);

    public List<ImprovementQueReply> getBmkQuestionReplyList(String commandCode);

    public BookmarkAnswer getBookmarkAnswer(String sentiment, List<String> command, String cmdSentiment);

    public List<String> sendBookmarkSuggestion(String typingText);
}
