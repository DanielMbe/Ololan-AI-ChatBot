package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.ImprovementReply;

@Repository
public interface ImprovementReplyRepository extends JpaRepository<ImprovementReply, Integer> {
    public List<ImprovementReply> findByCommandCode(String commandcode);

    public List<ImprovementReply> findAll();
}
