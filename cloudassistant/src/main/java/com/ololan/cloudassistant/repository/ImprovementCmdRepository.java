package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.ImprovementCmd;

@Repository
public interface ImprovementCmdRepository extends JpaRepository<ImprovementCmd, Integer> {
    public List<ImprovementCmd> findByCommand(String command);

    public List<ImprovementCmd> findAll();
}
