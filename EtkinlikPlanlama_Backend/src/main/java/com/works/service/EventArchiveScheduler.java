package com.works.service;

import com.works.entity.Event;
import com.works.repository.EventRepository;
import com.works.util.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventArchiveScheduler {

    final EventRepository eventRepository;

    @Scheduled(fixedRate = 3600000)
    public void archivePastEvents() {
        List<Event> activeEvents = eventRepository.findAll();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Event event : activeEvents) {
            if (event.getStatus() == EStatus.YAYINDA) {
                try {
                    LocalDate eventDate = LocalDate.parse(event.getEventDate());
                    if (eventDate.isBefore(today)) {
                        event.setStatus(EStatus.ARSIVLENDI);
                        eventRepository.save(event);
                        System.out.println("Etkinlik otomatik olarak arşivlendi: " + event.getTitle());
                    }
                } catch (Exception e) {

                }
            }
        }
    }
}
