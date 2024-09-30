package com.finalboss.service;

import com.finalboss.domain.YellowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YellowEventService {

    private final YellowEventRepository yellowEventRepository;

    public YellowEventService(YellowEventRepository yellowEventRepository) {
        this.yellowEventRepository = yellowEventRepository;
    }

    public List<YellowEvent> findAll() {
       return yellowEventRepository.findAll();
    }
    public Optional<YellowEvent> findById(String id) {
        return yellowEventRepository.findById(id);
    }
    public YellowEvent save(YellowEvent yellowEvent) {
        return yellowEventRepository.save(yellowEvent);
    }
    public void deleteById(String id) {
        yellowEventRepository.deleteById(id);
    }

}
