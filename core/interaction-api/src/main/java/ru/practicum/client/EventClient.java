package ru.practicum.client;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.event.EventFullDto;

@FeignClient(name = "event-service", path = "/admin/events")
public interface EventClient {
    @GetMapping("/{eventId}")
    EventFullDto getEventByIdForAdmin(@PathVariable @NotNull @Positive Long eventId);

    @PostMapping("/confirm")
     void setConfirmedRequests(@RequestParam(required = false, defaultValue = "0") Long eventId,
                               @RequestParam(required = false, defaultValue = "0") Integer count);
}
