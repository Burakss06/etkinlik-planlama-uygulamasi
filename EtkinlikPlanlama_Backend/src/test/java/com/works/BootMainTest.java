package com.works;

import com.works.entity.Event;
import com.works.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
public class BootMainTest {

    @Autowired
    EventService eventService;

    @Test
    void contextLoads() {
        Page<Event> eventPage = eventService.eventList(0);
        Assertions.assertNotNull(eventPage, "Event page should not be null");
        Assertions.assertNotNull(eventPage.getContent(), "Event content should not be null");
    }

}
