package com.example.mcq_platform_api.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.mcq_platform_api.cache.AnswerCache;
import com.example.mcq_platform_api.cache.AnswerListCache;
import com.example.mcq_platform_api.cache.QuestionListCache;
import com.example.mcq_platform_api.dto.request.QuestionRequest;
import com.example.mcq_platform_api.dto.request.QuestionUpdateRequest;
import com.example.mcq_platform_api.dto.response.AnswerResponse;
import com.example.mcq_platform_api.dto.response.OptionResponse;
import com.example.mcq_platform_api.dto.response.QuestionListResponse;
import com.example.mcq_platform_api.dto.response.QuestionResponse;
import com.example.mcq_platform_api.entities.Option;
import com.example.mcq_platform_api.entities.Question;
import com.example.mcq_platform_api.exception.BadRequestException;
import com.example.mcq_platform_api.exception.ResourceNotFoundException;
import com.example.mcq_platform_api.repository.QuestionRepo;

import jakarta.transaction.Transactional;


@Service
public class QuestionService {

    private final AnswerListCache answerListCacheService;
    private final AnswerCache answerCacheService;
    private final QuestionRepo questionRepo;
    private final QuestionListCache questionListCacheService;

    public QuestionService(AnswerListCache answerListCacheService , QuestionRepo questionRepo , AnswerCache answerCacheService , QuestionListCache questionListCacheService) {
        this.questionRepo = questionRepo;
        this.answerListCacheService = answerListCacheService;
        this.answerCacheService = answerCacheService;
        this.questionListCacheService = questionListCacheService;
    }

    public int saveAllQuestion(List<QuestionRequest> questionRequests) {
        List<Question> questions = new ArrayList<>();
        for (QuestionRequest qr : questionRequests) {
            if (qr.getQuestionText() == null)
                throw new BadRequestException("questionText Cannot be null");

            if (qr.getOptions() == null || qr.getOptions().size() < 2)
                throw new BadRequestException("At least add 2 or more options");

            // 1. Create Question 
            Question question = Question.builder()
                .id(UUID.randomUUID().toString())
                .subject(qr.getSubject())   
                .topic(qr.getTopic())
                .explanation(qr.getExplanation())
                .questionText(qr.getQuestionText())
                .build();

            // 2. Map OptionRequest → Option
            List<Option> options = qr.getOptions().stream().map(or -> {

            if (or.getOptionText() == null)
                throw new BadRequestException("Option text cannot be null");

            return Option.builder()
                    .id(UUID.randomUUID().toString())
                    .optionText(or.getOptionText())
                    .correct(or.isCorrect())
                    .question(question)
                    .build();

            }).toList();

            // 3. Set options in question
            question.setOptions(options);

            // 4. Add to list
            questions.add(question);
        }
        return questionRepo.saveAll(questions).size();
    }
    @Transactional
    public void updateQuestion(List<QuestionUpdateRequest> requestList) {

        List<String> ids = requestList.stream()
                .map(QuestionUpdateRequest::getQuestionId)
                .toList();

        Map<String, Question> questionMap = questionRepo.findAllById(ids)
            .stream()
            .collect(Collectors.toMap(Question::getId, q -> q));

        for (QuestionUpdateRequest req : requestList) {
            Question q = questionMap.get(req.getQuestionId());
            if (q == null) {
                throw new ResourceNotFoundException(
                    "Question not found with id: " + req.getQuestionId());
            }

            // update fields
            q.setQuestionText(req.getQuestionText());
            q.setExplanation(req.getExplanation());
            q.setSubject(req.getSubject());
            q.setTopic(req.getTopic());

            // replace options
            q.getOptions().clear();

            List<Option> newOptions = req.getOptions().stream().map(or -> {
                Option option = new Option();
                option.setId(UUID.randomUUID().toString());
                option.setOptionText(or.getOptionText());
                option.setCorrect(or.isCorrect());
                option.setQuestion(q);
                return option;
            }).toList();

            q.getOptions().addAll(newOptions);
        }

        // single save call (better)
        questionRepo.saveAll(questionMap.values());
    }
    public void deleteQuestionByQuestionId(String questionId){
        Question q = questionRepo.findById(questionId).orElseThrow(()->new ResourceNotFoundException("Question not found with id:"+questionId));
        questionRepo.delete(q);
    }
    public QuestionResponse getQuestionById(String questionId){
        Question q = questionRepo.findById(questionId).orElseThrow(()->new ResourceNotFoundException("Question not found with id:"+questionId));
        QuestionResponse qr = new QuestionResponse();
        qr.setNumber(1);
        qr.setQuestionId(questionId);
        qr.setQuestionText(q.getQuestionText());
        List<Option> optionList = q.getOptions();
        List<OptionResponse> optionResponses = new ArrayList<>();
        char c = 'a';
        for(Option option : optionList){
            optionResponses.add(new OptionResponse(c,option.getOptionText())); 
            if(option.isCorrect()){
                answerCacheService.cacheAnswer(new AnswerResponse(questionId , c , option.getOptionText()));
            }     
            c++;
        }
        qr.setOptions(optionResponses);
        return qr;
    }
    public AnswerResponse getAnswerByQuestionId(String id){
        Question q = questionRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Question not found with id:"+id));
        char c = 'a';
        AnswerResponse answerResponse = null;
        for(Option option : q.getOptions()){
            if(option.isCorrect()) {
                answerResponse = new AnswerResponse(id , c++ , option.getOptionText());
                answerCacheService.cacheAnswer(answerResponse);
            }
        }
        return answerResponse;
    }
    public QuestionListResponse getQuestions(String subject, String topic, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Question> questionPage;

        if (subject != null && topic != null) {
            questionPage = questionRepo.findBySubjectAndTopic(subject, topic, pageable);
        } 
        else if (subject != null) {
            questionPage = questionRepo.findBySubject(subject, pageable);
        } 
        else if (topic != null) {
            questionPage = questionRepo.findByTopic(topic, pageable);
        } 
        else {
            questionPage = questionRepo.findAll(pageable);
        }
        if(questionPage.getTotalElements() == 0) throw new ResourceNotFoundException("Question not found");
        QuestionListResponse questionListResponse = mapToQuestionListResponse(questionPage.getContent(), subject, topic );
        questionListCacheService.cacheQuestion(questionListResponse.getSessionId(), questionPage.getContent());
        questionListCacheService.cacheQuestionListResponse(questionListResponse.getSessionId(), questionListResponse);
        return questionListResponse;
    }
    public QuestionListResponse mapToQuestionListResponse(List<Question> questions , String subject , String topic) {
        List<QuestionResponse> response = new ArrayList<>();
        int number = 1;
        List<AnswerResponse> answers = new ArrayList<>();
        for (Question question : questions) {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setNumber(number++);
            questionResponse.setQuestionId(question.getId());
            questionResponse.setQuestionText(question.getQuestionText());
            List <OptionResponse> optionResponses = new ArrayList<>();
            char c = 'a';
            for(Option option : question.getOptions()){
                OptionResponse optionResponse = new OptionResponse();
                optionResponse.setLabel(c);
                optionResponse.setOptionText(option.getOptionText());
                optionResponses.add(optionResponse);
                if(option.isCorrect()){
                    answers.add(new AnswerResponse(question.getId(), c, option.getOptionText()));
                }
                c++;
            }
            questionResponse.setOptions(optionResponses);
            response.add(questionResponse);
        }    
        QuestionListResponse listResponse = new QuestionListResponse();
        listResponse.setQuestions(response);
        listResponse.setSubject(subject);
        listResponse.setTopic(topic);
        listResponse.setTotal(questions.size());
        listResponse.setSessionId(answerListCacheService.createSession(answers));

        if(subject != null && !questions.isEmpty() && topic != null){
            listResponse.setSubject(subject);
            listResponse.setTopic(topic);
        }

        else if(subject == null && !questions.isEmpty() && topic != null){
            listResponse.setSubject(questions.get(0).getSubject());
            listResponse.setTopic(topic);
        }
        
        else if(subject != null && !questions.isEmpty() && topic == null){
            listResponse.setSubject(subject);
            listResponse.setTopic("Mixed");
        }
        else{
            listResponse.setSubject("Mixed");
            listResponse.setTopic("Mixed");
        }
        return listResponse;
    }

}
