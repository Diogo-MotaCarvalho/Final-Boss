package com.finalboss.domain;

import java.util.List;

public record YellowEvent(
         String id,
         String name,
         String date,
         List<Market> markets
) {}
