package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.HistoryAnswer;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryAnswer, Integer> {
    public List<HistoryAnswer> findHistoryAnswerByKeyword(String keyword);

    public List<HistoryAnswer> findAll();
}
