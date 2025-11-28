// com.dae.kymes.controller.dashboard 패키지 아래
package com.dae.kdmes.controller.Dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class Dashboard {

    // 로그인 후 첫 화면

    // 로그인 후 첫 화면
    @GetMapping("")
    public String cmsDashboard() {
        // templates/dashboard/dashboard_cms.html
        return "dashboard/dashboard_cms";
    }
}
