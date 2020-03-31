package kpi.prject.testing.testing.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReportForInspectorReportTableDTO {
    @NotNull
    private Long id;
    @NotNull
    private String Name;
    @NotNull
    private LocalDate created;
    @NotNull
    private LocalDate updated;
    @NotNull
    private String description;
}
