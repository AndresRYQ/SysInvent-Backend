package com.agrihusac.SysInvent.api.dashboard;

import com.agrihusac.SysInvent.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Proposed route: GET /api/dashboard. The placeholder returns HTTP 501. */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/_plantilla")
    public void plantilla() {
        dashboardService.operacionPendiente();
    }
}
