package com.nazaninfz.saga.orchestrator.engine;


import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import com.nazaninfz.saga.orchestrator.core.exception.UnSuccessfulRollbackException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.mapper.SagaMapper;
import com.nazaninfz.saga.orchestrator.core.model.SagaSequence;
import com.nazaninfz.saga.orchestrator.repository.SagaSequenceRepository;
import com.nazaninfz.saga.orchestrator.utils.SagaOrchLogger;
import com.nazaninfz.saga.orchestrator.utils.SequenceActionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class SagaSequenceExecutor {

    private final SagaMapper sequenceMapper;
    private final SagaCommandExecutor commandExecutor;
    private final SagaSequenceRepository sequenceRepository;

    public void startSequence(SagaSequence sequence) {
        SagaSequenceEntity sequenceEntity = saveSequence(sequence);
        SagaOrchLogger.infoLog("Started", sequenceEntity);

        Map<String, SagaCommandOutput> outputMap = new HashMap<>();

        try {
            commandExecutor.executeCommand(sequence.getRootCommand(), sequence.getContextMap(), outputMap, sequenceEntity);
            SequenceActionUtils.success(sequenceEntity);
            sequenceRepository.save(sequenceEntity);
            sendCompleteSignal(sequenceEntity.getSequenceId());
            SagaOrchLogger.infoLog("completed successfully", sequenceEntity);
        } catch (UnSuccessfulRollbackException e) {
            SagaOrchLogger.errorLog("unsuccessful rollback exception", sequenceEntity, e);
            handleError(sequenceEntity, e);
            throw e;
        } catch (MicroserviceException e) {
            SagaOrchLogger.errorLog("micro service general exception" + ExceptionUtils.getStackTrace(e),
                    sequenceEntity, e);
            handleError(sequenceEntity, e);
            throw e;
        } catch (Exception e) {
            SagaOrchLogger.errorLog("unhandled exception" + ExceptionUtils.getStackTrace(e),
                    sequenceEntity, e);
            SequenceActionUtils.unknown(sequenceEntity);
            sequenceRepository.save(sequenceEntity);
            throw e;
        }

    }

    private void handleError(SagaSequenceEntity sequenceEntity, Exception e) {
        sequenceRepository.save(sequenceEntity.setExceptionSubText(ExceptionUtils.getStackTrace(e)));
        sendRollbackSignal(sequenceEntity.getSequenceId());
    }

    private void sendCompleteSignal(String sequenceId) {
        // todo send complete signal
    }

    private void sendRollbackSignal(String sequenceId) {
        // todo send rollback signal
    }

    private SagaSequenceEntity saveSequence(SagaSequence sequence) {
        SagaSequenceEntity sequenceEntity = sequenceMapper.toEntity(sequence);
        SequenceActionUtils.init(sequenceEntity);
        sequenceRepository.save(sequenceEntity);
        return sequenceEntity;
    }

}
