package com.finalboss.useCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalboss.domain.MarketUpdate;

public interface Handler {
    void readOperation(MarketUpdate update) throws JsonProcessingException;
}
