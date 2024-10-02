package com.finalboss.useCases;

import com.finalboss.domain.YellowEvent;

import java.util.Optional;

public interface YellowEventRepositoryI {
    Optional<YellowEvent> findById(String id);
    boolean existsById(String id);
    void save(YellowEvent yellowEvent);
}
