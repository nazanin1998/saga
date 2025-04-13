package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import com.nazaninfz.saga.orchestrator.core.exception.PostExecutionException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.model.CommandPostExecution;
import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import com.nazaninfz.saga.orchestrator.utils.CommandActionUtils;
import com.nazaninfz.saga.orchestrator.utils.SagaOrchLogger;
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
            SagaOrchLogger.infoLog(SagaCommandStepType.POST_EXECUTION, "Empty post execution", commandEntity);
            CommandActionUtils.emptyPostExecution(commandEntity);
            commandRepository.save(commandEntity);
            return;
        }
        SagaOrchLogger.infoLog(SagaCommandStepType.POST_EXECUTION, "Started", commandEntity);
        CommandActionUtils.startPostExecution(commandEntity);
        commandRepository.save(commandEntity);

        for (CommandPostExecution postExecution : postExecutions) {
            doPostExecution(postExecution, commandEntity, outputMap, contextMap);
        }
        CommandActionUtils.successPostExecution(commandEntity);
        commandRepository.save(commandEntity);
        SagaOrchLogger.infoLog(SagaCommandStepType.POST_EXECUTION, "post execution done", commandEntity);
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
            SagaOrchLogger.errorLog(SagaCommandStepType.POST_EXECUTION, "post execution exception", commandEntity, e);
            CommandActionUtils.failPostExecution(commandEntity, postExecution, e);
            commandRepository.save(commandEntity);
            throw new PostExecutionException(e);
        }
    }
}
