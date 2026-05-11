package com.example.mcq_platform_api.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    private String id;
    private String questionText;
    private String subject; 
    private String topic;
    private String explanation;

    @OneToMany(mappedBy = "question", cascade = jakarta.persistence.CascadeType.ALL)
    private List<Option> options; 
    
    @ManyToMany(mappedBy = "questions", cascade = jakarta.persistence.CascadeType.ALL)
    private List<PracticeSetItem> practiceSetItems;
    
}
