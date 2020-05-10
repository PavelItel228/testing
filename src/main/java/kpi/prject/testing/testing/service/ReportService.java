package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.dto.ReportDTO;
import kpi.prject.testing.testing.dto.ReportForInspectorReportTableDTO;
import kpi.prject.testing.testing.dto.ReportForUserReportTableDTO;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.repository.ReportsRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportsRepository reportsRepository;
    private final ModelMapper modelMapper;

    public ReportService(ReportsRepository reportsRepository, ModelMapper modelMapper) {
        this.reportsRepository = reportsRepository;
        this.modelMapper = modelMapper;
    }

    public Page<ReportForUserReportTableDTO> getAllByUserForUserTable(User user, Pageable pageable, String name) {
        Page<Report> reports = reportsRepository.findByOwnerAndNameContaining(user,name, pageable).orElse(new PageImpl<>(new ArrayList<>()));
        Type pageType = new TypeToken<Page<ReportForUserReportTableDTO>>() {}.getType();
        return modelMapper.map(reports, pageType);
    }



    public Page<ReportForInspectorReportTableDTO> getAllByInspectorAndStatusForTable(User user, ReportStatus status,
                                                                                     Pageable pageable, String name) {
        Page<Report> reports = reportsRepository.findAllByInspectorsAndStatusAndNameContaining(user,
                status, name, pageable);
        Type pageType = new TypeToken<Page<ReportForInspectorReportTableDTO>>() {}.getType();
        return modelMapper.map(reports, pageType);
    }

    public Report getFromDTO(ReportDTO reportDTO) {
        return modelMapper.map(reportDTO, Report.class);
    }

    public Optional<Report> getById(long id) {
        return reportsRepository.findById(id);
    }

}
