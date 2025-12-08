package com.example.taskboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Long taskId;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}