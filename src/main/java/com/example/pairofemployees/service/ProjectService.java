package com.example.pairofemployees.service;

import com.example.pairofemployees.entity.Employee;
import com.example.pairofemployees.entity.Project;
import com.example.pairofemployees.entity.Team;
import com.example.pairofemployees.repository.ProjectRepository;
import com.example.pairofemployees.repository.TeamRepository;
import com.sun.xml.bind.v2.TODO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public void generateTeams() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<Team> teams = new ArrayList<>();
        Map<String, Integer> teamMap = new HashMap<>();

        for (int i = 0; i < employees.size() - 1; i++) {
            for (int j = i + 1; j < employees.size(); j++) {
                if (employees.get(i).getProjectID() == employees.get(j).getProjectID()
                        && employees.get(i).getEmployeeId() != employees.get(j).getEmployeeId()) {
                    Employee first = employees.get(i);
                    Employee second = employees.get(j);

//                  TODO: move hasWorkedTogether() in previous if statement
                    if (first.getProjectID() == second.getProjectID() &&
                            hasWorkedTogether(first, second)) {

                        projectHasFound = false;

//                      TODO: replace forEach with Map
                        if (!teams.isEmpty()) {
                            teams.forEach(team -> {
                                if (doesTeamExist(team.getFirstEmployee(), team.getSecondEmployee(),
                                        first.getEmployeeId(), second.getEmployeeId())) {
                                    teamHasFound = true;
                                    foundedTeam = team;
                                    team.getProjects()
                                            .forEach(p -> {
                                                if (p.getProjectId() == first.getProjectID()) {
                                                    projectHasFound = true;
                                                    p.setTotalTime(p.getTotalTime() + getTimeWorkedTogether(first, second));
                                                    teamRepository.save(team);
                                                }
                                            });
                                }
                            });
                        }

                        if (!teamHasFound) {
                            Team team = new Team();
                            team.setFirstEmployee(first.getEmployeeId());
                            team.setSecondEmployee(second.getEmployeeId());

                            createProject(teams, first, second, team);

                        } else if (!projectHasFound) {
                            createProject(teams, first, second, foundedTeam);
                        }
                    }
                }
            }
        }
    }

    public Team findProjectWithMostDaysWorked() {
        Team bestTeam = projectRepository
                .findFirstByOrderByTotalTimeDesc()
                .map(Project::getTeam)
                .orElse(null);

        if (Objects.isNull(bestTeam)) {
            return null;
        }

        List<Project> bestTeamProjects = bestTeam.getProjects();
        List<Project> sorted = bestTeamProjects.stream()
                .sorted(Comparator.comparing(Project::getTotalTime).reversed())
                .toList();

        bestTeam.setProjects(sorted);

        return bestTeam;
    }

    private void createProject(List<Team> teams, Employee first, Employee second, Team team) {
        Project project = new Project();
        project.setTotalTime(getTimeWorkedTogether(first, second) + 1);
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

    private boolean doesTeamExist(long teamFirstId, long teamSecondId, long firstId, long secondId) {
        return teamFirstId == firstId
                && teamSecondId == secondId
                || teamFirstId == secondId
                && teamSecondId == firstId;
    }

    private long getTimeWorkedTogether(Employee first, Employee second) {
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
}
