package ru.practicum.service.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Event service.
 */
public interface EventService {
    /**
     * Gets events for user.
     *
     * @param userId the user id
     * @param from   the from
     * @param size   the size
     * @return the events for user
     */
    List<EventShortDto> getEventsForUser(Long userId, Integer from, Integer size);

    /**
     * Create event event full dto.
     *
     * @param userId   the user id
     * @param eventDto the event dto
     * @return the event full dto
     */
    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    /**
     * Gets event by id for user.
     *
     * @param userId  the user id
     * @param eventId the event id
     * @return the event by id for user
     */
    EventFullDto getEventByIdForUser(Long userId, Long eventId);

    /**
     * Change event event full dto.
     *
     * @param userId   the user id
     * @param eventId  the event id
     * @param eventDto the event dto
     * @return the event full dto
     */
    EventFullDto changeEvent(Long userId, Long eventId, UpdateEventAdminRequest eventDto);

    /**
     * Gets events for admin.
     *
     * @param users      the users
     * @param states     the states
     * @param categories the categories
     * @param rangeStart the range start
     * @param rangeEnd   the range end
     * @param from       the from
     * @param size       the size
     * @return the events for admin
     */
    List<EventFullDto> getEventsForAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Integer from, Integer size);

    /**
     * Update event by admin event full dto.
     *
     * @param updateEventAdminRequest the update event admin request
     * @param eventId                 the event id
     * @return the event full dto
     */
    EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

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
     * @param clientIp      the client ip
     * @return the events
     */
    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                  Boolean onlyAvailable, String sort, int from, int size, String clientIp);

    /**
     * Gets event by id.
     *
     * @param userId  id of the user
     * @param eventId id of the event
     * @return the event by id
     */
    EventFullDto getEventById(Long userId, Long eventId);

    /**
     * Gets event by id for admin.
     *
     * @param id the id
     * @return the event full dto by id
     */
    EventFullDto getEventByIdForAdmin(Long id);

    /**
     * Set confirmed requests for an event.
     *
     * @param eventId the id of event
     * @param count   count value to set
     */
    void setConfirmedRequests(Long eventId, Integer count);

    /**
     * Get the recommendations to use.
     *
     * @param userId     id of the user.
     * @param maxResults not used.
     * @return the list of recommended events.
     */
    List<EventShortDto> getRecommendations(Long userId, Integer maxResults);

    /**
     * Setting the like for an event
     *
     * @param userId  id of the user, who likes the event.
     * @param eventId the event liked.
     */
    void setLikeEvent(Long userId, Long eventId);

    /**
     * Get the interactions count for the list of events.
     *
     * @param eventIds the list of events to get for.
     */
    void getInteractionsCount(List<Long> eventIds);
}

