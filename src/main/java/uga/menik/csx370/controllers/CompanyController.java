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
import uga.menik.csx370.services.CompanyService;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ModelAndView listCompanies(HttpSession session) {
        ModelAndView mv = new ModelAndView("companies_page");
        Object uidObj = session.getAttribute("userId");
        if (uidObj != null) {
            try {
                int userId = Integer.parseInt(uidObj.toString());
                mv.addObject("companies", companyService.listCompaniesWithCounts(userId));
            } catch (Exception e) {
                mv.addObject("errorMessage", "Could not load companies.");
                e.printStackTrace();
            }
        }
        return mv;
    }
}
