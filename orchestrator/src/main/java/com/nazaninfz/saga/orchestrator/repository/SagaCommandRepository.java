package com.nazaninfz.saga.orchestrator.repository;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SagaCommandRepository extends MongoRepository<SagaCommandEntity, String> {
}
