package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.exception.PostExecutionException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.model.CommandPostExecution;
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
public class PostExecutor {

    private final SagaCommandRepository commandRepository;

    public void execute(
            List<CommandPostExecution> postExecutions,
            SagaCommandEntity commandEntity,
            Map<String, SagaCommandOutput> outputMap,
            Map<String, Object> contextMap
    ) {

        if (CollectionUtils.isEmpty(postExecutions)) {
            infoLog("Empty post execution", commandEntity);
            CommandActionUtils.emptyPostExecution(commandEntity);
            commandRepository.save(commandEntity);
            return;
        }
        infoLog("Started", commandEntity);
        CommandActionUtils.startPostExecution(commandEntity);
        commandRepository.save(commandEntity);

        for (CommandPostExecution postExecution : postExecutions) {
            doPostExecution(postExecution, commandEntity, outputMap, contextMap);
        }
        CommandActionUtils.startPostExecution(commandEntity);
        commandRepository.save(commandEntity);
        infoLog("PostExecution Done", commandEntity);
    }

    private void doPostExecution(
            CommandPostExecution postExecution,
            SagaCommandEntity commandEntity,
            Map<String, SagaCommandOutput> outputMap,
            Map<String, Object> contextMap
    ) {
        try {
            postExecution.execute(commandEntity.getInput(), outputMap, contextMap);
            CommandActionUtils.addSuccessPostExecutionStep(commandEntity, postExecution);
            commandRepository.save(commandEntity);
        } catch (Exception e) {
            errorLog("post execution exception", commandEntity, e);
            CommandActionUtils.failPostExecution(commandEntity, postExecution, e);
            commandRepository.save(commandEntity);
            throw new PostExecutionException(e);
        }
    }

    private static void infoLog(String message, SagaCommandEntity command) {
        log.info("SAGA (ORCHESTRATOR) - COMMAND_POST_EXECUTION - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                message, command.getCommandTitle(), command.getCommandId(), command.getSequenceId(),
                command.getOrderId());
    }

    private static void errorLog(String message, SagaCommandEntity command, Exception e) {
        log.error("SAGA (ORCHESTRATOR) - COMMAND_POST_EXECUTION_EXCEPTION - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                message, command.getCommandTitle(), command.getCommandId(), command.getSequenceId(),
                command.getOrderId(), e);
    }

}
