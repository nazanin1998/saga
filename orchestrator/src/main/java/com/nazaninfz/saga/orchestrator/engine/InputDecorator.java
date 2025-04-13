package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import com.nazaninfz.saga.orchestrator.core.exception.InputDecorationException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.model.CommandInputDecorator;
import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import com.nazaninfz.saga.orchestrator.utils.CommandActionUtils;
import com.nazaninfz.saga.orchestrator.utils.SagaOrchLogger;
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
            SagaOrchLogger.infoLog(SagaCommandStepType.DECORATOR, "Empty decorator", commandEntity);
            CommandActionUtils.emptyDecorator(commandEntity);
            commandRepository.save(commandEntity);
            return;
        }
        SagaOrchLogger.infoLog(SagaCommandStepType.DECORATOR, "Started", commandEntity);
        CommandActionUtils.startDecoration(commandEntity);
        commandRepository.save(commandEntity);

        try {
            decorator.decorate(commandEntity.getInput(), outputMap, contextMap);
            CommandActionUtils.successDecoration(commandEntity, decorator);
            commandRepository.save(commandEntity);
        } catch (Exception e) {
            SagaOrchLogger.errorLog(SagaCommandStepType.DECORATOR, "exception in decorating", commandEntity, e);
            CommandActionUtils.failDecoration(commandEntity, decorator, e);
            commandRepository.save(commandEntity);
            throw new InputDecorationException(e);
        }
    }
}
