package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.dto.ReportDTO;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.entity.enums.Role;
import kpi.prject.testing.testing.repository.ArchiveRepository;
import kpi.prject.testing.testing.repository.ReportsRepository;
import kpi.prject.testing.testing.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    private final ReportsRepository reportsRepository;
    private final UserRepository userRepository;
    private final ArchiveRepository archiveRepository;

    public OwnerService(ReportsRepository reportsRepository, UserRepository userRepository, ArchiveRepository archiveRepository) {
        this.reportsRepository = reportsRepository;
        this.userRepository = userRepository;
        this.archiveRepository = archiveRepository;
    }

    public void update(Report report, ReportDTO reportDTO) {
        report.setName(reportDTO.getName());
        report.setDescription(reportDTO.getDescription());
        report.setStatus(ReportStatus.QUEUE);
        reportsRepository.save(report);
    }
    @Transactional
    public void changeInspector(Report report) {
        List<User> inspectors = report.getInspectors();
        if (inspectors.size() == 1) {
            report.setStatus(ReportStatus.QUEUE);
            reportsRepository.save(report);
            return;
        }
        Long inspectorId = archiveRepository.findFirstByReportOrderByIdDesc(report)
                .orElseThrow(RuntimeException::new).getInspectorDecision().getId();
        List<User> newInspectors = inspectors.stream()
                .filter(inspector -> !inspector.getId().equals(inspectorId))
                .collect(Collectors.toList());
        report.setStatus(ReportStatus.QUEUE);
        report.setInspectors(newInspectors);
        reportsRepository.save(report);
    }

    public void save(Report report, User owner) {
        report.setStatus(ReportStatus.QUEUE);
        report.setOwner(owner);
        List<User> inspectors = getInspectors();
        report.setInspectors(inspectors);
        reportsRepository.save(report);
    }


    private List<User> getInspectors() {
        return userRepository.findAllByRole(Role.ROLE_INSPECTOR).orElse(new ArrayList<>());
    }
}
