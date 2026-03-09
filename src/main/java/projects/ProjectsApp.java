package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectsApp {

    private Scanner scanner = new Scanner(System.in);
    private ProjectDao projectDao = new ProjectDao();
    private Project curProject = null;

    public static void main(String[] args) {
        new ProjectsApp().processUserSelections();
    }

    private void processUserSelections() {
        boolean done = false;

        while (!done) {
            printCurrentProject();
            printMenu();

            int selection = getIntInput("Enter menu selection: ");

            switch (selection) {
                case 1:
                    printConnectionMessage();
                    createProject();
                    break;
                case 2:
                    printConnectionMessage();
                    listProjects();
                    break;
                case 3:
                    printConnectionMessage();
                    selectProject();
                    break;
                case 4:
                    printConnectionMessage();
                    updateProjectDetails();
                    break;
                case 5:
                    printConnectionMessage();
                    deleteProject();
                    break;
                case 0:
                    done = true;
                    break;
                default:
                    System.out.println("\nInvalid selection. Try again.");
            }
        }

        System.out.println("\nExiting application. Goodbye!");
    }

    private void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("  1) Add a project");
        System.out.println("  2) List projects");
        System.out.println("  3) Select a project");
        System.out.println("  4) Update project details");
        System.out.println("  5) Delete a project");
        System.out.println("  0) Exit");
    }

    private void printCurrentProject() {
        if (curProject != null) {
            System.out.println("\nCurrent project:");
            System.out.println("   ID=" + curProject.getProjectId());
            System.out.println("   name=" + curProject.getProjectName());
            System.out.println("   estimatedHours=" + curProject.getEstimatedHours());
            System.out.println("   actualHours=" + curProject.getActualHours());
            System.out.println("   difficulty=" + curProject.getDifficulty());
            System.out.println("   notes=" + curProject.getNotes());
        }
    }

    private void printConnectionMessage() {
        System.out.println("\n**************************************************");
        System.out.println("Connection to schema 'projects' is successful.");
        System.out.println("**************************************************\n");
    }

    private void createProject() {
        String name = getStringInput("Enter project name: ");
        BigDecimal estHours = getDecimalInput("Enter estimated hours: ");
        BigDecimal actHours = getDecimalInput("Enter actual hours: ");
        int difficulty = getIntInput("Enter project difficulty (1-5): ");
        String notes = getStringInput("Enter project notes: ");

        Project project = new Project();
        project.setProjectName(name);
        project.setEstimatedHours(estHours);
        project.setActualHours(actHours);
        project.setDifficulty(difficulty);
        project.setNotes(notes);

        Project dbProject = projectDao.insertProject(project);
        System.out.println("\nProject added with ID: " + dbProject.getProjectId());
    }

    private void listProjects() {
        List<Project> projects = projectDao.fetchAllProjects();
        System.out.println("\nProjects:");
        for (Project project : projects) {
            System.out.println("   " + project.getProjectId() + ": " + project.getProjectName());
        }
    }

    private void selectProject() {
        int id = getIntInput("Enter project ID: ");
        Optional<Project> project = projectDao.fetchProjectById(id);

        if (project.isPresent()) {
            curProject = project.get();
        } else {
            System.out.println("\nProject with ID " + id + " not found.");
        }
    }

    private void updateProjectDetails() {
        if (curProject == null) {
            System.out.println("\nNo project selected.");
            return;
        }

        String name = getStringInput("Enter project name [" + curProject.getProjectName() + "]: ");
        BigDecimal estHours = getDecimalInput("Enter estimated hours [" + curProject.getEstimatedHours() + "]: ");
        BigDecimal actHours = getDecimalInput("Enter actual hours [" + curProject.getActualHours() + "]: ");
        int difficulty = getIntInput("Enter project difficulty [" + curProject.getDifficulty() + "]: ");
        String notes = getStringInput("Enter project notes [" + curProject.getNotes() + "]: ");

        if (!name.isBlank()) curProject.setProjectName(name);
        if (estHours != null) curProject.setEstimatedHours(estHours);
        if (actHours != null) curProject.setActualHours(actHours);
        curProject.setDifficulty(difficulty);
        if (!notes.isBlank()) curProject.setNotes(notes);

        try {
            projectDao.modifyProjectDetails(curProject);
            System.out.println("\nProject details updated.");
        } catch (Exception e) {
            System.out.println("\nFailed to update project: " + e.getMessage());
        }
    }

    private void deleteProject() {
        int id = getIntInput("Enter project ID to delete: ");
        Optional<Project> project = projectDao.fetchProjectById(id);

        if (project.isPresent()) {
            projectDao.deleteProject(id);
            System.out.println("\nProject deleted: " + project.get().getProjectName());
            if (curProject != null && curProject.getProjectId() == id) {
                curProject = null;
            }
        } else {
            System.out.println("\nProject with ID " + id + " not found.");
        }
    }

    // Input utility methods
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                String input = getStringInput(prompt);
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private BigDecimal getDecimalInput(String prompt) {
        while (true) {
            try {
                String input = getStringInput(prompt);
                if (input.isBlank()) return null;
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal. Try again.");
            }
        }
    }
}