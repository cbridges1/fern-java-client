package com.guidewire.ccs.domain;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SuiteRun {
    private long id;
    private long testRunId;
    private String suiteName;
    private Instant startTime;
    private Instant endTime;
    private SpecRun[] specRuns;
}
