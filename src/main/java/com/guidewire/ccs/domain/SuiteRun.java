package com.guidewire.ccs.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SuiteRun implements Serializable {
    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Generated
    @JsonProperty("id")
    private final long id = COUNTER.getAndIncrement();
    @JsonProperty("test_run_id")
    private long testRunId;
    @JsonProperty("suite_name")
    private String suiteName;
    @JsonProperty("start_time")
    private OffsetDateTime startTime;
    @JsonProperty("end_time")
    private OffsetDateTime endTime;
    @JsonProperty("spec_runs")
    private SpecRun[] specRuns;

    @JsonGetter
    public String getStartTime() {
        return startTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @JsonGetter
    public String getEndTime() {
        return endTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
