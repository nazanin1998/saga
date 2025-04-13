package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.messagingsharedmodel.enums.RollbackType;
import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import com.nazaninfz.saga.orchestrator.core.exception.CommitingException;
import com.nazaninfz.saga.orchestrator.core.exception.UnknownCommandTypeException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.mapper.SagaMapper;
import com.nazaninfz.saga.orchestrator.core.model.SagaBaseCommand;
import com.nazaninfz.saga.orchestrator.core.model.SagaCommand;
import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import com.nazaninfz.saga.orchestrator.repository.SagaSequenceRepository;
import com.nazaninfz.saga.orchestrator.utils.CommandActionUtils;
import com.nazaninfz.saga.orchestrator.utils.SagaOrchLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaCommandExecutor {
    private final SagaCommandRepository commandRepository;
    private final SagaMapper mapper;
    private final SagaSequenceRepository sequenceRepository;
    private final InputDecorator inputDecorator;
    private final PostExecutor postExecutor;
    private final ConditionChecker conditionChecker;

    public void executeCommand(
            SagaBaseCommand baseCommand,
            Map<String, Object> contextMap,
            Map<String, SagaCommandOutput> outputMap,
            SagaSequenceEntity sequenceEntity
    ) {
//        if (baseCommand instanceof SagaSubSequence) {
//            executeCommand(
//                    ((SagaSubSequence) baseCommand).getRootCommand(),
//                    contextMap,
//                    outputMap,
//                    sequenceEntity);
//            return;
//        }
        if (!(baseCommand instanceof SagaCommand command)) {
            SagaOrchLogger.errorLog("EXECUTION", "Unknown command type exception");
            throw new UnknownCommandTypeException();
        }

        SagaCommandEntity commandEntity = saveCommand(command, sequenceEntity.getSequenceId());
        SagaOrchLogger.infoLog("EXECUTION", "Started", commandEntity);

        boolean areConditionsSatisfied = conditionChecker.areAllConditionsSatisfied(
                command.getConditions(),
                commandEntity,
                outputMap,
                contextMap);

        if (!areConditionsSatisfied) return;

        inputDecorator.decorate(command.getDecorator(), commandEntity, outputMap, contextMap);

        commitCommand(command, commandEntity, sequenceEntity);

        postExecutor.execute(command.getPostExecutions(), commandEntity, outputMap, contextMap);

        if (CollectionUtils.isEmpty(command.getNextCommands())) {
            return;
        }

        for (SagaBaseCommand nextCommand : command.getNextCommands()) {
            executeCommand(nextCommand, contextMap, outputMap, sequenceEntity);
        }
    }

    private SagaCommandEntity saveCommand(SagaCommand command, String sequenceId) {
        SagaCommandEntity commandEntity = mapper.toEntity(command);
        CommandActionUtils.init(commandEntity, sequenceId);
        return commandRepository.save(commandEntity);
    }

    private void commitCommand(
            SagaCommand command,
            SagaCommandEntity commandEntity,
            SagaSequenceEntity sequenceEntity
    ) {
        try {
            CommandActionUtils.startCommit(commandEntity);
            commandRepository.save(commandEntity);
            command.commit();
            CommandActionUtils.successCommiting(commandEntity);
            commandRepository.save(commandEntity);
            sequenceRepository.save(sequenceEntity.addExecutedCommandId(commandEntity.getCommandId()));
        } catch (MicroserviceException e) {
            handleCommittingException(command, commandEntity, e);
        } catch (Exception e) {
            handleCommittingException(command, commandEntity, new CommitingException(e));
        }

    }

    private void handleCommittingException(SagaCommand command, SagaCommandEntity commandEntity, MicroserviceException e) {
        SagaOrchLogger.errorLog("EXECUTION", "exception in commit command", commandEntity, e);
        commandEntity.setExceptionSubText(ExceptionUtils.getStackTrace(e));

        if (commandEntity.getRollbackIfCurrentCommandHasException() == RollbackType.REQUIRED) {
            CommandActionUtils.failCommit(commandEntity);
            commandRepository.save(commandEntity);
            SagaOrchLogger.infoLog("EXECUTION", "Must have rollback", commandEntity);
            throw e;
        }
        if (command.getRollbackIfCurrentCommandHasException() == RollbackType.NOT_REQUIRED) {
            CommandActionUtils.skipCommit(commandEntity);
            commandRepository.save(commandEntity);
            SagaOrchLogger.infoLog("EXECUTION", "Skip command", commandEntity);
            return;
        }

        SagaOrchLogger.infoLog("EXECUTION", "Unsupported RollbackType", commandEntity);
        throw new UnsupportedOperationException(e);
    }


}
