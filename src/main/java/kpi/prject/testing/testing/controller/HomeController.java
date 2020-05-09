package kpi.prject.testing.testing.controller;

import kpi.prject.testing.testing.dto.DeclineReasonDTO;
import kpi.prject.testing.testing.dto.ReportDTO;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.entity.enums.Role;
import kpi.prject.testing.testing.exceptions.InvalidUserException;
import kpi.prject.testing.testing.exceptions.UnknownReportError;
import kpi.prject.testing.testing.exceptions.UnknownUserException;
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

@Controller
@Slf4j
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final ReportService reportService;

    public HomeController(UserService userService, ReportService reportService) {
        this.userService = userService;
        this.reportService = reportService;
    }

    @ExceptionHandler(UnknownReportError.class)
    public String handleUnknownReportError() {
        log.warn("Requested invalid report");
        return "redirect:/error";
    }

    @ExceptionHandler(InvalidUserException.class)
    public String handleInvalidUserException() {
        log.warn("Requested invalid user");
        return "redirect:/error";
    }

    @ExceptionHandler(UnknownUserException.class)
    public String handleUnknownUserException() {
        log.warn("Requested unknown user");
        return "redirect:/error";
    }

//    @ExceptionHandler(Exception.class)
//    public String handleAllException() {
//        log.warn("Something went wrong");
//        return "redirect:/error";
//    }

    @GetMapping("")
    public String getHome(@RequestParam(required = false) String search, Model model, Principal principal, @PageableDefault(sort = {"updated", "id"},
            direction = Sort.Direction.DESC, size = 12) Pageable pageable) throws InvalidUserException {

        User user = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
        String searchString = Optional.ofNullable(search).orElse("");
        if (user.getRole() == Role.ROLE_USER) {
            model.addAttribute("reports", reportService.getAllByUserForUserTable(user, pageable, searchString));
            return "home/userHome";
        } else if (user.getRole() == Role.ROLE_INSPECTOR) {
            model.addAttribute("reports", reportService.getAllByInspectorAndStatusForTable(user, ReportStatus.QUEUE, pageable, searchString));
            return "home/inspHome";
        }
        return "redirect:/";
    }

        @GetMapping("/add")
    public String getAdd(@ModelAttribute("report") ReportDTO report) {
        return "home/addReport";
    }

    @PostMapping("/add")
    public String postAdd(@ModelAttribute("report") ReportDTO report, Principal principal) throws UnknownUserException {
        User user = userService.getByUsername(principal.getName()).orElseThrow(UnknownUserException::new);
        reportService.save(reportService.getFromDTO(report), user);
        return "redirect:/home";
    }

    @PostMapping(value = "/accept/{report_id}")
    public String acceptReport(@PathVariable String report_id, Principal principal)
            throws UnknownReportError, InvalidUserException {
        User inspector = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
        Report reportToAccept = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        reportService.acceptReport(reportToAccept, inspector);
        return "redirect:/home";
    }

    @GetMapping(value = "/decline/{report_id}")
    public String declineReport(@ModelAttribute("report_reason") DeclineReasonDTO reportReason,
                                @PathVariable String report_id,
                                Model model) {
        model.addAttribute("report_id", report_id);
        return "home/addReason";
    }

    @PostMapping(value = "/decline/{report_id}")
    public String declineReportPost(@ModelAttribute("report_reason") DeclineReasonDTO reportReason,
                                    @PathVariable String report_id, Principal principal) throws UnknownReportError, InvalidUserException {
        Report reportToDecline = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        User inspector = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
        reportService.declineReport(reportToDecline, reportReason, inspector);
        return "redirect:/home";
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
        reportService.update(report, reportToUpdate);
        return "redirect:/home";
    }

    @PostMapping(value = "/change/{report_id}")
    public String changeReportPost(@PathVariable String report_id) throws UnknownReportError {
        Report reportToUpdate = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        reportService.changeInspector(reportToUpdate);
        return "redirect:/home";
    }
}