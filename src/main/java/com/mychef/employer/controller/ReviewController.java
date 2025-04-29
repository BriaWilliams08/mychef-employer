package com.mychef.employer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mychef.employer.model.Review;
import com.mychef.employer.service.ReviewService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping("/submit")
    public ResponseEntity<Review> submitReview(@RequestBody @Valid Review review) {
        Review saved = service.submitReview(review);
        return ResponseEntity.ok(saved);
    }
}
