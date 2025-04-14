package com.nazaninfz.saga.orchestrator.core.entity;

import com.nazaninfz.messagingsharedmodel.models.MessagingData;
import com.nazaninfz.saga.orchestrator.core.enums.RemoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SagaRemoteCommandEntity extends SagaCommandEntity {

    private RemoteStatus remoteStatus;

    private Date commitStartDate;
    private Date commitEndDate;

    private Date rollBackStartDate;
    private Date rollBackEndDate;

    private Long timeout;

    private MessagingData messaging;

    private Map<String, Object> remoteHeaders;

}
