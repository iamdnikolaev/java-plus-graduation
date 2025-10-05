package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.client.EventClient;
import ru.practicum.client.UserClient;
import ru.practicum.dto.CommentFullDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Util comment class.
 */
@Component
@RequiredArgsConstructor
public class UtilCommentClass {

    private final CommentRepository commentRepository;
    private final UserClient userClient;
    private final EventClient eventClient;


    /**
     * To comment comment full dto.
     *
     * @param newCommentDto the new comment dto
     * @param eventId       the event id
     * @param userId        the user id
     * @return the comment full dto
     */
    public CommentFullDto toComment(NewCommentDto newCommentDto, Long eventId, Long userId) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());

        eventClient.getEventByIdForAdmin(eventId);
        comment.setEventId(eventId);

        UserDto user = userClient.getUserById(userId);
        comment.setAuthorId(user.getId());

        if (newCommentDto.getParentComment() != null) {
            Comment parentComment = commentRepository.findById(newCommentDto.getParentComment())
                    .orElseThrow(() -> new NotFoundException("Parent comment not found", ""));
            comment.setParent(parentComment);
        }

        LocalDateTime now = LocalDateTime.now();
        comment.setCreated(now);
        comment.setUpdated(now);

        Comment savedComment = commentRepository.save(comment);

        return toCommentFullDto(savedComment);
    }

    /**
     * To comment full dto comment full dto.
     *
     * @param comment the comment
     * @return the comment full dto
     */
    public CommentFullDto toCommentFullDto(Comment comment) {
        UserDto author = userClient.getUserById(comment.getAuthorId());
        EventFullDto event = eventClient.getEventByIdForAdmin(comment.getEventId());

        CommentFullDto dto = new CommentFullDto();
        dto.setId(comment.getId());
        dto.setAuthor(author);
        dto.setEvent(event);
        dto.setText(comment.getText());
        dto.setCreated(comment.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dto.setUpdated(comment.getUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // Обработка parentComment
        if (comment.getParent() != null) {
            dto.setParentComment(comment.getParent());
        }
        return dto;
    }

    /**
     * From comment full dto comment.
     *
     * @param dto the dto
     * @return the comment
     */
    public Comment fromCommentFullDto(CommentFullDto dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setAuthorId(dto.getAuthor().getId());
        comment.setEventId(dto.getEvent().getId());
        comment.setText(dto.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        comment.setCreated(LocalDateTime.parse(dto.getCreated(), formatter));
        comment.setUpdated(LocalDateTime.parse(dto.getUpdated(), formatter));

        if (dto.getParentComment() != null) {
            Comment parentComment = commentRepository.findById(dto.getParentComment().getId())
                    .orElseThrow(() -> new NotFoundException("Parent comment not found", ""));
            comment.setParent(parentComment);
        }

        return comment;

    }
}
