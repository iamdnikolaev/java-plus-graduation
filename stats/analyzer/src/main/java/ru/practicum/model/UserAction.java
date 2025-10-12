package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@Table(name = "user_actions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(ActionId.class)
public class UserAction {

    @Id
    Long userId;
    @Id
    Long eventId;

    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    UserActionType userActionType;

    @Column(name = "created")
    Instant timestamp;

    double weight;
}

