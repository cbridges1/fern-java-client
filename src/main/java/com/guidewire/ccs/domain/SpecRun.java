package com.guidewire.ccs.domain;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SpecRun {
    private long id;
    private long suiteId;
    private String specDescription;
    private String status;
    private String message;
    private Instant startTime;
    private Instant endTime;
    private Tag[] tags;
}
