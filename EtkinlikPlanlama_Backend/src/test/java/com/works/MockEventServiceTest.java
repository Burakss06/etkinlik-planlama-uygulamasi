package com.works;

import com.works.dto.EventSaveRequestDto;
import com.works.dto.EventUpdateRequestDto;
import com.works.dto.UserResponseDto;
import com.works.entity.Event;
import com.works.entity.User;
import com.works.repository.EventRepository;
import com.works.repository.UserRepository;
import com.works.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockEventServiceTest {

    @Mock
    EventRepository eventRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper model;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpSession session;

    @InjectMocks
    EventService eventService;

    @Test
    void saveEvent_success() {
        EventSaveRequestDto requestDto = new EventSaveRequestDto();
        requestDto.setTitle("Test Event");
        requestDto.setDescription("Test Description");
        requestDto.setEventDate("2026-12-31");

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");

        User owner = new User();
        owner.setId(1L);
        owner.setEmail("test@test.com");

        Event mappedEvent = new Event();
        mappedEvent.setTitle("Test Event");
        mappedEvent.setDescription("Test Description");
        mappedEvent.setEventDate("2026-12-31");

        Event savedEvent = new Event();
        savedEvent.setId(1L);
        savedEvent.setTitle("Test Event");
        savedEvent.setDescription("Test Description");
        savedEvent.setEventDate("2026-12-31");
        savedEvent.setOwner(owner);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(model.map(any(EventSaveRequestDto.class), eq(Event.class))).thenReturn(mappedEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        ResponseEntity<?> response = eventService.save(requestDto);
        assertEquals(200, response.getStatusCode().value());
        Event result = (Event) response.getBody();
        assert result.getId() == 1L;
        assert result.getTitle().equals("Test Event");
        assert result.getOwner().getId() == 1L;

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void saveEvent_throwsException_whenUserNotLoggedIn() {
        EventSaveRequestDto requestDto = new EventSaveRequestDto();
        requestDto.setEventDate("2026-12-31");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<?> response = eventService.save(requestDto);
        assertEquals(401, response.getStatusCode().value());

        verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    void listEvent_success() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Test Event 1");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Test Event 2");

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(event1, event2));

        when(eventRepository.findAll(any(Pageable.class))).thenReturn(eventPage);

        Page<Event> result = eventService.eventList(0);

        assert result.getContent().size() == 2;
        assert result.getContent().get(0).getId() == 1L;
        assert result.getContent().get(1).getId() == 2L;

        verify(eventRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateEvent_success() {
        EventUpdateRequestDto updateDto = new EventUpdateRequestDto();
        updateDto.setId(1L);
        updateDto.setTitle("Updated Event");
        updateDto.setDescription("Updated Description");
        updateDto.setEventDate("2026-12-31");

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1L);

        User owner = new User();
        owner.setId(1L);

        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setTitle("Old Event");
        existingEvent.setOwner(owner);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);

        ResponseEntity<?> result = eventService.update(updateDto);

        assertEquals(200, result.getStatusCode().value());
        assert ((Map<String, Object>) result.getBody()).get("success").equals(true);

        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void updateEvent_throwsException_whenNotFound() {
        EventUpdateRequestDto updateDto = new EventUpdateRequestDto();
        updateDto.setId(99L);
        updateDto.setEventDate("2026-12-31");

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1L);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userDto);
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> result = eventService.update(updateDto);
        assertEquals(404, result.getStatusCode().value());

        verify(eventRepository, times(1)).findById(99L);
        verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    void searchEvent_success() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Party Event");

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(event));

        when(eventRepository.findByTitleContainsOrDescriptionContainsAllIgnoreCase(eq("Party"), eq("Party"), any(Pageable.class)))
                .thenReturn(eventPage);

        Page<Event> result = eventService.search("Party", 0);

        assert result.getTotalElements() == 1;
        assert result.getContent().get(0).getTitle().equals("Party Event");

        verify(eventRepository, times(1))
                .findByTitleContainsOrDescriptionContainsAllIgnoreCase(eq("Party"), eq("Party"), any(Pageable.class));
    }
}
