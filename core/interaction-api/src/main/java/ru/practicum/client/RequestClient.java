package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.enums.RequestStatus;

@FeignClient(name = "request-service", path = "/users/requests")
public interface RequestClient {

    @GetMapping("/count")
    long getRequestCountByStatusAndEventId(@RequestParam RequestStatus requestStatus, @RequestParam Long eventId);

}
