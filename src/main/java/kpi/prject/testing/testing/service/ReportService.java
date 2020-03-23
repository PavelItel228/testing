package kpi.prject.testing.testing.service;

import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.repository.ReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    final
    ReportsRepository reportsRepository;

    public ReportService(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    public Page<Report> getAllByUser(User user, Pageable pageable) {
        return reportsRepository.findByUser(user, pageable).orElse(new PageImpl<Report>(new ArrayList<>()));
    }
}
