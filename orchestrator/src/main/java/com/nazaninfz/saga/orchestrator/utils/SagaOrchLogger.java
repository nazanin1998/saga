package com.nazaninfz.saga.orchestrator.utils;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SagaOrchLogger {

    public static void infoLog(SagaCommandStepType stepType, String message, SagaCommandEntity command) {
        log.info("SAGA (ORCHESTRATOR) - {} - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                stepType.name(),
                message,
                command.getCommandTitle(),
                command.getCommandId(),
                command.getSequenceId(),
                command.getOrderId());
    }

    public static void errorLog(SagaCommandStepType stepType, String message, SagaCommandEntity command, Exception e) {
        log.error("SAGA (ORCHESTRATOR) - {}_EXCEPTION - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                stepType.name(),
                message,
                command.getCommandTitle(),
                command.getCommandId(),
                command.getSequenceId(),
                command.getOrderId(), e);
    }

    public static void errorLog(SagaCommandStepType stepType, String message) {
        log.error("SAGA (ORCHESTRATOR) - {}_EXCEPTION - {}", stepType.name(), message);
    }
}
