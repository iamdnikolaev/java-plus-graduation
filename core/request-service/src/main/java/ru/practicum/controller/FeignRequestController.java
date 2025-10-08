package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.enums.RequestStatus;
import ru.practicum.service.RequestService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/requests")
public class FeignRequestController {

    private final RequestService requestService;

    @GetMapping("/count")
    public Long getRequestCountByStatusAndEventId(@RequestParam RequestStatus requestStatus, @RequestParam Long eventId) {
        log.info("getRequestCountByStatusAndEventId requestStatus = {}, eventId = {}", requestStatus, eventId);
        long result = requestService.getRequestCountByStatusAndEventId(requestStatus, eventId);
        log.info("getRequestCountByStatusAndEventId result = {}", result);

        return result;
    }
}
