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

import com.ololan.cloudassistant.controllerdata.DownloadAnswer;
import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;
import com.ololan.cloudassistant.controllerdto.DownloadAnswerDTO;
import com.ololan.cloudassistant.controllerdto.ImprovementAnswerDTO;
import com.ololan.cloudassistant.repository.DownloadRepository;
import com.ololan.cloudassistant.repository.ImprovementQueReplyRepository;

@RestController
@RequestMapping("/downloadanswers")
public class DownloadProcessorResource {
    @Autowired
    private DownloadRepository downloadRepository;
    @Autowired
    private ImprovementQueReplyRepository iQueReplyRepository;
    private StringBuilder sentimentValue;
    private ImprovementProcessorResource iProcessorResource;
    // private SparkSession sparkSession;
    // PretrainedPipeline pipelineSentiment;

    public DownloadProcessorResource(ImprovementProcessorResource iProcessor) {
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
    public DownloadAnswerDTO processDownloadInput(@PathVariable("input") String input) {
        String sentiment = detectDownloadSentiment(input);
        List<String> wordsList = tokenizeDownloadInput(input);
        String keyword = detectDownloadKeyword(wordsList);
        List<String> command = detectDownloadCommand(keyword, wordsList);

        if (command.isEmpty()) {
            String erroString = "unknown";
            ImprovementAnswerDTO improveAnswer = new ImprovementAnswerDTO();
            improveAnswer.setCommandCode(erroString);
            improveAnswer.setQuestion(erroString);
            improveAnswer.setReply("Sorry, i do not understand in this context.");
            improveAnswer.setSubject(erroString);
            return new DownloadAnswerDTO(improveAnswer);
        }

        return getDownloadAnswer(sentiment, command, sentimentValue.toString().trim());
    }

    @GetMapping(value = "/questionInput/{questionReply}")
    public DownloadAnswerDTO processDwnQuestionReply(@PathVariable(name = "questionReply") String questionReply) {
        String[] results = questionReply.split(":::");
        String sentiment = detectDownloadSentiment(results[0]);
        List<String> wordsList = tokenizeDownloadInput(results[0]);
        List<String> command = detectDwnQuestionReply(results[1], wordsList);

        if (command.isEmpty()) {
            String erroString = "unknown";
            ImprovementAnswerDTO improveAnswer = new ImprovementAnswerDTO();
            improveAnswer.setCommandCode(erroString);
            improveAnswer.setQuestion(erroString);
            improveAnswer.setReply("Sorry, i do not understand in this context.");
            improveAnswer.setSubject(erroString);
            return new DownloadAnswerDTO(improveAnswer);
        } else {
            command.add(results[1]);
        }

        return getDownloadAnswer(sentiment, command, sentimentValue.toString().trim());
    }

    public List<String> tokenizeDownloadInput(String input) {
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

    public String detectDownloadSentiment(String input) {
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

    public String detectDownloadKeyword(List<String> dataList) {
        boolean found = false;
        StringBuilder keyword = new StringBuilder("");
        List<DownloadAnswer> keywordList = getDownloadKeywordList();

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

    public List<String> detectDownloadCommand(String keyword, List<String> dataList) {
        List<String> command = new ArrayList<>();
        List<DownloadAnswer> commandList = getDownloadCommandList(keyword);

        for (int i = 0; i < commandList.size(); i++) {
            StringBuilder queryCmd = new StringBuilder("");
            StringBuilder endSubject = new StringBuilder("");
            StringBuilder middleSubject = new StringBuilder("");
            List<String> tokenizedCommand = tokenizeDownloadInput(commandList.get(i).getCommand());

            if (dataList.size() >= tokenizedCommand.size()) {
                int cmdIndex = 0;
                List<StringBuilder> results = getDownloadCommand(dataList, queryCmd, endSubject, middleSubject,
                        tokenizedCommand, cmdIndex);
                queryCmd = results.get(0);
                middleSubject = results.get(1);
                endSubject = results.get(2);

                if (commandList.get(i).getCommand().compareTo(queryCmd.toString().trim()) == 0) {
                    command.add(queryCmd.toString().trim());
                    sentimentValue = new StringBuilder(detectDownloadSentiment(commandList.get(i).getCommand()));

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

    public List<String> detectDwnQuestionReply(String code, List<String> dataList) {
        List<String> command = new ArrayList<>();
        List<ImprovementQueReply> commandList = getDwnQuestionReplyList(code);

        for (int i = 0; i < commandList.size(); i++) {
            StringBuilder queryCmd = new StringBuilder("");
            StringBuilder endSubject = new StringBuilder("");
            StringBuilder middleSubject = new StringBuilder("");
            List<String> tokenizedCommand = tokenizeDownloadInput(commandList.get(i).getQuestionReply());

            if (dataList.size() >= tokenizedCommand.size()) {
                int cmdIndex = 0;
                List<StringBuilder> results = getDownloadCommand(dataList, queryCmd, endSubject, middleSubject,
                        tokenizedCommand, cmdIndex);
                queryCmd = results.get(0);
                middleSubject = results.get(1);
                endSubject = results.get(2);

                if (commandList.get(i).getQuestionReply().compareTo(queryCmd.toString().trim()) == 0) {
                    command.add(queryCmd.toString().trim());
                    sentimentValue = new StringBuilder(detectDownloadSentiment(commandList.get(i).getQuestionReply()));

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

    public List<StringBuilder> getDownloadCommand(List<String> dataList, StringBuilder queryCmd,
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

    public List<DownloadAnswer> getDownloadKeywordList() {
        return downloadRepository.findAll();
    }

    public List<DownloadAnswer> getDownloadCommandList(String keyword) {
        return downloadRepository.findDownloadAnswerByKeyword(keyword);
    }

    public List<ImprovementQueReply> getDwnQuestionReplyList(String commandCode) {
        return iQueReplyRepository.findByCommandCode(commandCode);
    }

    public DownloadAnswerDTO getDownloadAnswer(String sentiment, List<String> command, String cmdSentiment) {
        ImprovementAnswerDTO answer = iProcessorResource.processNodesData(sentiment, command, cmdSentiment);
        return new DownloadAnswerDTO(answer);
    }

    @GetMapping(value = "/suggestionInput/{typingText}")
    public List<String> sendDownloadSuggestion(@PathVariable(name = "typingText") String typingText) {
        List<DownloadAnswer> entriesList = downloadRepository.findAll();
        List<String> suggestionsList = new ArrayList<>();

        for (DownloadAnswer entry : entriesList) {
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
