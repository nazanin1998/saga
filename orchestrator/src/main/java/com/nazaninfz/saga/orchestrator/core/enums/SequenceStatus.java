package com.nazaninfz.saga.orchestrator.core.enums;

public enum SequenceStatus {
    INITIALIZED,
    IN_PROGRESS,
    SUCCEED,
    ROLL_BACKED_SUCCESSFULLY,
    UNSUCCESSFUL_ROLL_BACK,
    UNKNOWN
}
