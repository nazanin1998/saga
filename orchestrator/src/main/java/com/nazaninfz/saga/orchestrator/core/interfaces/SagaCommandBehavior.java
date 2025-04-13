package com.nazaninfz.saga.orchestrator.core.interfaces;

import java.util.UUID;

public interface SagaCommandBehavior {
    void commit();
    void rollback();
    default String generateId() {
        return UUID.randomUUID().toString();
    }
}
