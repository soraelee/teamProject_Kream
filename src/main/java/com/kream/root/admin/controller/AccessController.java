package com.kream.root.admin.controller;


import com.kream.root.admin.domain.UserAccessLog;
import com.kream.root.admin.domain.UserAccessLogDTO.GraphDataDTO;
import com.kream.root.admin.service.userAccess.UserAccessLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/Access")
public class AccessController {

    @Autowired
    private UserAccessLogService userAccessLogService;

    @PostMapping("/logUserAccess")
    public void logUserAccess(HttpServletRequest request, @RequestBody UserAccessLog clientLog) {
        String refererUrl = request.getHeader("Referer");
        String userAgent = request.getHeader("User-Agent");

        userAccessLogService.logUserAccess(clientLog, refererUrl, userAgent);
    }
    @GetMapping("/os")
    public List<GraphDataDTO> getOsGraphData() {
        return userAccessLogService.getGraphDataByOs();
    }

    @GetMapping("/browser")
    public List<GraphDataDTO> getBrowserGraphData() {
        return userAccessLogService.getGraphDataByBrowser();
    }

    @GetMapping("/deviceType")
    public List<GraphDataDTO> getDeviceTypeGraphData() {
        return userAccessLogService.getGraphDataByDeviceType();
    }

    @GetMapping("/referer")
    public List<GraphDataDTO> getRefererGraphData() {
        return userAccessLogService.getGraphDataByRefererUrl();
    }

}
