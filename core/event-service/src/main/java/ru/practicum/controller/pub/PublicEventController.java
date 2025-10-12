package ru.practicum.controller.pub;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Public event controller.
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    /**
     * Gets events.
     *
     * @param text          the text
     * @param categories    the categories
     * @param paid          the paid
     * @param rangeStart    the range start
     * @param rangeEnd      the range end
     * @param onlyAvailable the only available
     * @param sort          the sort
     * @param from          the from
     * @param size          the size
     * @param request       the request
     * @return the events
     */
    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        // Валидация диапазона дат
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Дата начала rangeStart не может быть позже даты окончания rangeEnd", "");
        }

        // Валидация параметров пагинации
        if (from < 0 || size <= 0) {
            throw new ValidationException("Параметры пагинации 'from' и 'size' должны быть >= 0 и > 0 соответственно", "");
        }

        String clientIp = request.getRemoteAddr();

        log.info("==> GET /events: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, clientIp);
    }

    /**
     * Gets event by id.
     *
     * @param userId  the user
     * @param eventId the event id
     * @return the event by id
     */
    @GetMapping("/{event-id}")
    public EventFullDto getEventById(@RequestHeader("X-EWM-USER-ID") Long userId, @PathVariable("event-id") Long eventId) {
        log.info("Получение информации о событии с id={}", eventId);

        return eventService.getEventById(userId, eventId);
    }

    /**
     * Get the recommendations to use.
     *
     * @param userId     id of the user.
     * @param maxResults not used.
     * @return the list of recommended events.
     */
    @GetMapping("/recommendations")
    public List<EventShortDto> getRecommendations(@RequestHeader("X-EWM-USER-ID") Long userId, @RequestParam Integer maxResults) {
        return eventService.getRecommendations(userId, maxResults);
    }

    /**
     * Setting the like for an event
     *
     * @param userId  id of the user, who likes the event.
     * @param eventId the event liked.
     */
    @PutMapping("/{eventId}/like")
    public void setLikeEvent(@RequestHeader("X-EWM-USER-ID") Long userId, @PathVariable Long eventId) {
        eventService.setLikeEvent(userId, eventId);
    }

    /**
     * Get the interactions count for the list of events.
     *
     * @param eventIds the list of events to get for.
     */
    @GetMapping("/interactionsCount")
    public void getInteractionsCount(@RequestParam List<Long> eventIds) {
        eventService.getInteractionsCount(eventIds);
    }
}
