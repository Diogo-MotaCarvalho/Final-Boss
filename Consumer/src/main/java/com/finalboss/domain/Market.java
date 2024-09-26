package com.finalboss.domain;

import java.util.List;

public record Market(
        int id,
        String name,
        List<Selection> selections
) {}
