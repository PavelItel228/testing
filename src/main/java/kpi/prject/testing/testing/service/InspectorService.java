package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.dto.DeclineReasonDTO;
import kpi.prject.testing.testing.entity.Archive;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.repository.ArchiveRepository;
import kpi.prject.testing.testing.repository.ReportsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class InspectorService {

    private final ReportsRepository reportsRepository;
    private final ArchiveRepository archiveRepository;

    public InspectorService(ReportsRepository reportsRepository, ArchiveRepository archiveRepository) {
        this.reportsRepository = reportsRepository;
        this.archiveRepository = archiveRepository;
    }

    @Transactional
    public void acceptReport(Report report, User inspector) {
        Archive archive = Archive.builder()
                .report(report)
                .inspectorDecision(inspector)
                .name(report.getName())
                .description(report.getDescription())
                .status(ReportStatus.ACCEPTED)
                .build();
        archiveRepository.save(archive);
        report.setStatus(ReportStatus.ACCEPTED);
        report.setInspectors(new ArrayList<>());
        reportsRepository.save(report);
    }
    @Transactional
    public void declineReport(Report reportToDecline, DeclineReasonDTO reportReason, User inspector) {
        Archive archive = Archive.builder()
                .report(reportToDecline)
                .inspectorDecision(inspector)
                .name(reportToDecline.getName())
                .description(reportToDecline.getDescription())
                .declineReason(reportReason.getDeclineReason())
                .status(ReportStatus.NOT_ACCEPTED)
                .build();
        archiveRepository.save(archive);
        reportToDecline.setDeclineReason(reportReason.getDeclineReason());
        reportToDecline.setStatus(ReportStatus.NOT_ACCEPTED);
        reportsRepository.save(reportToDecline);
    }
}
