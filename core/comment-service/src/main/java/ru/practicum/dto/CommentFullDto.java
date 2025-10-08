package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.Comment;

/**
 * The type Comment full dto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentFullDto {
    Long id;
    UserDto author;
    EventFullDto event;
    String text;
    String created;
    String updated;
    Comment parentComment;
}
