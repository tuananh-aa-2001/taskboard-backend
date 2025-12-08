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