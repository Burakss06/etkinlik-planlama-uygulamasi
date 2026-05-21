package com.works.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventSaveRequestDto {
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
    @NotEmpty
    private String location;

    @NotNull
    @Size(min = 2, max = 50)
    @NotEmpty
    private String category;
}
