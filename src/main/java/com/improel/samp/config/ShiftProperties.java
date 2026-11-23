// Classe de configuração para mapear os horários e tolerâncias de jornada do application.yml.

package com.improel.samp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
@ConfigurationProperties(prefix = "samp.shift")
@Getter
@Setter
public class ShiftProperties {

    private LocalTime expectedStart = LocalTime.of(8, 0);
    private LocalTime maxStartTolerance = LocalTime.of(8, 15);
    private LocalTime expectedEnd = LocalTime.of(17, 30);
    private LocalTime maxEndTolerance = LocalTime.of(18, 0);
}
