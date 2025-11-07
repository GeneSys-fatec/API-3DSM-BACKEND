package com.servico_google.servico_google.model.converter;

import java.util.HashMap;
import java.util.Map;

import com.servico_google.servico_google.model.dto.google.CreateEventRequestDTO;
import com.servico_google.servico_google.model.dto.google.EventTimeDTO;
import com.servico_google.servico_google.model.dto.google.UpdateEventRequestDTO;

public final class GoogleEventConverter {

    private GoogleEventConverter() {}

    public static Map<String, Object> toCreateEventMap(CreateEventRequestDTO req) {
        Map<String, Object> eventBody = new HashMap<>();
        if (req == null) return eventBody;

        if (notBlank(req.getId())) eventBody.put("id", req.getId());
        if (req.getSummary() != null) eventBody.put("summary", req.getSummary());
        // description vazia por padrão para evitar null
        eventBody.put("description", req.getDescription() == null ? "" : req.getDescription());

        Map<String, Object> start = toEventTimeMap(req.getStart());
        Map<String, Object> end = toEventTimeMap(req.getEnd());
        if (!start.isEmpty()) eventBody.put("start", start);
        if (!end.isEmpty()) eventBody.put("end", end);

        return eventBody;
    }

    public static Map<String, Object> toUpdateEventMap(UpdateEventRequestDTO req) {
        Map<String, Object> eventBody = new HashMap<>();
        if (req == null) return eventBody;

        if (req.getSummary() != null) eventBody.put("summary", req.getSummary());
        if (req.getDescription() != null) eventBody.put("description", req.getDescription());

        if (req.getStart() != null) {
            Map<String, Object> start = toEventTimeMap(req.getStart());
            if (!start.isEmpty()) eventBody.put("start", start);
        }
        if (req.getEnd() != null) {
            Map<String, Object> end = toEventTimeMap(req.getEnd());
            if (!end.isEmpty()) eventBody.put("end", end);
        }

        return eventBody;
    }

    public static Map<String, Object> toEventTimeMap(EventTimeDTO t) {
        Map<String, Object> m = new HashMap<>();
        if (t == null) return m;

        if (notBlank(t.getDate())) {
            m.put("date", t.getDate());
        }
        if (notBlank(t.getDateTime())) {
            m.put("dateTime", t.getDateTime());
        }
        if (notBlank(t.getTimeZone())) {
            m.put("timeZone", t.getTimeZone());
        }
        return m;
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}