package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.ImprovementQuestion;

@Repository
public interface ImprovementQuestionRepository extends JpaRepository<ImprovementQuestion, Integer> {
    public List<ImprovementQuestion> findByCommandCode(String commandcode);

    public List<ImprovementQuestion> findAll();
}
