package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.dto.DeclineReasonDTO;
import kpi.prject.testing.testing.dto.ReportDTO;
import kpi.prject.testing.testing.dto.ReportForInspectorReportTableDTO;
import kpi.prject.testing.testing.dto.ReportForUserReportTableDTO;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.entity.enums.Role;
import kpi.prject.testing.testing.repository.ReportsRepository;
import kpi.prject.testing.testing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ReportService {

    private final ReportsRepository reportsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReportService(ReportsRepository reportsRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.reportsRepository = reportsRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Page<ReportForUserReportTableDTO> getAllByUserForUserTable(User user, Pageable pageable) {
        Page<Report> reports = reportsRepository.findByUser(user, pageable).orElse(new PageImpl<>(new ArrayList<>()));
        Type pageType = new TypeToken<Page<ReportForUserReportTableDTO>>() {}.getType();
        return modelMapper.map(reports, pageType);
    }



    public Page<ReportForInspectorReportTableDTO> getAllByInspectorAndStatusForTable(User user, ReportStatus status, Pageable pageable) {
        Page<Report> reports = reportsRepository.findAllByInspectorAndStatus(user, status, pageable).orElse(new PageImpl<>(new ArrayList<>()));
        Type pageType = new TypeToken<Page<ReportForInspectorReportTableDTO>>() {}.getType();
        return modelMapper.map(reports, pageType);
    }

    public Report getFromDTO(ReportDTO reportDTO) {
        return modelMapper.map(reportDTO, Report.class);
    }

    @Transactional
    public void save(Report report, User owner) {
        report.setStatus(ReportStatus.QUEUE);
        report.setUser(owner);
        List<User> inspectors = getInscpectors();
        report.setInspector(getRandomElement(inspectors));
        reportsRepository.save(report);
    }

    private static User getRandomElement(List<User> list) {
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

    public void update(Report report,ReportDTO reportDTO) {
        report.setName(reportDTO.getName());
        report.setDescription(reportDTO.getDescription());
        report.setStatus(ReportStatus.QUEUE);
        reportsRepository.save(report);
    }

    public void changeInspector(Report report) {
        List<User> inspectors = getInscpectors();
        inspectors.remove(report.getInspector());
        report.setStatus(ReportStatus.QUEUE);
        report.setInspector(getRandomElement(inspectors));
        reportsRepository.save(report);
    }

    private List<User> getInscpectors() {
        return userRepository.findAllByRole(Role.ROLE_INSPECTOR).orElse(new ArrayList<>());
    }

    public Optional<ReportDTO> getDTOById(long id) {
        Optional<Report> report = reportsRepository.findById(id);
        Type optionalReportType = new TypeToken<Optional<ReportDTO>>() {}.getType();
        return modelMapper.map(report, optionalReportType);
    }
}
