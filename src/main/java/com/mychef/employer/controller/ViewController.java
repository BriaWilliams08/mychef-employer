package com.mychef.employer.controller;

import java.util.Optional; // auto dependency injection

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // hold data passed to HTML
import org.springframework.ui.Model; // HTML GET
import org.springframework.web.bind.annotation.GetMapping; // bind forms fields
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mychef.employer.model.Employer;
import com.mychef.employer.model.Review;
import com.mychef.employer.service.EmployerService; //safety
import com.mychef.employer.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {
    @Autowired // auto inject EmployerService
    private EmployerService employerService;


    @GetMapping("/") // root URL /
    public String showLogin(Model model, HttpSession session){ // register only if new
        if( session.getAttribute("employer") != null){
            return "redirect:/result";
        }
        model.addAttribute("employer", new Employer()); // empty Employer obj for ThymeLeaf to bind form fields
        return "login"; // ThymeLeaf HTML file (login.html)
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute Employer employer, Model model, HttpSession session) {
        Optional<Employer> found = employerService.getEmployerByEmail(employer.getEmail());
        Employer current;

        if (found.isPresent()) {
            current = found.get();
        } else {
            current = employerService.registerEmployer(employer);
        }
        session.setAttribute("employer", current);
        model.addAttribute("employer", current);
        return "result";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate(); // clear session
        return "redirect:/"; // takes you back to register.html
    }
    
    @GetMapping("/result")
    public String showDashboard(HttpSession session, Model model) {
        Employer employer = (Employer)session.getAttribute("employer");
        if (employer == null) return "redirect:/";
        model.addAttribute("employer", employer);
        return "result";
    }

    // forgot what this is used for look into!!!!!!!!!!!!!!!!!!!!!!!!
    // THIS NEEDS TO HAVE SESSION
    @GetMapping("/employer/{id}") // GET requests for id
    public String viewEmployer(@PathVariable Long id, Model model){ //gets val from URL & assigns to var id
        Optional<Employer> employer = employerService.getEmployerById(id);
        if (employer.isPresent()){
            model.addAttribute("employer", employer.get());
            return "result";
        } else{
            model.addAttribute("error", "EMployer not found");
            return "error";
        }
    }

    // review views~~~~~~~~~~~~~~~~~~~~~~~
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/submit-review")
    public String showReviewForm(Model model, HttpSession session) {
        Employer employer = (Employer) session.getAttribute("employer");
        if (employer == null){
            return "redirect:/";
        }
        Review review = new Review();
        review.setEmployerName(employer.getFullName()); // auto set employer name in form
        model.addAttribute("review", review);
        return "submit-review"; // HTML form page
    }

    @PostMapping("/submit-review")
    public String handleReviewSubmission(@ModelAttribute Review review, Model model) {
        try{
            Review saved = reviewService.submitReview(review);
            model.addAttribute("review", saved);
            return "review-result"; // confirmation page
        } catch (RuntimeException e){
            model.addAttribute("review", review);
            model.addAttribute("error", "Employer name not found.");
            return "submit-review";
        }
    }

    // tests to make sure submit review works for chef del l8r
    @GetMapping("/test-review-result")
    public String testReviewResult(Model model){
        Review fakeReview = new Review();
        fakeReview.setEmployerName("employerName");
        return "review-result";
    }
}
