package com.example.taskboard.controller;

import com.example.taskboard.dto.TaskDTO;
import com.example.taskboard.dto.WebSocketMessage;
import com.example.taskboard.service.TaskService;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final TaskService taskService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    @MessageMapping("/task.create")
    @SendTo("/topic/tasks")
    public WebSocketMessage createTask(TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return new WebSocketMessage(
                WebSocketMessage.MessageType.TASK_CREATED,
                createdTask,
                taskDTO.getCreatedBy(),
                "Task created: " + createdTask.getTitle());
    }

    @MessageMapping("/task.update")
    @SendTo("/topic/tasks")
    public WebSocketMessage updateTask(TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(taskDTO.getId(), taskDTO);
        return new WebSocketMessage(
                WebSocketMessage.MessageType.TASK_UPDATED,
                updatedTask,
                null,
                "Task updated: " + updatedTask.getTitle());
    }

    @MessageMapping("/task.delete")
    @SendTo("/topic/tasks")
    public WebSocketMessage deleteTask(TaskDTO taskDTO) {
        taskService.deleteTask(taskDTO.getId());
        return new WebSocketMessage(
                WebSocketMessage.MessageType.TASK_DELETED,
                taskDTO,
                null,
                "Task deleted: " + taskDTO.getTitle());
    }

    @MessageMapping("/task.move")
    @SendTo("/topic/tasks")
    public WebSocketMessage moveTask(TaskDTO taskDTO) {
        TaskDTO movedTask = taskService.moveTask(taskDTO.getId(), taskDTO.getStatus());
        return new WebSocketMessage(
                WebSocketMessage.MessageType.TASK_MOVED,
                movedTask,
                null,
                "Task moved to " + movedTask.getStatus());
    }

    @MessageMapping("/user.join")
    public void userJoined(String username) {
        activeUsers.add(username);

        // Send USER_JOINED message with the joining user
        WebSocketMessage joinMessage = new WebSocketMessage(
            WebSocketMessage.MessageType.USER_JOINED,
            null,
            username,
            username + " joined the board"
        );
        messagingTemplate.convertAndSend("/topic/users", joinMessage);
        // Send USER_LIST message with all active users to sync everyone
        WebSocketMessage listMessage = new WebSocketMessage(
            WebSocketMessage.MessageType.USER_LIST,
            null,
            String.join(",", activeUsers), // Send comma-separated list
            "Active users list"
        );
        messagingTemplate.convertAndSend("/topic/users", listMessage);
    }

   @MessageMapping("/user.leave")
    public void userLeft(String username) {
        activeUsers.remove(username);
        
        // Notify all users that someone left
        WebSocketMessage leaveMessage = new WebSocketMessage(
            WebSocketMessage.MessageType.USER_LEFT,
            null,
            username,
            username + " left the board"
        );
        messagingTemplate.convertAndSend("/topic/users", leaveMessage);
        
        // Send updated user list
        WebSocketMessage listMessage = new WebSocketMessage(
            WebSocketMessage.MessageType.USER_LIST,
            null,
            String.join(",", activeUsers),
            "Active users list"
        );
        messagingTemplate.convertAndSend("/topic/users", listMessage);
    }
}