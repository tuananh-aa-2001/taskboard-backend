package com.example.taskboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    
    private MessageType type;
    private TaskDTO task;
    private String username;
    private String message;
    
    public enum MessageType {
        TASK_CREATED,
        TASK_UPDATED,
        TASK_DELETED,
        TASK_MOVED,
        USER_JOINED,
        USER_LEFT,
        USER_TYPING,
        USER_LIST
    }
}