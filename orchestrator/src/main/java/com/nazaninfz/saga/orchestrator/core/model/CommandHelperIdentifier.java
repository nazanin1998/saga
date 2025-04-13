package com.nazaninfz.saga.orchestrator.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandHelperIdentifier {
    private String id;
    private String title;
}
