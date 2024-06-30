package com.osayijoy.eventbooking.service.impl;

import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.Event;
import com.osayijoy.eventbooking.repository.EventRepository;
import com.osayijoy.eventbooking.service.EmailService;
import com.osayijoy.eventbooking.service.EventService;
import com.osayijoy.eventbooking.service.ReservationService;
import com.osayijoy.eventbooking.utils.BeanUtilWrapper;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;
    private final ReservationService reservationService;
    private final EmailService emailService;

    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDTO eventDto) {
        Event event = mapToEntity(eventDto);
        event = eventRepository.save(event);
        return mapToDto(event);
    }

    @Transactional
    public EventResponseDto updateEvent(Long id, EventRequestDTO eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));

        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setAvailableAttendeesCount(eventDto.getAvailableAttendeesCount());
        event.setDescription(eventDto.getDescription());
        event.setCategory(eventDto.getCategory());
        event = eventRepository.save(event);

        return mapToDto(event);
    }

    @Override
    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        return mapToDto(event);
    }

    @Override
    public PaginatedResponseDTO<EventResponseDto> searchEvents(String name, String startDate, String endDate, String category,
                                                               int page, int size) {
        Specification<Event> spec = buildSpecification(name, startDate, endDate, category);
        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventPage = eventRepository.findAll(spec, pageable);
        List<EventResponseDto> events = eventPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return PaginatedResponseDTO.<EventResponseDto>builder()
                .content(events)
                .currentPage(eventPage.getNumber())
                .totalPages(eventPage.getTotalPages())
                .totalItems(eventPage.getTotalElements())
                .isFirstPage(eventPage.isFirst())
                .isLastPage(eventPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        eventRepository.delete(event);
    }

    @Override
    public List<EventResponseDto> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);
        List<Event> upcomingEvents = eventRepository.findByDateBetween(now, next24Hours);
        return upcomingEvents.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void notifyUsersOfUpcomingEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        List<String> userEmails = reservationService.getUserEmailsForEvent(eventId);
        String subject = "Upcoming Event Notification";
        String message = "Dear user, the event " + event.getName() + " will start soon. Get ready!";

        for (String userEmail : userEmails) {
            emailService.sendEmail(userEmail, subject, message);
            log.info("Notification sent to {}", userEmail);
        }
    }

    private Specification<Event> buildSpecification(String name, String startDate, String endDate, String category) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (category != null && !category.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category"), category));
            }

            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("date"), start, end));
            } else if (startDate != null && !startDate.isEmpty()) {
                LocalDate start = LocalDate.parse(startDate);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("date"), start));
            } else if (endDate != null && !endDate.isEmpty()) {
                LocalDate end = LocalDate.parse(endDate);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("date"), end));
            }

            return predicate;
        };
    }


    private EventResponseDto mapToDto(Event event) {
        EventResponseDto eventResponseDto = new EventResponseDto();
        BeanUtilWrapper.copyNonNullProperties(event, eventResponseDto);
        return eventResponseDto;
    }
    private Event mapToEntity(EventRequestDTO eventRequestDTO) {
        Event event = new Event();
        BeanUtilWrapper.copyNonNullProperties(eventRequestDTO, event);
        return event;
    }
}
