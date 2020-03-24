package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.dto.DeclineReasonDTO;
import kpi.prject.testing.testing.dto.ReportDTO;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.entity.enums.Role;
import kpi.prject.testing.testing.repository.ReportsRepository;
import kpi.prject.testing.testing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ReportService {

    private final ReportsRepository reportsRepository;
    private final UserRepository userRepository;

    public ReportService(ReportsRepository reportsRepository, UserRepository userRepository) {
        this.reportsRepository = reportsRepository;
        this.userRepository = userRepository;
    }

    public Page<Report> getAllByUser(User user, Pageable pageable) {
        return reportsRepository.findByUser(user, pageable).orElse(new PageImpl<>(new ArrayList<>()));
    }

    public Page<Report> getAllByInspectorAndStatus(User user, ReportStatus status, Pageable pageable) {
        return reportsRepository.findAllByInspectorAndStatus(user, status, pageable).orElse(new PageImpl<>(new ArrayList<>()));
    }

    public Report getFromDTO(ReportDTO reportDTO) {
        return Report.builder().name(reportDTO.getName()).description(reportDTO.getDescription()).build();
    }

    public void save(Report report, User owner) {
        report.setStatus(ReportStatus.QUEUE);
        report.setUser(owner);
        List<User> inspectors = userRepository.findAllByRole(Role.ROLE_INSPECTOR).orElse(new ArrayList<>());
        report.setInspector(getRandomElement(inspectors));
        reportsRepository.save(report);
    }

    private static User getRandomElement(List<User> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public Optional<Report> getById(long id) {
        return reportsRepository.findById(id);
    }

    public void acceptReport(Report report) {
        report.setStatus(ReportStatus.ACCEPTED);
        reportsRepository.save(report);
    }

    public void declineReport(Report reportToDecline, DeclineReasonDTO reportReason) {
        reportToDecline.setReason(reportReason.getReason());
        reportToDecline.setStatus(ReportStatus.NOT_ACCEPTED);
        reportsRepository.save(reportToDecline);
    }
}
