package com.finalboss.domain;

import java.util.List;

public record MarketUpdate(String id,
        String name,
        Operation operation,
        Event event,
        List<Selection> selections) {}



