package com.example.taskboard.service;

import com.example.taskboard.dto.TaskDTO;
import com.example.taskboard.model.Task;
import com.example.taskboard.model.TaskStatus;
import com.example.taskboard.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return convertToDTO(task);
    }
    
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
    
    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setAssignedTo(taskDTO.getAssignedTo());
        task.setDueDate(taskDTO.getDueDate());
        
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }
    
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    @Transactional
    public TaskDTO moveTask(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }
    
    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getAssignedTo(),
            task.getDueDate(),
            task.getCreatedAt(),
            task.getUpdatedAt(),
            task.getCreatedBy()
        );
    }
    
    private Task convertToEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.TODO);
        task.setPriority(dto.getPriority());
        task.setAssignedTo(dto.getAssignedTo());
        task.setDueDate(dto.getDueDate());
        task.setCreatedBy(dto.getCreatedBy());
        return task;
    }
}