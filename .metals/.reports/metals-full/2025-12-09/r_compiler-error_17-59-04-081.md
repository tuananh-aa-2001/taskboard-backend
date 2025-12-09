file:///C:/typescript-code/taskboard/src/main/java/com/example/taskboard/controller/WebSocketController.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.1
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.1\scala3-library_3-3.3.1.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.10\scala-library-2.13.10.jar [exists ]
Options:



action parameters:
offset: 793
uri: file:///C:/typescript-code/taskboard/src/main/java/com/example/taskboard/controller/WebSocketController.java
text:
```scala
package com.example.taskboard.controller;

import com.example.taskboard.dto.TaskDTO;
import com.example.taskboard.dto.CommentDTO;
import com.example.taskboard.dto.WebSocketMessage;
import com.example.taskboard.service.TaskService;
import com.example.taskboard.service.CommentService;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final TaskService taskService;
    private final CommentService commentService;
    private final SimpMess@@agingTemplate messagingTemplate;
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    @MessageMapping("/task.create")
    public void  createTask(TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        WebSocketMessage message = new WebSocketMessage(
                WebSocketMessage.MessageType.TASK_CREATED,
                createdTask,
                taskDTO.getCreatedBy(),
                "Task created: " + createdTask.getTitle());
        messagingTemplate.convertAndSend("/topic/board/" + createdTask.getBoardId(), message);
    }

    @MessageMapping("/task.update")
    public void  updateTask(TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(taskDTO.getId(), taskDTO);
        WebSocketMessage message = new WebSocketMessage(
                WebSocketMessage.MessageType.TASK_UPDATED,
                updatedTask,
                null,
                "Task updated: " + updatedTask.getTitle());
        messagingTemplate.convertAndSend("/topic/board/" + updatedTask.getBoardId(), message);
    }

    @MessageMapping("/task.delete")
    public void deleteTask(TaskDTO taskDTO) {
        taskService.deleteTask(taskDTO.getId());
        WebSocketMessage message = new WebSocketMessage(
            WebSocketMessage.MessageType.TASK_DELETED,
            taskDTO,
            null,
            "Task deleted: " + taskDTO.getTitle()
        );
        messagingTemplate.convertAndSend("/topic/board/" + taskDTO.getBoardId(), message);
    }

    @MessageMapping("/task.move")
    public void moveTask(TaskDTO taskDTO) {
        TaskDTO movedTask = taskService.moveTask(taskDTO.getId(), taskDTO.getStatus());
        WebSocketMessage message = new WebSocketMessage(
                WebSocketMessage.MessageType.TASK_MOVED,
                movedTask,
                null,
                "Task moved to " + movedTask.getStatus());
        messagingTemplate.convertAndSend("/topic/board/" + movedTask.getBoardId(), message);
    }

    @MessageMapping("/comment.create")
    public void createComment(CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(commentDTO);
        WebSocketMessage message = new WebSocketMessage(
            WebSocketMessage.MessageType.COMMENT_CREATED,
            null,
            createdComment.getAuthor(),
            createdComment.getContent()
        );
        message.setCommentId(createdComment.getId());
        message.setTaskId(createdComment.getTaskId());
        // Send to task-specific topic
        messagingTemplate.convertAndSend("/topic/task/" + createdComment.getTaskId() + "/comments", message);
    }
    
    @MessageMapping("/comment.delete")
    public void deleteComment(CommentDTO commentDTO) {
        commentService.deleteComment(commentDTO.getId());
        WebSocketMessage message = new WebSocketMessage(
            WebSocketMessage.MessageType.COMMENT_DELETED,
            null,
            null,
            "Comment deleted"
        );
        message.setCommentId(commentDTO.getId());
        message.setTaskId(commentDTO.getTaskId());
        messagingTemplate.convertAndSend("/topic/task/" + commentDTO.getTaskId() + "/comments", message);
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
```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:933)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:168)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.HoverProvider$.hover(HoverProvider.scala:34)
	scala.meta.internal.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:352)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator