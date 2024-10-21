package com.guidewire.ccs.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Tag {
    private long id;
    private String name;
}
