package com.ololan.cloudassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ololan.cloudassistant.controllerdata.BookmarkAnswer;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkAnswer, Integer> {
    public List<BookmarkAnswer> findBookmarkAnswerByKeyword(String keyword);

    public List<BookmarkAnswer> findAll();
}
