package com.nazaninfz.saga.orchestrator.core.config.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
public class OrchestratorProps {

    @Value("spring.application.name")
    private String applicationName;

    @Value("saga.command.default.time-out")
    private Duration defaultCommandTimeOut;

}
