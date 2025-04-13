//package com.nazaninfz.saga.orchestrator.engine;
//
//
//import com.nazaninfz.saga.orchestrator.entity.SagaSequenceEntity;
//import com.nazaninfz.saga.orchestrator.interfaces.SagaCommandOutput;
//import com.nazaninfz.saga.orchestrator.mapper.SagaMapper;
//import com.nazaninfz.saga.orchestrator.model.SagaCommand;
//import com.nazaninfz.saga.orchestrator.model.SagaSequence;
//import com.nazaninfz.saga.orchestrator.repository.SagaSequenceRepository;
//import com.nazaninfz.saga.orchestrator.utils.SequenceActionUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Slf4j
//@RequiredArgsConstructor
//public class SagaSequenceExecutor {
//    private final SagaSequenceRepository sequenceRepository;
//    private final SagaMapper sequenceMapper;
//    private final SagaCommandExecutor commandExecutor;
//
//    public void startSequence(SagaSequence sequence) {
//        SagaSequenceEntity sequenceEntity = saveSequence(sequence);
//        infoLog("Started", sequenceEntity);
//
//        Map<String, SagaCommandOutput> outputMap = new HashMap<>();
//
//        try {
//            executeRootCommand(sequence.getRootCommand(), sequenceEntity, outputMap, sequence.getContextMap());
//        } catch (UnSuccessfulRollbackException e) {
//            errorLog("UnSuccessfulRollbackException", sequenceEntity, e);
//            throw e;
//        } catch (SagaGeneralException e) {
//            errorLog("SagaGeneralException", sequenceEntity, e);
//            throw e;
//        } catch (Exception e) {
//            errorLog("UnhandledException", sequenceEntity, e);
//            sequenceEntity = sequenceEntity.saveNewStatus(SequenceStatus.UNKNOWN, sequenceServices);
//            throw e;
//        }
//
//    }
//
//    private void executeRootCommand(
//            SagaCommand rootCommand,
//            SagaSequenceEntity sequenceEntity,
//            Map<String, SagaCommandOutput> outputMap,
//            Map<String, Object> contextMap
//    ) {
//        String id = sequenceEntity.getSequenceId();
//        String title = sequenceEntity.getSequenceTitle();
//
//        try {
//            commandExecutor.executeCommand(
//                    rootCommand,
//                    contextMap,
//                    outputMap,
//                    sequenceEntity);
//
//            sequenceEntity = sequenceEntity.saveNewStatus(SequenceStatus.SUCCEED, sequenceServices);
//            sendCompleteSignal(id);
//            log.info("sequence completed successfully, id: {}, title: {}", id, title);
//        } catch (Exception e) {
//            log.error("exception in sequence, id: {}, title: {}", id, title, e);
//            sequenceEntity = sequenceServices.saveSequence(
//                    sequenceEntity.setExceptionSubText(ExceptionUtils.getStackTrace(e)));
//            sendRollbackSignal(id);
//            throw e;
//        }
//    }
//
//    private void sendCompleteSignal(String sequenceId) {
//        // todo send complete signal
//    }
//
//    private void sendRollbackSignal(String sequenceId) {
//        // todo send rollback signal
//    }
//
//    private SagaSequenceEntity saveSequence(SagaSequence sequence) {
//        SagaSequenceEntity sequenceEntity = sequenceMapper.toEntity(sequence);
//        SequenceActionUtils.init(sequenceEntity);
//        sequenceRepository.save(sequenceEntity);
//        return sequenceEntity;
//    }
//
//    private static void infoLog(String message, SagaSequenceEntity sequenceEntity) {
//        log.info("SAGA (ORCHESTRATOR) - SEQUENCE - {}, sequence: {}", message, sequenceEntity);
//    }
//
//    private static void errorLog(String message, SagaSequenceEntity sequenceEntity, Exception e) {
//        log.error("SAGA (ORCHESTRATOR) - SEQUENCE_EXCEPTION - {}, sequence id: {}, title: {}",
//                message, sequenceEntity.getSequenceId(), sequenceEntity.getSequenceTitle(), e);
//    }
//}
