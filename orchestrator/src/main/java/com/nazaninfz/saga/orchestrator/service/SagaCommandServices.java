package com.nazaninfz.saga.orchestrator.service;

import com.nazaninfz.saga.orchestrator.repository.SagaCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaCommandServices {
    private final SagaCommandRepository commandRepository;

}
