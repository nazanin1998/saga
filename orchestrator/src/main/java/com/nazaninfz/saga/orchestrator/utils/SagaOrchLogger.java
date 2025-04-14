package com.nazaninfz.saga.orchestrator.utils;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SagaOrchLogger {

    public static void infoLog(SagaCommandStepType stepType, String message, SagaCommandEntity command) {
        log.info(stepType.name(), message, command);
    }

    public static void infoLog(String stepName, String message, SagaCommandEntity command) {
        log.info("SAGA (ORCHESTRATOR) - {} - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                stepName,
                message,
                command.getCommandName(),
                command.getCommandId(),
                command.getSequenceId(),
                command.getOrderId());
    }

    public static void errorLog(String stepName, String message, SagaCommandEntity command, Exception e) {
        log.error("SAGA (ORCHESTRATOR) - {}_EXCEPTION - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                stepName,
                message,
                command.getCommandName(),
                command.getCommandId(),
                command.getSequenceId(),
                command.getOrderId(), e);
    }

    public static void errorLog(SagaCommandStepType stepType, String message, SagaCommandEntity command, Exception e) {
        log.error(stepType.name(), message, command, e);
    }

    public static void errorLog(SagaCommandStepType stepType, String message) {
        errorLog(stepType.name(), message);
    }

    public static void errorLog(String stepType, String message) {
        log.error("SAGA (ORCHESTRATOR) - {}_EXCEPTION - {}", stepType, message);
    }

    public static void infoLog(String message, SagaSequenceEntity sequenceEntity) {
        log.info("SAGA (ORCHESTRATOR) - SEQUENCE - {}, sequence id: {}, title: {}",
                message, sequenceEntity.getSequenceId(), sequenceEntity.getSequenceTitle());
    }

    public static void errorLog(String message, SagaSequenceEntity sequenceEntity, Exception e) {
        log.error("SAGA (ORCHESTRATOR) - SEQUENCE_EXCEPTION - {}, sequence id: {}, title: {}",
                message, sequenceEntity.getSequenceId(), sequenceEntity.getSequenceTitle(), e);
    }
}
