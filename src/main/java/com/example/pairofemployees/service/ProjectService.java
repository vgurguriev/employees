package com.example.pairofemployees.service;

import com.example.pairofemployees.entity.Employee;
import com.example.pairofemployees.entity.Project;
import com.example.pairofemployees.entity.Team;
import com.example.pairofemployees.repository.ProjectRepository;
import com.example.pairofemployees.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProjectService {
    private final EmployeeService employeeService;
    private final TeamRepository teamRepository;

    private final ProjectRepository projectRepository;

    private static boolean teamHasFound = false;
    private static boolean projectHasFound = false;
    private static Team foundedTeam;

    public ProjectService(EmployeeService employeeService, TeamRepository teamRepository, ProjectRepository projectRepository) {
        this.employeeService = employeeService;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
    }

    public void findPairs() {
        List<Employee> employees = employeeService.getAllEmployees();

        List<Team> teams = new ArrayList<>();

        for (int i = 0; i < employees.size() - 1; i++) {
            for (int j = i + 1; j < employees.size(); j++) {
                if (employees.get(i).getProjectID() == employees.get(j).getProjectID()
                        && employees.get(i).getEmpId() != employees.get(j).getEmpId()) {
                    Employee first = employees.get(i);
                    Employee second = employees.get(j);


                    if (first.getProjectID() == second.getProjectID() &&
                            hasWorkedTogether(first, second)) {

                        projectHasFound = false;

                        if (!teams.isEmpty()) {
                            teams.forEach(team -> {
                                if (teamPresent(team.getFirstEmployee(), team.getSecondEmployee(),
                                        first.getEmpId(), second.getEmpId())) {
                                    teamHasFound = true;
                                    foundedTeam = team;
                                    team.getProjects()
                                            .forEach(p -> {
                                                if (p.getProjectId() == first.getProjectID()) {
                                                    projectHasFound = true;
                                                    p.setTotalTime(p.getTotalTime() + timeWorkedTogether(first, second));
                                                    teamRepository.save(team);
                                                }
                                            });
                                }
                            });
                        }


                        if (!teamHasFound) {
                            Team team = new Team();
                            team.setFirstEmployee(first.getEmpId());
                            team.setSecondEmployee(second.getEmpId());

                            createProject(teams, first, second, team);

                        } else if (!projectHasFound) {
                            createProject(teams, first, second, foundedTeam);
                        }
                    }
                }
            }
        }
    }

    private void createProject(List<Team> teams, Employee first, Employee second, Team team) {
        Project project = new Project();
        project.setTotalTime(timeWorkedTogether(first, second) + 1);
        project.setProjectId(first.getProjectID());

        List<Project> projects = new ArrayList<>();

        if (!team.getProjects().isEmpty()) {
            projects = team.getProjects();
        }
        team.setProjects(projects);
        project.setTeam(team);
        projects.add(project);
        teams.add(team);
        teamRepository.save(team);
    }

    private boolean teamPresent(long teamFirstId, long teamSecondId, long firstId, long secondId) {
        return teamFirstId == firstId
                && teamSecondId == secondId
                || teamFirstId == secondId
                && teamSecondId == firstId;
    }

    private long timeWorkedTogether(Employee first, Employee second) {
        LocalDate start = first.getDateFrom().isBefore(second.getDateFrom()) ?
                second.getDateFrom() : first.getDateFrom();

        LocalDate end = first.getDateTo().isBefore(second.getDateTo()) ?
                first.getDateTo() : second.getDateTo();

        return Math.abs(ChronoUnit.DAYS.between(start, end));

    }

    private boolean hasWorkedTogether(Employee first, Employee second) {
        return (first.getDateFrom().isBefore(second.getDateTo())
                || first.getDateFrom().isEqual(second.getDateTo()))
                && (first.getDateTo().isAfter(second.getDateFrom())
                || first.getDateTo().isEqual(second.getDateFrom()));

    }

    public Team findProjectWithMostDaysWorked() {
        List<Project> projects = projectRepository.findAllByOrderByTotalTimeDesc();

        Team bestTeam = projects.get(0).getTeam();

        List<Project> bestTeamProjects = bestTeam.getProjects();

        List<Project> sorted = bestTeamProjects
                .stream()
                .sorted(Comparator.comparing(Project::getTotalTime).reversed())
                .toList();

        bestTeam.setProjects(sorted);

        return bestTeam;

    }
}