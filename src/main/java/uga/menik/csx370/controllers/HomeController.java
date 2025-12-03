/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

/**
 * This controller handles the home page and some of it's sub URLs.
 */
@Controller
@RequestMapping
public class HomeController {

    /**
     * This function handles the / URL.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error,
                               HttpSession session) {
        return new ModelAndView("redirect:/dashboard");
    }

    /**
     * This function handles post creation form submissions from the home page.
     */
    @PostMapping("/createpost")
    public String createPost(@RequestParam(name = "posttext") String postText,
                           HttpSession session) {
        return "redirect:/dashboard";
    }

}
