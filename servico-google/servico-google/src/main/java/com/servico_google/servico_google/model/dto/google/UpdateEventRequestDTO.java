package com.servico_google.servico_google.model.dto.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequestDTO {
    private String summary;
    private String description;
    private EventTimeDTO start;
    private EventTimeDTO end;
}