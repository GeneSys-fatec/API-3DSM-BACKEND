package com.servico_google.servico_google.model.dto.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestDTO {
    private String code;
    private String redirectUri;
}