package com.servico_google.servico_google.model.dto.google;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTimeDTO {
    // Preencha apenas um: date (YYYY-MM-DD) OU dateTime (ISO 8601)
    private String date;
    private String dateTime;
    private String timeZone; // opcional

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        if (date != null && !date.isBlank()) {
            m.put("date", date);
        }
        if (dateTime != null && !dateTime.isBlank()) {
            m.put("dateTime", dateTime);
        }
        if (timeZone != null && !timeZone.isBlank()) {
            m.put("timeZone", timeZone);
        }
        return m;
    }
}