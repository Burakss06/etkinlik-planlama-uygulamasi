package com.works.controller;

import com.works.dto.EventSaveRequestDto;
import com.works.dto.EventUpdateRequestDto;
import com.works.entity.Event;
import com.works.service.EventService;
import com.works.util.EStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("event")
@RequiredArgsConstructor
public class EventRestController {
    final EventService eventService;

    @PostMapping("save")
    public ResponseEntity<?> save(@Valid @RequestBody EventSaveRequestDto eventSaveRequestDto) {
        return eventService.save(eventSaveRequestDto);
    }

    @DeleteMapping("deleteOne/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        return eventService.deleteOne(id);
    }

    @PutMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return eventService.update(eventUpdateRequestDto);
    }

    @GetMapping("list")
    public Page<Event> eventList(@RequestParam(defaultValue = "0") int page) {
        return eventService.eventList(page);
    }

    @GetMapping("search")
    public Page<Event> search(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String q) {
        return eventService.search(q, page);
    }

    @GetMapping("getOne/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return eventService.getOne(id);
    }

    @GetMapping("myList")
    public ResponseEntity<?> myEventList(@RequestParam(defaultValue = "0") int page) {
        return eventService.myEventList(page);
    }

    @PutMapping("changeStatus/{id}/{newStatus}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @PathVariable EStatus newStatus) {
        return eventService.changeStatus(id, newStatus);
    }

    @PutMapping("join/{eventId}")
    public ResponseEntity<?> joinEvent(@PathVariable Long eventId) {
        return eventService.joinEvent(eventId);
    }

    @PutMapping("leave/{eventId}")
    public ResponseEntity<?> leaveEvent(@PathVariable Long eventId) {
        return eventService.leaveEvent(eventId);
    }

    @GetMapping("participants/{eventId}")
    public ResponseEntity<?> getParticipants(@PathVariable Long eventId) {
        return eventService.getParticipants(eventId);
    }
}
