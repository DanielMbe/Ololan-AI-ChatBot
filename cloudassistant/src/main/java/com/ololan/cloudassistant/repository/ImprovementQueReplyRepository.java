package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.ImprovementQueReply;

@Repository
public interface ImprovementQueReplyRepository extends JpaRepository<ImprovementQueReply, Integer> {
    public List<ImprovementQueReply> findByCommandCode(String commandcode);

    public List<ImprovementQueReply> findAll();
}
