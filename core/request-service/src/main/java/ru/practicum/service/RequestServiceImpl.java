package ru.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.EventClient;
import ru.practicum.client.UserClient;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Request;
import ru.practicum.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestServiceImpl implements RequestService {
    EventClient eventClient;
    UserClient userClient;
    RequestRepository requestRepository;
    RequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> getRequestByUserId(Long userId) {
        userClient.getUserById(userId);
        List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return requestList.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestByUserAndEvent(Long userId, Long eventId) {
        List<Request> requestList = requestRepository.findAllByEventId(eventId);
        return requestList.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        UserDto userDto = userClient.getUserById(userId);
        EventFullDto event = eventClient.getEventByIdForAdmin(eventId);
        requestToEventVerification(userDto, event);
        Request request = requestMapper.formUserAndEventToRequest(userDto, event);
        requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userClient.getUserById(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Object with id=" + requestId + " was not found!", "")
        );
        request.setStatus(RequestStatus.CANCELED);
        request = requestRepository.save(request);

        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult requestUpdateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<Request> requestList = requestRepository
                .findByIdInAndEventId(eventRequestStatusUpdateRequest.getRequestIds(), eventId);
        return requestUpdateVerification(eventId, requestList, eventRequestStatusUpdateRequest.getStatus());
    }

    @Override
    public long getRequestCountByStatusAndEventId(RequestStatus requestStatus, Long eventId) {
        return requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, eventId);
    }

    private void requestToEventVerification(UserDto user, EventFullDto event) {
        long userId = user.getId();
        long eventId = event.getId();

        if (requestRepository.findAllByRequesterId(userId).stream()
                .map(r -> r.getEventId().equals(eventId))
                .toList().contains(true)) {
            throw new ConflictException("User with id=" + userId +
                    " has already made a request for participation in the event with id=" + eventId, "");
        }
        if (event.getInitiator().getId() != null && userId == event.getInitiator().getId()) {
            throw new ConflictException("Initiator of event with id=" + userId +
                    " cannot add request for participation in his own event", "");
        }
        if (event.getPublishedOn() == null) {
            throw new ConflictException("", "You cannot participate in an unpublished event id=" + event.getId());
        }
        if (event.getParticipantLimit() != 0) {
            long countRequests = requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, event.getId());
            if (countRequests >= event.getParticipantLimit()) {
                throw new ConflictException("The event with id=" + event.getId() + " has reached the limit of participation requests", "");
            }
        }
    }

    private EventRequestStatusUpdateResult requestUpdateVerification(Long eventId, List<Request> requestList, RequestStatus status) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (Request request : requestList) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("You can only change the status of pending applications", "");
            }
            int count = requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, eventId);

            EventFullDto event = eventClient.getEventByIdForAdmin(request.getEventId());

            if (count >= event.getParticipantLimit()) {
                throw new ConflictException("The event with id=" + event.getId() +
                        " has reached the limit of participation requests", "");
            }
            if (event.getId().equals(eventId)) {
                request.setStatus(status);
                requestRepository.save(request);

                if (status == RequestStatus.CONFIRMED) {
                    confirmedRequests.add(requestMapper.toParticipationRequestDto(request));
                } else if (status == RequestStatus.REJECTED) {
                    rejectedRequests.add(requestMapper.toParticipationRequestDto(request));
                }
            }
        }
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);

        Integer count = requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, eventId);
        eventClient.setConfirmedRequests(eventId, count);

        return result;
    }
}
