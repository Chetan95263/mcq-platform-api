package com.example.mcq_platform_api.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PracticeSetItem {
    @Id
    private String id;
    
    @ManyToOne
    private PracticeSet practiceSet;

    @ManyToMany
    private List<Question> questions;

}
