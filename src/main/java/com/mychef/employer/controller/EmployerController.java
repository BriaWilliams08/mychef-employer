package com.mychef.employer.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mychef.employer.model.Employer;
import com.mychef.employer.service.EmployerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {
    private final EmployerService service;

    public EmployerController(EmployerService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Employer> register(@RequestBody @Valid Employer employer) {
        Employer saved = service.registerEmployer(employer);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employer> getById(@PathVariable Long id) {
        Optional<Employer> employer = service.getEmployerById(id);
        return employer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
