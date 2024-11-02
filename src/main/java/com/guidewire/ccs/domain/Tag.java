package com.guidewire.ccs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Tag implements Serializable {
    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Generated
    @JsonProperty("id")
    private final long id = COUNTER.getAndIncrement();
    @JsonProperty("name")
    private String name;
}
