package com.ololan.cloudassistant.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ololan.cloudassistant.controllerdata.ImprovementCmd;
import com.ololan.cloudassistant.controllerdata.ImprovementReply;
import com.ololan.cloudassistant.controllerdata.ImprovementQuestion;
import com.ololan.cloudassistant.controllerdto.ImprovementAnswerDTO;
import com.ololan.cloudassistant.repository.ImprovementCmdRepository;
import com.ololan.cloudassistant.repository.ImprovementReplyRepository;
import com.ololan.cloudassistant.repository.ImprovementQuestionRepository;

@RestController
public class ImprovementProcessorResource {
    @Autowired
    private ImprovementCmdRepository commandRepository;
    @Autowired
    private ImprovementReplyRepository replyRepository;
    @Autowired
    private ImprovementQuestionRepository questionRepository;

    public ImprovementProcessorResource() {
        // empty constructor
    }

    public ImprovementAnswerDTO processNodesData(String sentiment, List<String> command, String cmdSentiment) {
        String commandCode = (command.size() > 3) ? command.get(3) : getCommandCode(command.get(0));
        StringBuilder commandQuestion = new StringBuilder("");
        StringBuilder commandReply = new StringBuilder("");

        if ((command.get(2).compareTo("wrong argument") == 0) ||
                (command.get(2).compareTo("missing argument") == 0)) {
            commandQuestion.append(getQuestion(commandCode));
            commandReply.append("");
        } else {
            // "good argument", "negative argument", "none"
            commandQuestion.append("");
            commandReply.append(detectCognitiveReply(sentiment, cmdSentiment, detectReply(commandCode)));
        }

        ImprovementAnswerDTO improveAnswer = new ImprovementAnswerDTO();
        improveAnswer.setCommandCode(commandCode);
        improveAnswer.setQuestion(commandQuestion.toString());
        improveAnswer.setReply(commandReply.toString());

        if (command.get(2).compareTo("good argument") == 0) {
            improveAnswer.setSubject(command.get(1));
        } else {
            improveAnswer.setSubject("");
        }

        return improveAnswer;
    }

    public String getQuestion(String cmdCode) {
        List<ImprovementQuestion> questionList = questionRepository.findAll();
        StringBuilder question = new StringBuilder("");

        for (ImprovementQuestion questCmd : questionList) {
            if (cmdCode.compareTo(questCmd.getCommandCode()) == 0) {
                question.append(questCmd.getQuestion());
                break;
            }
        }
        return question.toString();
    }

    public String getCommandCode(String value) {
        List<ImprovementCmd> commandList = commandRepository.findByCommand(value);
        return commandList.get(0).getCommandCode();
    }

    public List<String> detectReply(String commandCode) {
        List<ImprovementReply> replyList = replyRepository.findByCommandCode(commandCode);
        List<String> replies = new ArrayList<>();

        for (ImprovementReply reply : replyList) {
            replies.add(reply.getReply());
        }

        return replies;
    }

    public String detectCognitiveReply(String sentiment, String cmdSentiment, List<String> replyList) {
        Random rand = new Random();
        if (sentiment.compareTo(cmdSentiment) == 0) {
            return replyList.get(rand.nextInt(replyList.size() - 1));
        } else {
            return replyList.get(replyList.size() - 1);
        }
    }
}
