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
}
