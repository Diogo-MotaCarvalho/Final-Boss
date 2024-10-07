package com.finalboss.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "market")
public record Market(
        @Id //TODO remove dependencies from domain
        String id,
        String name,
        List<Selection> selections) {
}
