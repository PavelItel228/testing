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
import java.util.List;

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
        archiveRepository.save(createArchive(report, inspector));
        reportsRepository.save(report);
    }
    //todo
    @Transactional
    public void declineReport(Report reportToDecline, DeclineReasonDTO reportReason, User inspector) {
        archiveRepository.save(createArchive(reportToDecline, reportReason, inspector));
        reportsRepository.save(reportToDecline);
    }

    private Archive createArchive(Report reportToDecline, DeclineReasonDTO reportReason, User inspector){
        return Archive.builder()
                .report(reportToDecline)
                .inspectorDecision(inspector)
                .name(reportToDecline.getName())
                .description(reportToDecline.getDescription())
                .declineReason(reportReason.getDeclineReason())
                .status(ReportStatus.NOT_ACCEPTED)
                .build();
    }

    private Archive createArchive(Report reportToDecline, User inspector){
        return Archive.builder()
                .report(reportToDecline)
                .inspectorDecision(inspector)
                .name(reportToDecline.getName())
                .description(reportToDecline.getDescription())
                .status(ReportStatus.NOT_ACCEPTED)
                .build();
    }
}
