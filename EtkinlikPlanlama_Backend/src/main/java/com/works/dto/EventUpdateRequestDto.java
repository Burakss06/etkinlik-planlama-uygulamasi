package com.works.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EventUpdateRequestDto {
    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @NotEmpty
    private String title;

    @NotNull
    @Size(min = 2, max = 500)
    @NotEmpty
    private String description;

    @NotNull
    @Size(min = 2, max = 100)
    @NotEmpty
    private String eventDate;

    @NotNull
    @NotEmpty
    private String eventTime;

    @NotNull
    @Size(min = 2, max = 200)
    @NotEmpty
    private String location;

    @NotNull
    @Size(min = 2, max = 50)
    @NotEmpty
    private String category;
}
