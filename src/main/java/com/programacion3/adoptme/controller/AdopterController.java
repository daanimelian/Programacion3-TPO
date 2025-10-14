package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.domain.Adopter;
import com.programacion3.adoptme.repo.AdopterRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/adopters")
public class AdopterController {
    private final AdopterRepository repo;
    public AdopterController(AdopterRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Adopter> all() { return repo.findAll(); }
}
