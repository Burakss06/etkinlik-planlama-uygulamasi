package com.works.service;

import com.works.dto.EventSaveRequestDto;
import com.works.dto.EventUpdateRequestDto;
import com.works.dto.UserResponseDto;
import com.works.entity.User;
import com.works.entity.Event;
import com.works.repository.UserRepository;
import com.works.repository.EventRepository;
import com.works.util.EStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    final EventRepository eventRepository;
    final UserRepository userRepository;
    final ModelMapper model;
    final HttpServletRequest request;

    @CacheEvict(cacheNames = "eventListCache", allEntries = true)
    public ResponseEntity<?> save(EventSaveRequestDto eventSaveRequestDto) {
        UserResponseDto userDto = (UserResponseDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Bu işlem için giriş yapmalısınız."));
        }

        try {
            LocalDate date = LocalDate.parse(eventSaveRequestDto.getEventDate());
            if (date.isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Etkinlik tarihi geçmiş bir tarih olamaz."));
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Geçersiz tarih formatı (yyyy-MM-dd olmalıdır)."));
        }

        Optional<User> optionalOwner = userRepository.findById(userDto.getId());
        if (optionalOwner.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Kullanıcı bulunamadı."));
        }
        User owner = optionalOwner.get();

        Event event = model.map(eventSaveRequestDto, Event.class);
        event.setOwner(owner);
        event.setStatus(EStatus.YAYINDA);

        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    @CacheEvict(cacheNames = "eventListCache", allEntries = true)
    public ResponseEntity<?> deleteOne(Long id) {
        UserResponseDto userDto = (UserResponseDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Bu işlem için giriş yapmalısınız."));
        }

        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Etkinlik bulunamadı id: " + id));
        }
        Event event = optionalEvent.get();

        if (!event.getOwner().getId().equals(userDto.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", "Bu etkinliği silmeye yetkiniz yok."));
        }

        eventRepository.deleteById(id);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Etkinlik başarıyla silindi."));
    }

    @CacheEvict(cacheNames = "eventListCache", allEntries = true)
    public ResponseEntity<?> update(EventUpdateRequestDto eventUpdateRequestDto) {
        UserResponseDto userDto = (UserResponseDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Bu işlem için giriş yapmalısınız."));
        }

        try {
            LocalDate date = LocalDate.parse(eventUpdateRequestDto.getEventDate());
            if (date.isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Etkinlik tarihi geçmiş bir tarih olamaz."));
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Geçersiz tarih formatı (yyyy-MM-dd olmalıdır)."));
        }

        Optional<Event> optionalEvent = eventRepository.findById(eventUpdateRequestDto.getId());
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Etkinlik bulunamadı id: " + eventUpdateRequestDto.getId()));
        }
        Event event = optionalEvent.get();

        if (!event.getOwner().getId().equals(userDto.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", "Bu etkinliği güncellemeye yetkiniz yok."));
        }

        event.setTitle(eventUpdateRequestDto.getTitle());
        event.setDescription(eventUpdateRequestDto.getDescription());
        event.setEventDate(eventUpdateRequestDto.getEventDate());
        event.setEventTime(eventUpdateRequestDto.getEventTime());
        event.setLocation(eventUpdateRequestDto.getLocation());
        event.setCategory(eventUpdateRequestDto.getCategory());

        eventRepository.save(event);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Etkinlik başarıyla güncellendi."));
    }

    @Cacheable(value = "eventListCache", key = "#page")
    public Page<Event> eventList(int page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        return eventRepository.findAll(pageable);
    }

    public Page<Event> search(String q, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return eventRepository.findByTitleContainsOrDescriptionContainsAllIgnoreCase(q, q, pageable);
    }

    public ResponseEntity<?> getOne(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            return ResponseEntity.ok(optionalEvent.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Etkinlik bulunamadı id: " + id));
    }

    public ResponseEntity<?> myEventList(int page) {
        UserResponseDto userDto = (UserResponseDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Lütfen giriş yapınız."));
        }
        Pageable pageable = PageRequest.of(page, 10);
        Page<Event> eventPage = eventRepository.findByOwner_Id(userDto.getId(), pageable);
        return ResponseEntity.ok(eventPage);
    }

    @CacheEvict(cacheNames = "eventListCache", allEntries = true)
    public ResponseEntity<?> changeStatus(Long id, EStatus newStatus) {
        UserResponseDto userDto = (UserResponseDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Bu işlem için giriş yapmalısınız."));
        }

        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Etkinlik bulunamadı id: " + id));
        }
        Event event = optionalEvent.get();

        if (!event.getOwner().getId().equals(userDto.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", "Bu etkinliğin durumunu değiştirmeye yetkiniz yok."));
        }

        event.setStatus(newStatus);
        eventRepository.save(event);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Etkinlik durumu başarıyla güncellendi: " + newStatus));
    }

    @CacheEvict(cacheNames = "eventListCache", allEntries = true)
    public ResponseEntity<?> joinEvent(Long eventId) {
        UserResponseDto userDto = (UserResponseDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Bu işlem için giriş yapmalısınız."));
        }

        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Etkinlik bulunamadı id: " + eventId));
        }
        Event event = optionalEvent.get();

        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Kullanıcı bulunamadı."));
        }
        User user = optionalUser.get();

        if (event.getParticipants().contains(user)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Zaten bu etkinliğe katılmışsınız."));
        }

        event.getParticipants().add(user);
        eventRepository.save(event);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Etkinliğe başarıyla katıldınız."));
    }

    @CacheEvict(cacheNames = "eventListCache", allEntries = true)
    public ResponseEntity<?> leaveEvent(Long eventId) {
        UserResponseDto userDto = (UserResponseDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Bu işlem için giriş yapmalısınız."));
        }

        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Etkinlik bulunamadı id: " + eventId));
        }
        Event event = optionalEvent.get();

        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Kullanıcı bulunamadı."));
        }
        User user = optionalUser.get();

        if (!event.getParticipants().contains(user)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Bu etkinliğin katılımcısı değilsiniz."));
        }

        event.getParticipants().remove(user);
        eventRepository.save(event);
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Etkinlikten başarıyla ayrıldınız."));
    }

    public ResponseEntity<?> getParticipants(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Etkinlik bulunamadı id: " + eventId));
        }
        Event event = optionalEvent.get();
        return ResponseEntity.ok().body(event.getParticipants());
    }
}
