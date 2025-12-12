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

import com.ololan.cloudassistant.controllerdata.HistoryAnswer;
import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;
import com.ololan.cloudassistant.controllerdto.HistoryAnswerDTO;
import com.ololan.cloudassistant.controllerdto.ImprovementAnswerDTO;
import com.ololan.cloudassistant.repository.HistoryRepository;
import com.ololan.cloudassistant.repository.ImprovementQueReplyRepository;

@RestController
@RequestMapping("/historyanswers")
public class HistoryProcessorResource {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private ImprovementQueReplyRepository iQueReplyRepository;
    private StringBuilder sentimentValue;
    private ImprovementProcessorResource iProcessorResource;
    // private SparkSession sparkSession;
    // PretrainedPipeline pipelineSentiment;

    public HistoryProcessorResource(ImprovementProcessorResource iProcessor) {
        sentimentValue = new StringBuilder("");
        iProcessorResource = iProcessor;

        /*
         * sparkSession =
         * SparkSession.builder().appName("Ololan_Cloud_AI").master("local[*]")
         * .config("spark.driver.memory", "1G").config("spark.driver.maxResultSize",
         * "1G")
         * .config("spark.kryoserializer.buffer.max", "1024M")
         * .config("spark.jars.packages",
         * "com.johnsnowlabs.nlp:spark-nlp_2.12:5.2.0").getOrCreate();
         */
        // pipelineSentiment = new PretrainedPipeline("analyze_sentiment", "en");
    }

    @GetMapping(value = "/commandInput/{input}")
    public HistoryAnswerDTO processHistoryInput(@PathVariable("input") String input) {
        String sentiment = detectHistorySentiment(input);
        List<String> wordsList = tokenizeHistoryInput(input);
        String keyword = detectHistoryKeyword(wordsList);
        List<String> command = detectHistoryCommand(keyword, wordsList);

        if (command.isEmpty()) {
            String erroString = "unknown";
            ImprovementAnswerDTO improveAnswer = new ImprovementAnswerDTO();
            improveAnswer.setCommandCode(erroString);
            improveAnswer.setQuestion(erroString);
            improveAnswer.setReply("Sorry, i do not understand in this context.");
            improveAnswer.setSubject(erroString);
            return new HistoryAnswerDTO(improveAnswer);
        }

        return getHistoryAnswer(sentiment, command, sentimentValue.toString().trim());
    }

    @GetMapping(value = "/questionInput/{questionReply}")
    public HistoryAnswerDTO processHistQuestionReply(@PathVariable(name = "questionReply") String questionReply) {
        String[] results = questionReply.split(":::");
        String sentiment = detectHistorySentiment(results[0]);
        List<String> wordsList = tokenizeHistoryInput(results[0]);
        List<String> command = detectHistQuestionReply(results[1], wordsList);

        if (command.isEmpty()) {
            String erroString = "unknown";
            ImprovementAnswerDTO improveAnswer = new ImprovementAnswerDTO();
            improveAnswer.setCommandCode(erroString);
            improveAnswer.setQuestion(erroString);
            improveAnswer.setReply("Sorry, i do not understand in this context.");
            improveAnswer.setSubject(erroString);
            return new HistoryAnswerDTO(improveAnswer);
        } else {
            command.add(results[1]);
        }

        return getHistoryAnswer(sentiment, command, sentimentValue.toString().trim());
    }

    public List<String> tokenizeHistoryInput(String input) {
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

    public String detectHistorySentiment(String input) {
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

    public String detectHistoryKeyword(List<String> dataList) {
        boolean found = false;
        StringBuilder keyword = new StringBuilder("");
        List<HistoryAnswer> keywordList = getHistoryKeywordList();

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

    public List<String> detectHistoryCommand(String keyword, List<String> dataList) {
        List<String> command = new ArrayList<>();
        List<HistoryAnswer> commandList = getHistoryCommandList(keyword);

        for (int i = 0; i < commandList.size(); i++) {
            StringBuilder queryCmd = new StringBuilder("");
            StringBuilder endSubject = new StringBuilder("");
            StringBuilder middleSubject = new StringBuilder("");
            List<String> tokenizedCommand = tokenizeHistoryInput(commandList.get(i).getCommand());

            if (dataList.size() >= tokenizedCommand.size()) {
                int cmdIndex = 0;
                List<StringBuilder> results = getHistoryCommand(dataList, queryCmd, endSubject, middleSubject,
                        tokenizedCommand, cmdIndex);
                queryCmd = results.get(0);
                middleSubject = results.get(1);
                endSubject = results.get(2);

                if (commandList.get(i).getCommand().compareTo(queryCmd.toString().trim()) == 0) {
                    command.add(queryCmd.toString().trim());
                    sentimentValue = new StringBuilder(detectHistorySentiment(commandList.get(i).getCommand()));

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

    public List<String> detectHistQuestionReply(String code, List<String> dataList) {
        List<String> command = new ArrayList<>();
        List<ImprovementQueReply> commandList = getHistQuestionReplyList(code);

        for (int i = 0; i < commandList.size(); i++) {
            StringBuilder queryCmd = new StringBuilder("");
            StringBuilder endSubject = new StringBuilder("");
            StringBuilder middleSubject = new StringBuilder("");
            List<String> tokenizedCommand = tokenizeHistoryInput(commandList.get(i).getQuestionReply());

            if (dataList.size() >= tokenizedCommand.size()) {
                int cmdIndex = 0;
                List<StringBuilder> results = getHistoryCommand(dataList, queryCmd, endSubject, middleSubject,
                        tokenizedCommand, cmdIndex);
                queryCmd = results.get(0);
                middleSubject = results.get(1);
                endSubject = results.get(2);

                if (commandList.get(i).getQuestionReply().compareTo(queryCmd.toString().trim()) == 0) {
                    command.add(queryCmd.toString().trim());
                    sentimentValue = new StringBuilder(detectHistorySentiment(commandList.get(i).getQuestionReply()));

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

    public List<StringBuilder> getHistoryCommand(List<String> dataList, StringBuilder queryCmd,
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

    public List<HistoryAnswer> getHistoryKeywordList() {
        return historyRepository.findAll();
    }

    public List<HistoryAnswer> getHistoryCommandList(String keyword) {
        return historyRepository.findHistoryAnswerByKeyword(keyword);
    }

    public List<ImprovementQueReply> getHistQuestionReplyList(String commandCode) {
        return iQueReplyRepository.findByCommandCode(commandCode);
    }

    public HistoryAnswerDTO getHistoryAnswer(String sentiment, List<String> command, String cmdSentiment) {
        ImprovementAnswerDTO answer = iProcessorResource.processNodesData(sentiment, command, cmdSentiment);
        return new HistoryAnswerDTO(answer);
    }

    @GetMapping(value = "/suggestionInput/{typingText}")
    public List<String> sendHistorySuggestion(@PathVariable(name = "typingText") String typingText) {
        List<HistoryAnswer> entriesList = historyRepository.findAll();
        List<String> suggestionsList = new ArrayList<>();

        for (HistoryAnswer entry : entriesList) {
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
