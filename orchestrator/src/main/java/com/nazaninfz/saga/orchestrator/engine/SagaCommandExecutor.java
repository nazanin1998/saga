package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.mapper.SagaMapper;
import com.nazaninfz.saga.orchestrator.core.model.SagaBaseCommand;
import com.nazaninfz.saga.orchestrator.core.model.SagaCommand;
import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import com.nazaninfz.saga.orchestrator.service.SagaSequenceServices;
import com.nazaninfz.saga.orchestrator.utils.CommandActionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaCommandExecutor {
    private final SagaCommandFactory commandFactory;
    private final SagaCommandRepository commandRepository;
    private final SagaMapper mapper;
    private final SagaSequenceServices sequenceServices;
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
            log.error("UnknownCommandTypeException in execute command");
            throw new UnknownCommandTypeException();
        }
        SagaCommandEntity commandEntity = saveCommand(command, sequenceEntity.getSequenceId());
        infoLog("Started", commandEntity);

        boolean areConditionsSatisfied = conditionChecker.areAllConditionsSatisfied(
                sagaCommand.getConditions(),
                sagaCommand.getInput(),
                commandEntity,
                outputMap,
                contextMap);

        if (!areConditionsSatisfied) return;

        inputDecorator.decorate(
                sagaCommand.getDecorator(),
                sagaCommand.getInput(),
                commandEntity,
                outputMap,
                contextMap
        );
        commitCommand(sagaCommand, commandEntity, sequenceEntity);
        postExecutor.execute(
                sagaCommand.getPostExecutions(),
                sagaCommand.getInput(),
                commandEntity,
                outputMap,
                contextMap
        );

        if (CollectionUtils.isEmpty(sagaCommand.getNextCommands())) {
            return;
        }

        for (SagaBaseCommand nextCommand : sagaCommand.getNextCommands()) {
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
            commandEntity = commandEntity.saveNewStatus(CommandStatus.COMMITTING_STARTED, commandServices);
            command.commit();
            commandEntity = commandEntity.saveNewStatus(CommandStatus.COMMITTING_PASSED, commandServices);
            sequenceEntity = sequenceServices.saveSequence(sequenceEntity.addExecutedCommandId(command.getCommandId()));
        } catch (SagaGeneralException e) {
            handleCommittingException(command, commandEntity, e);
        } catch (Exception e) {
            handleCommittingException(command, commandEntity, new SagaGeneralException(e));
        }

    }

    private void handleCommittingException(SagaCommand command, SagaCommandEntity commandEntity, SagaGeneralException e) {
        log.error("exception in commit command id: {}, title: {}",
                command.getCommandId(),
                command.getCommandTitle());
        commandEntity.setExceptionSubText(ExceptionUtils.getStackTrace(e));

        if (command.getOnExceptionBehavior() == OnExceptionBehavior.ROLLBACK) {
            commandEntity.setCommandStatus(CommandStatus.COMMITTING_FAILED);
            commandEntity = commandEntity.save(commandServices);
            throw e;
        }
        if (command.getOnExceptionBehavior() == OnExceptionBehavior.SKIP) {
            commandEntity = commandEntity.saveNewStatus(CommandStatus.COMMITTING_SKIPPED, commandServices);
            return;
        }

        log.error("unsupported operation for on exception behaviour");
        throw new UnsupportedOperationException(e);
    }

    private static void infoLog(String message, SagaCommandEntity command) {
        log.info("SAGA (ORCHESTRATOR) - COMMAND - {}, command: {}, commandId: {}, sequenceId: {}, orderId: {}",
                message, command.getCommandTitle(), command.getCommandId(), command.getSequenceId(),
                command.getOrderId());
    }

    private static void errorLog(String message, SagaSequenceEntity sequenceEntity, Exception e) {
        log.error("SAGA (ORCHESTRATOR) - COMMAND_EXCEPTION - {}, sequence id: {}, title: {}",
                message, sequenceEntity.getSequenceId(), sequenceEntity.getSequenceTitle(), e);
    }

}
