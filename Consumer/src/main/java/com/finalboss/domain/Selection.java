package com.finalboss.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "selections")
public record Selection(
        @Id
        int id,
        String name,
        float odd
) {}
