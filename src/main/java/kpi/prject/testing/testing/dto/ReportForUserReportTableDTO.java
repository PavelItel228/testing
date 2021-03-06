package kpi.prject.testing.testing.dto;


import kpi.prject.testing.testing.entity.enums.ReportStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ReportForUserReportTableDTO {
    @NotNull
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private ReportStatus status;
    @NotNull
    private LocalDate created;
    @NotNull
    private LocalDate updated;
    @NotNull
    private String declineReason;
}
