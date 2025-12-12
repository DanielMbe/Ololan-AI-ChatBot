package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.DownloadAnswer;

@Repository
public interface DownloadRepository extends JpaRepository<DownloadAnswer, Integer> {
    public List<DownloadAnswer> findDownloadAnswerByKeyword(String keyword);

    public List<DownloadAnswer> findAll();
}
