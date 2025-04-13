package com.nazaninfz.saga.orchestrator.utils;

import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import com.nazaninfz.saga.orchestrator.core.enums.SequenceStatus;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.UUID;

@UtilityClass
public class SequenceActionUtils {

    public void init(SagaSequenceEntity sequence) {
        sequence.setSequenceId(UUID.randomUUID().toString())
                .setSequenceStatus(SequenceStatus.INITIALIZED)
                .setExecutedCommandIds(new ArrayList<>());
    }

    public void success(SagaSequenceEntity sequence) {
        sequence.setSequenceStatus(SequenceStatus.SUCCEED);
    }

    public void unknown(SagaSequenceEntity sequence) {
        sequence.setSequenceStatus(SequenceStatus.UNKNOWN);
    }
}
