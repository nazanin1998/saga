package com.nazaninfz.saga.orchestrator.engine;

import com.nazaninfz.messagingsharedmodel.enums.ResponseType;
import com.nazaninfz.messagingsharedmodel.models.UserData;
import com.nazaninfz.messagingsharedmodel.models.request.RequestObject;
import com.nazaninfz.messagingsharedmodel.models.response.ResponseObject;
import com.nazaninfz.rabbitproducer.exception.InvalidMessagingRequestException;
import com.nazaninfz.rabbitproducer.exception.SendMessageException;
import com.nazaninfz.saga.orchestrator.core.config.props.OrchestratorProps;
import com.nazaninfz.saga.orchestrator.core.entity.SagaRemoteCommandEntity;
import com.nazaninfz.saga.orchestrator.core.exception.MicroServiceCommitException;
import com.nazaninfz.saga.orchestrator.core.exception.SagaRemoteCommandInvalidResponseObjectException;
import com.nazaninfz.saga.orchestrator.core.exception.SagaRemoteCommandTimeoutException;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.mapper.SagaMapper;
import com.nazaninfz.saga.orchestrator.core.model.SagaRemoteCommand;
import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import com.nazaninfz.saga.orchestrator.utils.RemoteCommandActionUtils;
import com.nazaninfz.saga.orchestrator.utils.SagaOrchLogger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SagaRemoteCommandExecutor {

    private final SagaMapper mapper;
    private final OrchestratorProps orchestratorProps;
    private final SagaCommandRepository commandRepository;

    public void commit(SagaRemoteCommand command) {
        SagaRemoteCommandEntity commandEntity = mapper.toEntity(command);
        commandRepository.save(commandEntity);

        RequestObject request = createRequestObject(commandEntity);
        RemoteCommandActionUtils.readyToSend(commandEntity);
        commandRepository.save(commandEntity);

        try {
            SagaOrchLogger.infoLog("REMOTE_COMMIT", "send request: " + request, commandEntity);
            ResponseObject response = command.getProducer().sendAndReceive(request);
            SagaOrchLogger.infoLog("REMOTE_COMMIT", "receive response is: " + response, commandEntity);

            RemoteCommandActionUtils.receiveResponseStatus(commandEntity);
            commandRepository.save(commandEntity);

            Object body = handleResponseAndGetBody(response);
            commandEntity.setOutput((SagaCommandOutput) body);
            RemoteCommandActionUtils.parsedResponse(commandEntity);
            commandRepository.save(commandEntity);

        } catch (SagaRemoteCommandTimeoutException e) {
            SagaOrchLogger.errorLog("REMOTE_COMMIT_EXCEPTION", "timeout", commandEntity, e);
            RemoteCommandActionUtils.timeout(commandEntity);
            commandRepository.save(commandEntity);
            throw e;
        } catch (SagaRemoteCommandInvalidResponseObjectException e) {
            SagaOrchLogger.errorLog("REMOTE_COMMIT_EXCEPTION", "invalid response object", commandEntity, e);
            RemoteCommandActionUtils.parseResponseFailed(commandEntity);
            commandRepository.save(commandEntity);
            throw e;
        } catch (MicroServiceCommitException e) {
            SagaOrchLogger.errorLog("REMOTE_COMMIT_EXCEPTION", "micro service commiting exception", commandEntity, e);
            RemoteCommandActionUtils.microserviceThrowsException(commandEntity);
            commandRepository.save(commandEntity);
            throw e;
        } catch (InvalidMessagingRequestException e) {
            SagaOrchLogger.errorLog("REMOTE_COMMIT_EXCEPTION", "invalid response object", commandEntity, e);
            RemoteCommandActionUtils.emptyMessagingData(commandEntity);
            commandRepository.save(commandEntity);
        } catch (SendMessageException e) {
            SagaOrchLogger.errorLog("REMOTE_COMMIT_EXCEPTION", "messaging layer exception", commandEntity, e);
            RemoteCommandActionUtils.messagingLayerException(commandEntity);
            commandRepository.save(commandEntity);
            throw e;
        }
    }

    private Object handleResponseAndGetBody(ResponseObject response) {
        if (response == null) {
            throw new SagaRemoteCommandTimeoutException();
        }
        if (response.getResult() == null || response.getResult().getResponseType() == null) {
            throw new SagaRemoteCommandInvalidResponseObjectException();
        }
        if (response.getResult().getResponseType() == ResponseType.SUCCESS) {
            return response.getBody();
        }
        throw new MicroServiceCommitException(response.getResult());
    }

    private RequestObject createRequestObject(SagaRemoteCommandEntity entity) {

        RequestObject requestObject = mapper.toRequestObject(entity);
        requestObject.setProducerName(orchestratorProps.getApplicationName());
        requestObject.setUser(new UserData());

        if (requestObject.getTimeout() == null) {
            requestObject.setTimeout(orchestratorProps.getDefaultCommandTimeOut().toMillis());
        }
        return requestObject;
    }

}
