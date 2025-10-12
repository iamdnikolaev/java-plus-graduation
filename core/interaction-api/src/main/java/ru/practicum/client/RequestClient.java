package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.RequestStatus;

import java.util.List;

@FeignClient(name = "request-service")
public interface RequestClient {

    @GetMapping("/users/requests/count")
    long getRequestCountByStatusAndEventId(@RequestParam RequestStatus requestStatus, @RequestParam Long eventId);

    @GetMapping("users/{user-id}/events/{event-id}/requests")
    List<ParticipationRequestDto> getRequestByUserAndEvent(@PathVariable("user-id") Long userId,
                                                           @PathVariable("event-id") Long eventId);
}
