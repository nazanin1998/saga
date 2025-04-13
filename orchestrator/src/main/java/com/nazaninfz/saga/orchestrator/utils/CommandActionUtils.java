package com.nazaninfz.saga.orchestrator.utils;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.enums.CommandStatus;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import com.nazaninfz.saga.orchestrator.core.model.CommandExecutionCondition;
import com.nazaninfz.saga.orchestrator.core.model.CommandHelperIdentifier;
import com.nazaninfz.saga.orchestrator.core.model.CommandInputDecorator;
import com.nazaninfz.saga.orchestrator.core.model.CommandPostExecution;
import com.nazaninfz.saga.orchestrator.engine.InputDecorator;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.UUID;

@UtilityClass
public class CommandActionUtils {

    public void init(SagaCommandEntity command, String sequenceId) {
        command.setSequenceId(sequenceId)
                .setCommandId(UUID.randomUUID().toString())
                .setCommandStatus(CommandStatus.INITIALIZED)
                .setStepHistories(new ArrayList<>());
    }

    public void emptyConditions(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.CONDITIONS_CHECKING_NOT_NEEDED);
        command.addSkipStep(new CommandHelperIdentifier()
                        .setId(UUID.randomUUID().toString())
                        .setTitle("EMPTY_CONDITIONS"),
                SagaCommandStepType.CONDITION);
    }

    public void emptyDecorator(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.DECORATION_NOT_NEEDED);
        command.addSkipStep(new CommandHelperIdentifier()
                        .setId(UUID.randomUUID().toString())
                        .setTitle("EMPTY_DECORATOR"),
                SagaCommandStepType.DECORATOR);
    }

    public void emptyPostExecution(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.POST_EXECUTION_NOT_NEEDED);
        command.addSkipStep(new CommandHelperIdentifier()
                        .setId(UUID.randomUUID().toString())
                        .setTitle("EMPTY_POST_EXECUTION"),
                SagaCommandStepType.POST_EXECUTION);
    }

    public void startCheckingConditions(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.CONDITIONS_CHECKING_STARTED);
    }

    public void startDecoration(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.DECORATION_STARTED);
    }

    public void startPostExecution(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.POST_EXECUTION_STARTED);
    }

    public void failCheckingConditions(SagaCommandEntity command, CommandExecutionCondition condition) {
        command.setCommandStatus(CommandStatus.CONDITIONS_CHECKING_FAILED);
        command.addFailureStep(condition, SagaCommandStepType.CONDITION);
    }

    public void failCheckingConditions(SagaCommandEntity command, CommandExecutionCondition condition, Exception e) {
        command.setCommandStatus(CommandStatus.CONDITIONS_CHECKING_FAILED);
        command.addFailureStep(condition, SagaCommandStepType.CONDITION, e);
    }

    public void failDecoration(SagaCommandEntity command, CommandInputDecorator decorator, Exception e) {
        command.setCommandStatus(CommandStatus.DECORATION_FAILED);
        command.addFailureStep(decorator, SagaCommandStepType.DECORATOR, e);
    }

    public void failPostExecution(SagaCommandEntity command, CommandPostExecution postExecution, Exception e) {
        command.setCommandStatus(CommandStatus.POST_EXECUTION_FAILED);
        command.addFailureStep(postExecution, SagaCommandStepType.POST_EXECUTION, e);
    }

    public void successCheckingConditions(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.CONDITIONS_CHECKING_PASSED);
    }

    public void successDecoration(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.DECORATION_PASSED);
    }

    public void successPostExecution(SagaCommandEntity command) {
        command.setCommandStatus(CommandStatus.POST_EXECUTION_PASSED);
    }

    public void addSuccessCheckingConditionsStep(SagaCommandEntity command, CommandExecutionCondition condition) {
        command.addSuccessStep(condition, SagaCommandStepType.CONDITION);
    }

    public void addSuccessDecorationStep(SagaCommandEntity command, CommandInputDecorator decorator) {
        command.addSuccessStep(decorator, SagaCommandStepType.DECORATOR);
    }

    public void addSuccessPostExecutionStep(SagaCommandEntity command, CommandPostExecution postExecution) {
        command.addSuccessStep(postExecution, SagaCommandStepType.POST_EXECUTION);
    }

}
