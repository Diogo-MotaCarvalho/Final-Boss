package com.finalboss.repository;

import com.finalboss.domain.YellowEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRepo extends MongoRepository<YellowEvent, String> {
}
