package com.works.entity;

import com.works.util.EStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String eventDate;

    private String eventTime;

    @Column(length = 200)
    private String location;

    @Column(length = 50)
    private String category;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @ManyToOne
    private User owner;

    @ManyToMany
    private java.util.List<User> participants;
}
