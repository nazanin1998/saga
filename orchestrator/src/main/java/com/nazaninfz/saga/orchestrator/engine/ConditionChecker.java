package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.exception.ConditionCheckException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.model.CommandExecutionCondition;
import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import com.nazaninfz.saga.orchestrator.utils.CommandActionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConditionChecker {
    private final SagaCommandRepository commandRepository;

    public boolean areAllConditionsSatisfied(
            List<CommandExecutionCondition> conditions,
            SagaCommandEntity commandEntity,
            Map<String, SagaCommandOutput> outputMap,
            Map<String, Object> contextMap
    ) {

        if (CollectionUtils.isEmpty(conditions)) {
            infoLog("Empty conditions", commandEntity);
            CommandActionUtils.emptyConditions(commandEntity);
            commandRepository.save(commandEntity);
            return true;
        }

        infoLog("Started", commandEntity);
        CommandActionUtils.startCheckingConditions(commandEntity);
        commandRepository.save(commandEntity);

        for (CommandExecutionCondition condition : conditions) {

            boolean isSatisfied = isConditionSatisfied(condition, commandEntity, outputMap, contextMap);
            if (!isSatisfied) {
                CommandActionUtils.failCheckingConditions(commandEntity, condition);
                commandRepository.save(commandEntity);
                infoLog("Unsatisfied", commandEntity);
                return false;
            }
            CommandActionUtils.addSuccessCheckingConditionsStep(commandEntity, condition);
        }

        CommandActionUtils.successCheckingConditions(commandEntity);
        commandRepository.save(commandEntity);
        infoLog("Satisfied", commandEntity);
        return true;
    }

    private boolean isConditionSatisfied(
            CommandExecutionCondition condition,
            SagaCommandEntity commandEntity,
            Map<String, SagaCommandOutput> outputMap,
            Map<String, Object> contextMap
    ) {
        try {
            return condition.isSatisfied(commandEntity.getInput(), outputMap, contextMap);
        } catch (Exception e) {
            errorLog("satisfy method exception", commandEntity, e);
            CommandActionUtils.failCheckingConditions(commandEntity, condition, e);
            commandRepository.save(commandEntity);
            throw new ConditionCheckException(e);
        }
    }


    private static void infoLog(String message, SagaCommandEntity command) {
        log.info("SAGA (ORCHESTRATOR) - COMMAND_CONDITION - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                message, command.getCommandTitle(), command.getCommandId(), command.getSequenceId(),
                command.getOrderId());
    }

    private static void errorLog(String message, SagaCommandEntity command, Exception e) {
        log.error("SAGA (ORCHESTRATOR) - COMMAND_CONDITION_EXCEPTION - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                message, command.getCommandTitle(), command.getCommandId(), command.getSequenceId(),
                command.getOrderId(), e);
    }
}
