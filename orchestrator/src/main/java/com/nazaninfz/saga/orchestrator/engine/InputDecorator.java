package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.exception.InputDecorationException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.model.CommandInputDecorator;
import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import com.nazaninfz.saga.orchestrator.utils.CommandActionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class InputDecorator {
    private final SagaCommandRepository commandRepository;

    public void decorate(
            CommandInputDecorator decorator,
            SagaCommandEntity commandEntity,
            Map<String, SagaCommandOutput> outputMap,
            Map<String, Object> contextMap
    ) {
        if (decorator == null) {
            infoLog("Empty decorator", commandEntity);
            CommandActionUtils.emptyDecorator(commandEntity);
            commandRepository.save(commandEntity);
            return;
        }
        infoLog("Started", commandEntity);
        CommandActionUtils.startDecoration(commandEntity);
        commandRepository.save(commandEntity);

        try {
            decorator.decorate(commandEntity.getInput(), outputMap, contextMap);
            CommandActionUtils.addSuccessDecorationStep(commandEntity, decorator);
            CommandActionUtils.successDecoration(commandEntity);
        } catch (Exception e) {
            errorLog("exception in decorating", commandEntity, e);
            CommandActionUtils.failDecoration(commandEntity, decorator, e);
            commandRepository.save(commandEntity);
            throw new InputDecorationException(e);
        }
    }

    private static void infoLog(String message, SagaCommandEntity command) {
        log.info("SAGA (ORCHESTRATOR) - COMMAND_DECORATOR - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                message, command.getCommandTitle(), command.getCommandId(), command.getSequenceId(),
                command.getOrderId());
    }

    private static void errorLog(String message, SagaCommandEntity command, Exception e) {
        log.error("SAGA (ORCHESTRATOR) - COMMAND_DECORATOR_EXCEPTION - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                message, command.getCommandTitle(), command.getCommandId(), command.getSequenceId(),
                command.getOrderId(), e);
    }
}
