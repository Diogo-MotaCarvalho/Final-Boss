package com.finalboss.repository;

import com.finalboss.domain.YellowEvent;
import com.finalboss.useCases.YellowEventRepositoryI;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class YellowEventRepository implements YellowEventRepositoryI {

    private final MongoRepository<YellowEvent, String> repo;

    public YellowEventRepository(MongoRepository<YellowEvent, String> repo) {
        this.repo = repo;
    }

    public Optional<YellowEvent> findById(String id) {
        return repo.findById(id);
    }

    public boolean existsById(String id) {
        return repo.existsById(id);
    }

    public void save(YellowEvent yellowEvent) {
        repo.save(yellowEvent);
    }


}
