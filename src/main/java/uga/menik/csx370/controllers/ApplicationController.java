/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.controllers;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import uga.menik.csx370.services.ApplicationService;
import uga.menik.csx370.models.ApplicationView;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final List<String> statusOptions = Arrays.asList("Applied", "OA", "Interviewing", "Offer", "Rejected");

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/{applicationId}")
    public ModelAndView applicationDetail(HttpSession session, @PathVariable("applicationId") int applicationId) {
        ModelAndView mv = new ModelAndView("application_detail");
        mv.addObject("statusOptions", statusOptions);
        Integer userId = parseUserId(session);
        if (userId == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }
        try {
            ApplicationView detail = applicationService.getApplicationDetail(applicationId, userId);
            if (detail == null) {
                mv.setViewName("redirect:/applications?error=missing");
                return mv;
            }
            mv.addObject("application", detail);
            mv.addObject("rounds", applicationService.getInterviewRounds(applicationId, userId));
            mv.addObject("offers", applicationService.getOffers(applicationId, userId));
        } catch (Exception e) {
            mv.addObject("errorMessage", "Could not load application.");
            e.printStackTrace();
        }
        return mv;
    }

    @GetMapping
    public ModelAndView listApplications(HttpSession session) {
        ModelAndView mv = new ModelAndView("applications_page");
        mv.addObject("statusOptions", statusOptions);

        Object uidObj = session.getAttribute("userId");
        if (uidObj != null) {
            try {
                int userId = Integer.parseInt(uidObj.toString());
                mv.addObject("applications", applicationService.getApplicationsForUser(userId));
            } catch (Exception e) {
                mv.addObject("errorMessage", "Could not load applications.");
                e.printStackTrace();
            }
        }
        return mv;
    }

    @PostMapping
    public String createApplication(
            HttpSession session,
            @RequestParam("companyName") String companyName,
            @RequestParam(value = "industry", required = false) String industry,
            @RequestParam(value = "hqLocation", required = false) String hqLocation,
            @RequestParam(value = "website", required = false) String website,
            @RequestParam("title") String title,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "employmentType", required = false) String employmentType,
            @RequestParam(value = "jobLevel", required = false) String jobLevel,
            @RequestParam(value = "season", required = false) String season,
            @RequestParam(value = "appliedDate", required = false) String appliedDate,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "source", required = false) String source,
            @RequestParam(value = "resumeVersion", required = false) String resumeVersion,
            @RequestParam(value = "notes", required = false) String notes) {

        Integer userId = parseUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            Date applied = (appliedDate != null && !appliedDate.isBlank()) ? Date.valueOf(appliedDate) : null;
            applicationService.createApplication(userId, companyName, industry, hqLocation, website, title, location,
                    employmentType, jobLevel, season, applied, status, source, resumeVersion, notes);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/applications?error=true";
        }
        return "redirect:/applications";
    }

    @PostMapping("/{applicationId}/status")
    public String updateStatus(HttpSession session,
                            @PathVariable("applicationId") int applicationId,
                            @RequestParam("status") String status,
                            @RequestParam(value = "notes", required = false) String notes) {
        Integer userId = parseUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            applicationService.updateApplicationStatus(applicationId, userId, status, notes);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/applications?error=true";
        }
        return "redirect:/applications";
    }

    @PostMapping("/{applicationId}/delete")
    public String deleteApplication(HttpSession session,
                                    @PathVariable("applicationId") int applicationId) {
        Integer userId = parseUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            applicationService.deleteApplication(applicationId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/applications?error=true";
        }
        return "redirect:/applications";
    }

    @PostMapping("/{applicationId}/rounds")
    public String addInterviewRound(HttpSession session,
                                    @PathVariable("applicationId") int applicationId,
                                    @RequestParam("roundType") String roundType,
                                    @RequestParam(value = "scheduledDate", required = false) String scheduledDate,
                                    @RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "feedback", required = false) String feedback) {
        Integer userId = parseUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            Date scheduled = (scheduledDate != null && !scheduledDate.isBlank()) ? Date.valueOf(scheduledDate) : null;
            applicationService.addInterviewRound(applicationId, userId, roundType, scheduled, status, feedback);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/applications/" + applicationId + "?error=true";
        }
        return "redirect:/applications/" + applicationId;
    }

    @PostMapping("/{applicationId}/offers")
    public String addOffer(HttpSession session,
                           @PathVariable("applicationId") int applicationId,
                           @RequestParam(value = "compensation", required = false) String compensation,
                           @RequestParam(value = "startDate", required = false) String startDate,
                           @RequestParam(value = "decisionDeadline", required = false) String decisionDeadline,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "notes", required = false) String notes) {
        Integer userId = parseUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            Date start = (startDate != null && !startDate.isBlank()) ? Date.valueOf(startDate) : null;
            Date deadline = (decisionDeadline != null && !decisionDeadline.isBlank()) ? Date.valueOf(decisionDeadline) : null;
            applicationService.addOffer(applicationId, userId, compensation, start, deadline, status, notes);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/applications/" + applicationId + "?error=true";
        }
        return "redirect:/applications/" + applicationId;
    }

    private Integer parseUserId(HttpSession session) {
        Object uidObj = session.getAttribute("userId");
        if (uidObj == null) {
            return null;
        }
        try {
            return Integer.parseInt(uidObj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
