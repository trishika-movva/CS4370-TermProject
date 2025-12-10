/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import uga.menik.csx370.services.ApplicationService;

/**
 * Reports/summary page with status counts, interviews, and offers.
 */
@Controller
@RequestMapping("/reports")
public class ReportsController {

    private final ApplicationService applicationService;

    @Autowired
    public ReportsController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ModelAndView reports(HttpSession session) {
        ModelAndView mv = new ModelAndView("reports_page");
        Object uidObj = session.getAttribute("userId");
        if (uidObj == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }
        try {
            int userId = Integer.parseInt(uidObj.toString());
            mv.addObject("statusSummary", applicationService.getStatusSummary(userId));
            mv.addObject("upcomingInterviews", applicationService.getUpcomingInterviews(userId, 10));
            mv.addObject("recentOffers", applicationService.getRecentOffers(userId, 10));
        } catch (Exception e) {
            mv.addObject("errorMessage", "Could not load reports.");
            e.printStackTrace();
        }
        return mv;
    }
}
