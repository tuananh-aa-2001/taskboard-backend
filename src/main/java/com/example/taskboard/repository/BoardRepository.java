package com.example.taskboard.repository;

import com.example.taskboard.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOwner(String owner);
    List<Board> findByIsPublicTrue();
    List<Board> findByOwnerOrIsPublicTrue(String owner, Boolean isPublic);
}