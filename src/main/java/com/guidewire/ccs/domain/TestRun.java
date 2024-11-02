package com.guidewire.ccs.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.guidewire.ccs.utils.Helper;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TestRun implements Serializable {
    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Generated
    @JsonProperty("id")
    private final long id = COUNTER.getAndIncrement();
    @JsonProperty("test_project_name")
    private String testProjectName;
    @JsonProperty("test_seed")
    @Generated
    private final long testSeed = 0L;
    @JsonProperty("start_time")
    private OffsetDateTime startTime;
    @JsonProperty("end_time")
    private OffsetDateTime endTime;
    @JsonProperty("suite_runs")
    private SuiteRun[] suiteRuns;

    @JsonGetter
    public String getStartTime() {
        return startTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @JsonGetter
    public String getEndTime() {
        return endTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}


