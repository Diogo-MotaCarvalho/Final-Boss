package com.finalboss;

import com.finalboss.repository.YellowEventRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = YellowEventRepository.class)
public class FinalClassConsumer {

    public static void main(String[] args) {
        SpringApplication.run(FinalClassConsumer.class, args);}
}

