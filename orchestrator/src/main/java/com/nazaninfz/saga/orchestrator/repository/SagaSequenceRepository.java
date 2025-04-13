package com.nazaninfz.saga.orchestrator.repository;

import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SagaSequenceRepository extends MongoRepository<SagaSequenceEntity, String> {
}
