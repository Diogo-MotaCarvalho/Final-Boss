package com.finalboss.domain;

import java.util.List;

public record Market(
        String id,
        String name,
        List<Selection> selections
) {}
