package kpi.prject.testing.testing.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
public class ReportDTO {
    @NotNull
    private final String name;
    @NotNull
    private final String description;
}
