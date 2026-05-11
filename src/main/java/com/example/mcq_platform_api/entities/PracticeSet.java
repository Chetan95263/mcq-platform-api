package com.example.mcq_platform_api.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PracticeSet {
    @Id
    private String id;
    private String userId;
    private String topic;
    private String subject;
    private String dateAndTime;   

    @OneToOne(mappedBy = "practiceSet", cascade = jakarta.persistence.CascadeType.ALL)
    private PracticeSetItem practiceSetItems;  
} 

