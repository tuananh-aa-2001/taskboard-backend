file:///C:/typescript-code/taskboard/src/main/java/com/example/taskboard/dto/WebSocketMessage.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.1
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.1\scala3-library_3-3.3.1.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.10\scala-library-2.13.10.jar [exists ]
Options:



action parameters:
offset: 582
uri: file:///C:/typescript-code/taskboard/src/main/java/com/example/taskboard/dto/WebSocketMessage.java
text:
```scala
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
    private Long commentId;
    private Long taskId;
    
    public WebSocketMessage(MessageType type, TaskDTO task, String username, String message) {
        this.type = type;
        this.task = task;
        this.username = username;
  @@      this.message = message;
    }
    
    public enum MessageType {
        TASK_CREATED,
        TASK_UPDATED,
        TASK_DELETED,
        TASK_MOVED,
        USER_JOINED,
        USER_LEFT,
        USER_TYPING,
        USER_LIST,
        COMMENT_CREATED,
        COMMENT_DELETED
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