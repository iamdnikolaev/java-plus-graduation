package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import java.util.List;

/**
 * The type Private event controller.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("users/{user-id}/events")
public class PrivateEventController {

    private static final String USERID = "user-id";
    private static final String EVENTID = "event-id";
    private final RequestService requestService;

    /**
     * Gets request by user and event.
     *
     * @param userId  the user id
     * @param eventId the event id
     * @return the request by user and event
     */
    @GetMapping("/{event-id}/requests")
    public List<ParticipationRequestDto> getRequestByUserAndEvent(@PathVariable(USERID) Long userId,
                                                                  @PathVariable(EVENTID) Long eventId) {
        log.info("Private: get request userId {}, eventId {}", userId, eventId);
        return requestService.getRequestByUserAndEvent(userId, eventId);
    }

    /**
     * Request update status event request status update result.
     *
     * @param userId   the user id
     * @param eventId  the event id
     * @param eventDto the event dto
     * @return the event request status update result
     */
    @PatchMapping("/{event-id}/requests")
    public EventRequestStatusUpdateResult requestUpdateStatus(@PathVariable(USERID) Long userId,
                                                              @PathVariable(EVENTID) Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest eventDto) {
        log.info("Private: patch request status {}", eventDto);
        return requestService.requestUpdateStatus(userId, eventId, eventDto);
    }
}
