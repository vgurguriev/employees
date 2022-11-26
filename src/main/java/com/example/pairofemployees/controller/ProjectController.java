package com.example.pairofemployees.controller;

import com.example.pairofemployees.entity.Team;
import com.example.pairofemployees.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {
    private final ProjectService projectService;


    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;

    }

    @GetMapping("/pairs")
    public String getPairs(Model model) {
        projectService.findPairs();
        Team team = projectService.findProjectWithMostDaysWorked();

        model.addAttribute("team", team);

        return "pair";
    }
}
