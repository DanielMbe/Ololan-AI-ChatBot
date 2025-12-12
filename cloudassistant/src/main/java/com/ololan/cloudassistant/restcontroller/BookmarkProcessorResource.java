package com.ololan.cloudassistant.restcontroller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ololan.cloudassistant.controllerdata.BookmarkAnswer;
import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;
import com.ololan.cloudassistant.controllerdto.BookmarkAnswerDTO;
import com.ololan.cloudassistant.controllerdto.ImprovementAnswerDTO;
import com.ololan.cloudassistant.repository.BookmarkRepository;
import com.ololan.cloudassistant.repository.ImprovementQueReplyRepository;

@RestController
@RequestMapping("/bookmarkanswers")
public class BookmarkProcessorResource {
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private ImprovementQueReplyRepository iQueReplyRepository;
    private StringBuilder sentimentValue;
    private ImprovementProcessorResource iProcessorResource;
    // private SparkSession sparkSession;
    // PretrainedPipeline pipelineSentiment;

    public BookmarkProcessorResource(ImprovementProcessorResource iProcessor) {
        sentimentValue = new StringBuilder("");
        iProcessorResource = iProcessor;

        /*
         * sparkSession =
         * SparkSession.builder().appName("Ololan_Cloud_AI").master("local[*]")
         * .config("spark.driver.memory", "1G").config("spark.driver.maxResultSize",
         * "1G")
         * .config("spark.kryoserializer.buffer.max", "1024M")
         * .config("spark.jars.packages", "com.johnsnowlabs.nlp:spark-nlp_2.12:5.2.0")
         * .getOrCreate();
         */
        // pipelineSentiment = new PretrainedPipeline("analyze_sentiment", "en");
    }

    @GetMapping(value = "/commandInput/{input}")
    public BookmarkAnswerDTO processBookmarkInput(@PathVariable("input") String input) {
        String sentiment = detectBookmarkSentiment(input);
        List<String> wordsList = tokenizeBookmarkInput(input);
        String keyword = detectBookmarkKeyword(wordsList);
        List<String> command = detectBookmarkCommand(keyword, wordsList);

        if (command.isEmpty()) {
            String erroString = "unknown";
            ImprovementAnswerDTO improveAnswer = new ImprovementAnswerDTO();
            improveAnswer.setCommandCode(sentiment);
            improveAnswer.setQuestion(erroString);
            improveAnswer.setReply("Sorry, i do not understand in this context.");
            improveAnswer.setSubject(erroString);
            return new BookmarkAnswerDTO(improveAnswer);
        }

        return getBookmarkAnswer(sentiment, command, sentimentValue.toString().trim());
    }

    @GetMapping(value = "/questionInput/{questionReply}")
    public BookmarkAnswerDTO processBmkQuestionReply(@PathVariable(name = "questionReply") String questionReply) {
        String[] results = questionReply.split(":::");
        String sentiment = detectBookmarkSentiment(results[0]);
        List<String> wordsList = tokenizeBookmarkInput(results[0]);
        List<String> command = detectBmkQuestionReply(results[1], wordsList);

        if (command.isEmpty()) {
            String erroString = "unknown";
            ImprovementAnswerDTO improveAnswer = new ImprovementAnswerDTO();
            improveAnswer.setCommandCode(erroString);
            improveAnswer.setQuestion(erroString);
            improveAnswer.setReply("Sorry, i do not understand in this context.");
            improveAnswer.setSubject(erroString);
            return new BookmarkAnswerDTO(improveAnswer);
        } else {
            command.add(results[1]);
        }

        return getBookmarkAnswer(sentiment, command, sentimentValue.toString().trim());
    }

    public List<String> tokenizeBookmarkInput(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        List<String> result = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken().trim());
        }
        if (result.isEmpty()) {
            result.add("none");
        }

        return result;
    }

    public String detectBookmarkSentiment(String input) {
        /*
         * Map<String, Seq<String>> annotation =
         * JavaConversions.mapAsJavaMap(pipelineSentiment.annotate(input));
         * List<String> sentiment =
         * JavaConversions.seqAsJavaList(annotation.get("sentiment"));
         * return sentiment.get(0);
         */
        Random rand = new Random();
        return (rand.nextInt(input.length()) > 5 ? "negative" : "positive");
    }

    public String detectBookmarkKeyword(List<String> dataList) {
        boolean found = false;
        StringBuilder keyword = new StringBuilder("");
        List<BookmarkAnswer> keywordList = getBookmarkKeywordList();

        for (int i = 0; i < keywordList.size(); i++) {
            for (int j = 0; j < dataList.size(); j++) {
                if (dataList.get(j).compareTo(keywordList.get(i).getKeyword()) == 0) {
                    keyword.append(keywordList.get(i).getKeyword());
                    found = true;
                    break;
                }
            }

            if (found) {
                break;
            }
        }
        return keyword.toString().trim();
    }

    public List<String> detectBookmarkCommand(String keyword, List<String> dataList) {
        List<String> command = new ArrayList<>();
        List<BookmarkAnswer> commandList = getBookmarkCommandList(keyword);

        for (int i = 0; i < commandList.size(); i++) {
            StringBuilder queryCmd = new StringBuilder("");
            StringBuilder endSubject = new StringBuilder("");
            StringBuilder middleSubject = new StringBuilder("");
            List<String> tokenizedCommand = tokenizeBookmarkInput(commandList.get(i).getCommand());

            if (dataList.size() >= tokenizedCommand.size()) {
                int cmdIndex = 0;
                List<StringBuilder> results = getBookmarkCommand(dataList, queryCmd, endSubject, middleSubject,
                        tokenizedCommand, cmdIndex);
                queryCmd = results.get(0);
                middleSubject = results.get(1);
                endSubject = results.get(2);

                if (commandList.get(i).getCommand().compareTo(queryCmd.toString().trim()) == 0) {
                    command.add(queryCmd.toString().trim());
                    sentimentValue = new StringBuilder(detectBookmarkSentiment(commandList.get(i).getCommand()));

                    if (!middleSubject.toString().isEmpty()) {
                        command.add(middleSubject.toString().trim());
                    } else if (!endSubject.toString().isEmpty()) {
                        command.add(endSubject.toString().trim());
                    } else {
                        command.add("");
                    }

                    String good = "good argument";
                    String wrong = "wrong argument";
                    String missing = "missing argument";
                    String negative = "negative argument";
                    String none = "none";

                    if (commandList.get(i).getHasMiddleSubject() == 1) {
                        if (!middleSubject.toString().isEmpty()) {
                            command.add(good);
                        } else if (!endSubject.toString().isEmpty()) {
                            command.add(wrong);
                        } else {
                            command.add(missing);
                        }
                    } else if (commandList.get(i).getHasEndSubject() == 1) {
                        if (!endSubject.toString().isEmpty()) {
                            command.add(good);
                        } else if (!middleSubject.toString().isEmpty()) {
                            command.add(wrong);
                        } else {
                            command.add(missing);
                        }
                    } else {
                        if ((!middleSubject.toString().isEmpty()) || (!endSubject.toString().isEmpty())) {
                            command.add(negative);
                        } else {
                            command.add(none);
                        }
                    }
                    break;
                }
            }
        }
        return command;
    }

    public List<String> detectBmkQuestionReply(String code, List<String> dataList) {
        List<String> command = new ArrayList<>();
        List<ImprovementQueReply> commandList = getBmkQuestionReplyList(code);

        for (int i = 0; i < commandList.size(); i++) {
            StringBuilder queryCmd = new StringBuilder("");
            StringBuilder endSubject = new StringBuilder("");
            StringBuilder middleSubject = new StringBuilder("");
            List<String> tokenizedCommand = tokenizeBookmarkInput(commandList.get(i).getQuestionReply());

            if (dataList.size() >= tokenizedCommand.size()) {
                int cmdIndex = 0;
                List<StringBuilder> results = getBookmarkCommand(dataList, queryCmd, endSubject, middleSubject,
                        tokenizedCommand, cmdIndex);
                queryCmd = results.get(0);
                middleSubject = results.get(1);
                endSubject = results.get(2);

                if (commandList.get(i).getQuestionReply().compareTo(queryCmd.toString().trim()) == 0) {
                    command.add(queryCmd.toString().trim());
                    sentimentValue = new StringBuilder(detectBookmarkSentiment(commandList.get(i).getQuestionReply()));

                    if (!middleSubject.toString().isEmpty()) {
                        command.add(middleSubject.toString().trim());
                    } else if (!endSubject.toString().isEmpty()) {
                        command.add(endSubject.toString().trim());
                    } else {
                        command.add("");
                    }

                    String good = "good argument";
                    String wrong = "wrong argument";
                    String missing = "missing argument";
                    String negative = "negative argument";
                    String none = "none";

                    if (commandList.get(i).getHasMiddleSubject() == 1) {
                        if (!middleSubject.toString().isEmpty()) {
                            command.add(good);
                        } else if (!endSubject.toString().isEmpty()) {
                            command.add(wrong);
                        } else {
                            command.add(missing);
                        }
                    } else if (commandList.get(i).getHasEndSubject() == 1) {
                        if (!endSubject.toString().isEmpty()) {
                            command.add(good);
                        } else if (!middleSubject.toString().isEmpty()) {
                            command.add(wrong);
                        } else {
                            command.add(missing);
                        }
                    } else {
                        if ((!middleSubject.toString().isEmpty()) || (!endSubject.toString().isEmpty())) {
                            command.add(negative);
                        } else {
                            command.add(none);
                        }
                    }
                    break;
                }
            }
        }
        return command;
    }

    public List<StringBuilder> getBookmarkCommand(List<String> dataList, StringBuilder queryCmd,
            StringBuilder endSubject, StringBuilder middleSubject, List<String> tokenizedCommand,
            int cmdIndex) {
        for (int j = 0; j < dataList.size(); j++) {
            if (cmdIndex < tokenizedCommand.size()) {
                if (dataList.get(j).compareTo(tokenizedCommand.get(cmdIndex)) == 0) {
                    queryCmd.append(tokenizedCommand.get(cmdIndex) + " ");
                    cmdIndex++;
                } else if (cmdIndex > 0) {
                    middleSubject.append(dataList.get(j) + " ");
                }
            } else {
                endSubject.append(dataList.get(j) + " ");
            }
        }

        List<StringBuilder> results = new ArrayList<>();
        results.add(queryCmd);
        results.add(middleSubject);
        results.add(endSubject);
        return results;
    }

    public List<BookmarkAnswer> getBookmarkKeywordList() {
        return bookmarkRepository.findAll();
    }

    public List<BookmarkAnswer> getBookmarkCommandList(String keyword) {
        return bookmarkRepository.findBookmarkAnswerByKeyword(keyword);
    }

    public List<ImprovementQueReply> getBmkQuestionReplyList(String commandCode) {
        return iQueReplyRepository.findByCommandCode(commandCode);
    }

    public BookmarkAnswerDTO getBookmarkAnswer(String sentiment, List<String> command, String cmdSentiment) {
        ImprovementAnswerDTO answer = iProcessorResource.processNodesData(sentiment, command, cmdSentiment);
        return new BookmarkAnswerDTO(answer);
    }

    @GetMapping(value = "/suggestionInput/{typingText}")
    public List<String> sendBookmarkSuggestion(@PathVariable(name = "typingText") String typingText) {
        List<BookmarkAnswer> entriesList = bookmarkRepository.findAll();
        List<String> suggestionsList = new ArrayList<>();

        for (BookmarkAnswer entry : entriesList) {
            boolean match = false;

            if (entry.getCommand().startsWith(typingText) || typingText.contains(entry.getCommand())) {
                match = true;
            }
            if (match) {
                suggestionsList.add(entry.getCommand());
            }
        }

        suggestionsList.sort(Comparator.reverseOrder());
        return suggestionsList;
    }
}
