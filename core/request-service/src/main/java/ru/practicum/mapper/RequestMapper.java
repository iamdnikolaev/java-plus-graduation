package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.client.EventClient;
import ru.practicum.client.UserClient;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.enums.RequestStatus;
import ru.practicum.model.Request;

import java.time.LocalDateTime;

/**
 * The type Request mapper.
 */
@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final UserClient userClient;
    private final EventClient eventClient;

    /**
     * To participation request dto participation request dto.
     *
     * @param request the request
     * @return the participation request dto
     */
    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getEventId(),
                request.getRequesterId(),
                request.getStatus(),
                request.getCreated()
        );
    }

    /**
     * To request request.
     *
     * @param participationRequestDto the participation request dto
     * @param requesterId             the requester id
     * @param eventId                 the event id
     * @return the request
     */
    public Request toRequest(ParticipationRequestDto participationRequestDto, Long requesterId, Long eventId) {
        return new Request(
                null,
                eventClient.getEventByIdForAdmin(eventId).getId(),
                userClient.getUserById(requesterId).getId(),
                participationRequestDto.getStatus(),
                participationRequestDto.getCreated()
        );
    }

    /**
     * Form user and event to request request.
     *
     * @param user  the user
     * @param event the event
     * @return the request
     */
    public Request formUserAndEventToRequest(UserDto user, EventFullDto event) {
        if (user == null || event == null) {
            return null;
        }

        Request request = new Request();
        request.setEventId(event.getId());
        request.setRequesterId(user.getId());
        request.setStatus(setStatus(event));
        request.setCreated(LocalDateTime.now());

        return request;
    }

    private RequestStatus setStatus(EventFullDto event) {
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return RequestStatus.CONFIRMED;
        }
        return RequestStatus.PENDING;
    }
}
