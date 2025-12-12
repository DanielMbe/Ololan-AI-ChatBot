package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.GeneralAnswer;

@Repository
public interface GeneralRepository extends JpaRepository<GeneralAnswer, Integer> {
    public List<GeneralAnswer> findGeneralAnswerByKeyword(String keyword);

    public List<GeneralAnswer> findAll();
}
