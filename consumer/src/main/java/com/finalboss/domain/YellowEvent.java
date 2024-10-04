package com.finalboss.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "yellow-events")
public record YellowEvent(
        @Id
        String id,
        String name,
        String date,
        List<Market> markets
) {
}
