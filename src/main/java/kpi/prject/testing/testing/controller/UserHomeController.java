package kpi.prject.testing.testing.controller;

import kpi.prject.testing.testing.dto.ReportDTO;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.exceptions.InvalidUserException;
import kpi.prject.testing.testing.exceptions.UnknownReportError;
import kpi.prject.testing.testing.exceptions.UnknownUserException;
import kpi.prject.testing.testing.service.OwnerService;
import kpi.prject.testing.testing.service.ReportService;
import kpi.prject.testing.testing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/userHome")
public class UserHomeController {

    private final UserService userService;
    private final ReportService reportService;
    private  final OwnerService ownerService;

    public UserHomeController(UserService userService, ReportService reportService, OwnerService ownerService) {
        this.userService = userService;
        this.reportService = reportService;
        this.ownerService = ownerService;
    }

    @ExceptionHandler(UnknownReportError.class)
    public String handleUnknownReportError() {
        log.warn("Requested invalid report");
        return "redirect:/error";
    }

    @ExceptionHandler(UnknownUserException.class)
    public String handleUnknownUserException() {
        log.warn("Requested unknown user");
        return "redirect:/error";
    }

    @GetMapping("")
    public String getHome(@RequestParam(required = false) String search, Model model, Principal principal, @PageableDefault(sort = {"updated", "id"},
            direction = Sort.Direction.DESC, size = 12) Pageable pageable) throws InvalidUserException {
        User user = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
        String searchString = Optional.ofNullable(search).orElse("");
        model.addAttribute("reports", reportService.getAllByUserForUserTable(user, pageable, searchString));
        return "home/userHome";
    }

    @GetMapping("/add")
    public String getAdd(@ModelAttribute("report") ReportDTO report) {
        return "home/addReport";
    }

    @PostMapping("/add")
    public String postAdd(@ModelAttribute("report") ReportDTO report, Principal principal) throws UnknownUserException {
        User user = userService.getByUsername(principal.getName()).orElseThrow(UnknownUserException::new);
        ownerService.save(reportService.getFromDTO(report), user);
        return "redirect:/userHome";
    }

    @GetMapping(value = "/update/{report_id}")
    public String updateReport(@PathVariable String report_id,
                               Model model) throws UnknownReportError {
        Report report = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        model.addAttribute("report", report);
        return "home/updateReport";
    }

    @PostMapping(value = "/update/{report_id}")
    public String updateReportPost(@ModelAttribute("reportToUpdate") ReportDTO reportToUpdate, @PathVariable String report_id) throws UnknownReportError {
        Report report = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        ownerService.update(report, reportToUpdate);
        return "redirect:/userHome";
    }

    @PostMapping(value = "/change/{report_id}")
    public String changeReportPost(@PathVariable String report_id) throws UnknownReportError {
        Report reportToUpdate = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        ownerService.changeInspector(reportToUpdate);
        return "redirect:/userHome";
    }
}
