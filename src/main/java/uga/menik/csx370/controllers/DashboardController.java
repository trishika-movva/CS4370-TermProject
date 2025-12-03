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
import uga.menik.csx370.models.ApplicationView;
import uga.menik.csx370.services.ApplicationService;
import uga.menik.csx370.services.CompanyService;
import uga.menik.csx370.services.UserService;

/**
 * Simple dashboard placeholder for authenticated users.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final CompanyService companyService;

    @Autowired
    public DashboardController(UserService userService, ApplicationService applicationService, CompanyService companyService) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.companyService = companyService;
    }

    @GetMapping
    public ModelAndView dashboard(HttpSession session) {
        ModelAndView mv = new ModelAndView("dashboard");
        var user = userService.getLoggedInUser();
        if (user != null) {
            mv.addObject("username", user.getUsername());
            mv.addObject("email", user.getEmail());
        } else if (session.getAttribute("username") != null) {
            mv.addObject("username", session.getAttribute("username").toString());
            mv.addObject("email", session.getAttribute("email"));
        }

        Object uidObj = session.getAttribute("userId");
        if (uidObj != null) {
            try {
                int userId = Integer.parseInt(uidObj.toString());
                mv.addObject("totalApplications", applicationService.countApplicationsForUser(userId));
                mv.addObject("openApplications", applicationService.countOpenApplicationsForUser(userId));
                var companies = companyService.listCompaniesWithCounts(userId);
                mv.addObject("companyCount", companies.size());
                mv.addObject("recentApplications", applicationService.getRecentApplications(userId, 5));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mv;
    }
}
