package com.example.mcq_platform_api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.mcq_platform_api.dto.response.OptionResponse;
import com.example.mcq_platform_api.dto.response.QuestionListResponse;
import com.example.mcq_platform_api.dto.response.QuestionResponse;
import com.example.mcq_platform_api.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;
    
    @Test
    public void testGetQuestions() throws Exception {

        QuestionListResponse responseList = createList("math", null);

        Mockito.when(questionService.getQuestions(Mockito.anyString(), Mockito.any(), Mockito.anyInt()))
       .thenReturn(responseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/questions")
            .param("subject", "math")
            .param("size", "2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subject").value("math"))
            .andExpect(jsonPath("$.topic").doesNotExist())
            .andExpect(jsonPath("$.total").value(2))
            .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print());
}
    private QuestionListResponse createList(String subject , String topic){
        
        QuestionResponse q1 = createQuestionResponse(1);
        QuestionResponse q2 = createQuestionResponse(2);
        QuestionListResponse list = new QuestionListResponse();
        list.setQuestions(List.of(q1,q2));
        list.setSessionId("34");
        list.setSubject(subject);
        list.setTopic(topic);
        list.setTotal(2);
        return list;
    }
    private QuestionResponse createQuestionResponse(int id){
        QuestionResponse qr1 = new QuestionResponse();
        qr1.setNumber(id);
        qr1.setQuestionText("sample text");
        qr1.setQuestionId(UUID.randomUUID().toString());
        OptionResponse or1 = new OptionResponse();
        OptionResponse or2 = new OptionResponse();
        OptionResponse or3 = new OptionResponse();
        or1.setLabel('a');
        or2.setLabel('b');
        or3.setLabel('c');
        or1.setOptionText("option1");
        or2.setOptionText("option2");
        or3.setOptionText("option3");
        qr1.setOptions(List.of(or1,or2,or3));
        return qr1;
    }
}
