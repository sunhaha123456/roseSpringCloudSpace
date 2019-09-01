package com.rose.conf;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeBeweenConfig {
    private LocalTime start;
    private LocalTime end;
}
