package kpi.prject.testing.testing.controller;

import kpi.prject.testing.testing.dto.DeclineReasonDTO;
import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.exceptions.InvalidUserException;
import kpi.prject.testing.testing.exceptions.UnknownReportError;
import kpi.prject.testing.testing.service.InspectorService;
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
@RequestMapping("/inspHome")
public class InspHomeController {

    private final UserService userService;
    private final ReportService reportService;
    private final InspectorService inspectorService;

    public InspHomeController(UserService userService, ReportService reportService, InspectorService inspectorService) {
        this.userService = userService;
        this.reportService = reportService;
        this.inspectorService = inspectorService;
    }

    @ExceptionHandler(InvalidUserException.class)
    public String handleInvalidUserException() {
        log.warn("Requested invalid user");
        return "redirect:/error";
    }

    @ExceptionHandler(UnknownReportError.class)
    public String handleUnknownReportError() {
        log.warn("Requested invalid report");
        return "redirect:/error";
    }

    @GetMapping("")
    public String getHome(@RequestParam(required = false) String search, Model model, Principal principal,
                          @PageableDefault(sort = {"updated", "id"}, direction = Sort.Direction.DESC, size = 12) Pageable pageable)
            throws InvalidUserException {
        User user = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
        String searchString = Optional.ofNullable(search).orElse("");
        model.addAttribute("reports", reportService
                .getAllByInspectorAndStatusForTable(user, ReportStatus.QUEUE, pageable, searchString));
        return "home/inspHome";
    }

    @PostMapping(value = "/decline/{report_id}")
    public String declineReportPost(@ModelAttribute("report_reason") DeclineReasonDTO reportReason,
                                    @PathVariable String report_id, Principal principal) throws UnknownReportError, InvalidUserException {
        Report reportToDecline = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        User inspector = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
        inspectorService.declineReport(reportToDecline, reportReason, inspector);
        return "redirect:/inspHome";
    }

    @PostMapping(value = "/accept/{report_id}")
    public String acceptReport(@PathVariable String report_id, Principal principal)
            throws UnknownReportError, InvalidUserException {
        User inspector = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
        Report reportToAccept = reportService.getById(Long.parseLong(report_id)).orElseThrow(UnknownReportError::new);
        inspectorService.acceptReport(reportToAccept, inspector);
        return "redirect:/inspHome";
    }

    @GetMapping(value = "/decline/{report_id}")
    public String declineReport(@ModelAttribute("report_reason") DeclineReasonDTO reportReason,
                                @PathVariable String report_id,
                                Model model) {
        model.addAttribute("report_id", report_id);
        return "home/addReason";
    }
}
