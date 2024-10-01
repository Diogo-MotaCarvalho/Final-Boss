package com.finalboss.service;

import com.finalboss.domain.YellowEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YellowEventRepository {

    private final YellowEventRepositoryI yellowEventRepositoryI;

    public YellowEventRepository(YellowEventRepositoryI yellowEventRepositoryI) {
        this.yellowEventRepositoryI = yellowEventRepositoryI;
    }

    public List<YellowEvent> findAll() {
       return yellowEventRepositoryI.findAll();
    }
    public Optional<YellowEvent> findById(String id) {
        return yellowEventRepositoryI.findById(id);
    }
    public YellowEvent save(YellowEvent yellowEvent) {
        return yellowEventRepositoryI.save(yellowEvent);
    }
    public void deleteById(String id) {
        yellowEventRepositoryI.deleteById(id);
    }

}
