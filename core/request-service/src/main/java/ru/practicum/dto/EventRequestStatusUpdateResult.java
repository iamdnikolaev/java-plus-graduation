package ru.practicum.dto;

import lombok.Data;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

/**
 * The type Event request status update result.
 */
@Data
public class EventRequestStatusUpdateResult {
    /**
     * The Confirmed requests.
     */
    List<ParticipationRequestDto> confirmedRequests;
    /**
     * The Rejected requests.
     */
    List<ParticipationRequestDto> rejectedRequests;
}
