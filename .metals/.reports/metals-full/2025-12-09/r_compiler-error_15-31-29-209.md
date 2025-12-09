file:///C:/typescript-code/taskboard/src/main/java/com/example/taskboard/service/BoardService.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.1
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.1\scala3-library_3-3.3.1.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.10\scala-library-2.13.10.jar [exists ]
Options:



action parameters:
uri: file:///C:/typescript-code/taskboard/src/main/java/com/example/taskboard/service/BoardService.java
text:
```scala
package com.example.taskboard.service;

import com.example.taskboard.dto.BoardDTO;
import com.example.taskboard.model.Board;
import com.example.taskboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    
    private final BoardRepository boardRepository;
    
    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BoardDTO> getBoardsByOwner(String owner) {
        return boardRepository.findByOwner(owner).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BoardDTO> getAccessibleBoards(String username) {
        return boardRepository.findByOwnerOrIsPublicTrue(username, true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public BoardDTO getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return convertToDTO(board);
    }
    
    @Transactional
    public BoardDTO createBoard(BoardDTO boardDTO) {
        Board board = convertToEntity(boardDTO);
        Board savedBoard = boardRepository.save(board);
        return convertToDTO(savedBoard);
    }
    
    @Transactional
    public BoardDTO updateBoard(Long id, BoardDTO boardDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        
        board.setName(boardDTO.getName());
        board.setDescription(boardDTO.getDescription());
        board.setIsPublic(boardDTO.getIsPublic());
        
        Board updatedBoard = boardRepository.save(board);
        return convertToDTO(updatedBoard);
    }
    
    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
    
    private BoardDTO convertToDTO(Board board) {
        return new BoardDTO(
            board.getId(),
            board.getName(),
            board.getDescription(),
            board.getOwner(),
            board.getIsPublic(),
            board.getCreatedAt(),
            board.getUpdatedAt()
        );
    }
    
    private Board convertToEntity(BoardDTO dto) {
        Board board = new Board();
        board.setId(dto.getId());
        board.setName(dto.getName());
        board.setDescription(dto.getDescription());
        board.setOwner(dto.getOwner());
        board.setIsPublic(dto.getIsPublic() != null ? dto.getIsPublic() : false);
        return board;
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
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:44)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:90)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:109)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator