package com.nazaninfz.saga.orchestrator.core.entity;

import com.nazaninfz.messagingsharedmodel.enums.RollbackType;
import com.nazaninfz.saga.orchestrator.core.enums.CommandStatus;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandInput;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import com.nazaninfz.saga.orchestrator.core.model.CommandHelperIdentifier;
import com.nazaninfz.saga.orchestrator.core.model.SagaCommandStepHistory;
import com.nazaninfz.saga.orchestrator.utils.SagaCommandStepUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document("saga_command")
public class SagaCommandEntity {

    @Id
    private String commandId;
    private String commandName;
    private String sequenceId;
    private String exceptionSubText;
    private int orderId;
    private SagaCommandInput input;
    private SagaCommandOutput output;
    private CommandStatus commandStatus;
    private RollbackType rollbackIfCurrentCommandHasException;
    private RollbackType rollbackIfNextCommandsHaveException;
    private List<SagaCommandStepHistory> stepHistories;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @Transient
    public void addStepHistory(SagaCommandStepHistory stepHistory) {
        if (CollectionUtils.isEmpty(stepHistories)) {
            stepHistories = new ArrayList<>();
        }
        stepHistories.add(stepHistory);
    }

    @Transient
    public void addSuccessStep(CommandHelperIdentifier identifier, SagaCommandStepType stepType) {
        addStepHistory(SagaCommandStepUtils.success(identifier, stepType));
    }

    @Transient
    public void addSkipStep(CommandHelperIdentifier identifier, SagaCommandStepType stepType) {
        addStepHistory(SagaCommandStepUtils.skip(identifier, stepType));
    }

    @Transient
    public void addFailureStep(CommandHelperIdentifier identifier, SagaCommandStepType stepType) {
        addStepHistory(SagaCommandStepUtils.failure(identifier, stepType));
    }

    @Transient
    public void addFailureStep(CommandHelperIdentifier identifier, SagaCommandStepType stepType, Throwable e) {
        addStepHistory(SagaCommandStepUtils.failure(identifier, stepType, e));
    }
}
