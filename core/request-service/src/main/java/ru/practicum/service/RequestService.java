package ru.practicum.service;

import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.enums.RequestStatus;

import java.util.List;

public interface RequestService {
    /**
     * Gets request by user id.
     *
     * @param userId the user id
     * @return the request by user id
     */
    List<ParticipationRequestDto> getRequestByUserId(Long userId);

    /**
     * Gets request by user and event.
     *
     * @param userId  the user id
     * @param eventId the event id
     * @return the request by user and event
     */
    List<ParticipationRequestDto> getRequestByUserAndEvent(Long userId, Long eventId);

    /**
     * Create request participation request dto.
     *
     * @param userId  the user id
     * @param eventId the event id
     * @return the participation request dto
     */
    ParticipationRequestDto createRequest(Long userId, Long eventId);

    /**
     * Cancel request participation request dto.
     *
     * @param userId    the user id
     * @param requestId the request id
     * @return the participation request dto
     */
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    /**
     * Request update status event request status update result.
     *
     * @param userId                          the user id
     * @param eventId                         the event id
     * @param eventRequestStatusUpdateRequest the event request status update request
     * @return the event request status update result
     */
    EventRequestStatusUpdateResult requestUpdateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    /** Count requests on required status
     *
     * @param requestStatus the status to count
     * @param eventId       the evet id
     * @return result of count
     */
    long getRequestCountByStatusAndEventId(RequestStatus requestStatus, Long eventId);
}
