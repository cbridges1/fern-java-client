package com.guidewire.ccs.domain;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TestRun {
    private long id;
    private String testProjectName;
    private long testSeed;
    private Instant startTime;
    private Instant endTime;
    private SuiteRun[] suiteRuns;

}


