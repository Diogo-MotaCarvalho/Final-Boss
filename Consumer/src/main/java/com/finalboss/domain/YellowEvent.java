package com.finalboss.domain;

import java.util.List;

public record YellowEvent(
         int id,
         String name,
         List<Market> markets
) {}
