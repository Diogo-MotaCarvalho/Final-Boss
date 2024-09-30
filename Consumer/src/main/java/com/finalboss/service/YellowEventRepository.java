package com.finalboss.service;

import com.finalboss.domain.YellowEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YellowEventRepository extends MongoRepository<YellowEvent,String> {

}
