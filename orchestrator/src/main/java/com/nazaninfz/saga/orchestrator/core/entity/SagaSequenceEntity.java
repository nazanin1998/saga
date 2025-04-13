package com.nazaninfz.saga.orchestrator.core.entity;

import com.nazaninfz.saga.orchestrator.core.enums.SequenceStatus;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document("saga_sequence")
public class SagaSequenceEntity implements Serializable {

    @Id
    private String sequenceId;
    private String sequenceTitle;
    private SequenceStatus sequenceStatus;
    private Map<String, Object> contextMap;
    private List<String> executedCommandIds;
    private String exceptionSubText;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @Transient
    public SagaSequenceEntity addExecutedCommandId(String commandId) {
        if (CollectionUtils.isEmpty(executedCommandIds)) {
            executedCommandIds = new ArrayList<>();
        }
        executedCommandIds.add(commandId);
        return this;
    }

}
