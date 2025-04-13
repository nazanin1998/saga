package com.nazaninfz.saga.orchestrator.utils;

import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepStatus;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import com.nazaninfz.saga.orchestrator.core.model.CommandHelperIdentifier;
import com.nazaninfz.saga.orchestrator.core.model.SagaCommandStepHistory;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.exception.ExceptionUtils;

@UtilityClass
public class SagaCommandStepUtils {

    public SagaCommandStepHistory success(CommandHelperIdentifier identifier, SagaCommandStepType type) {
        return from(identifier, type)
                .setStepStatus(SagaCommandStepStatus.SUCCEED);
    }

    public SagaCommandStepHistory skip(CommandHelperIdentifier identifier, SagaCommandStepType type) {
        return from(identifier, type)
                .setStepStatus(SagaCommandStepStatus.SKIPPED);
    }

    public SagaCommandStepHistory failure(CommandHelperIdentifier identifier, SagaCommandStepType type) {
        return from(identifier, type)
                .setStepStatus(SagaCommandStepStatus.FAILED);
    }

    public SagaCommandStepHistory failure(CommandHelperIdentifier identifier, SagaCommandStepType type, Throwable e) {
        return failure(identifier, type)
                .setDetails(ExceptionUtils.getStackTrace(e));
    }

    private SagaCommandStepHistory from(CommandHelperIdentifier identifier, SagaCommandStepType type) {
        return (SagaCommandStepHistory) new SagaCommandStepHistory()
                .setStepType(type)
                .setId(identifier.getId())
                .setTitle(identifier.getTitle());
    }
}
