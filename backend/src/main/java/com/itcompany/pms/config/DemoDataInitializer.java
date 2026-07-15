package com.itcompany.pms.config;

import com.itcompany.pms.entity.Project;
import com.itcompany.pms.entity.ProjectMember;
import com.itcompany.pms.entity.ProjectStatus;
import com.itcompany.pms.entity.Role;
import com.itcompany.pms.entity.Task;
import com.itcompany.pms.entity.TaskStatus;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.repository.ProjectMemberRepository;
import com.itcompany.pms.repository.ProjectRepository;
import com.itcompany.pms.repository.TaskRepository;
import com.itcompany.pms.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DemoDataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataInitializer(UserRepository userRepository,
                               ProjectRepository projectRepository,
                               ProjectMemberRepository projectMemberRepository,
                               TaskRepository taskRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = createUser("admin", "admin123", "admin@itcompany.com", Role.ADMIN);
        User leiter1 = createUser("leiter1", "leiter123", "leiter1@itcompany.com", Role.PROJEKTLEITER);
        User leiter2 = createUser("leiter2", "leiter123", "leiter2@itcompany.com", Role.PROJEKTLEITER);
        User mitarbeiter1 = createUser("mitarbeiter1", "mit123", "mit1@itcompany.com", Role.MITARBEITER);
        User mitarbeiter2 = createUser("mitarbeiter2", "mit123", "mit2@itcompany.com", Role.MITARBEITER);
        User mitarbeiter3 = createUser("mitarbeiter3", "mit123", "mit3@itcompany.com", Role.MITARBEITER);

        Project website = createProject("Website Redesign", "Modernisierung der Unternehmenswebsite", leiter1, ProjectStatus.AKTIV);
        Project mobile = createProject("Mobile App Dev", "Entwicklung einer neuen mobilen Anwendung", leiter1, ProjectStatus.AKTIV);
        Project migration = createProject("Legacy System Migration", "Migration des alten Systems", leiter2, ProjectStatus.AKTIV);
        createProject("Archiviertes Projekt", "Ein bereits abgeschlossenes Projekt", leiter1, ProjectStatus.ARCHIVIERT);

        createMember(website, mitarbeiter1);
        createMember(website, mitarbeiter2);
        createMember(mobile, mitarbeiter1);
        createMember(migration, mitarbeiter3);

        createTask("Homepage erstellen", "Designen und implementieren der Homepage", website, leiter1, mitarbeiter1, TaskStatus.ERLEDIGT);
        createTask("CSS-Styling", "Komplettes Styling durchführen", website, leiter1, mitarbeiter2, TaskStatus.IN_BEARBEITUNG);
        createTask("Testing durchführen", "QA und Testing der Website", website, leiter1, mitarbeiter1, TaskStatus.OFFEN);
        createTask("API-Design", "REST-API für Mobile App entwerfen", mobile, leiter1, mitarbeiter1, TaskStatus.IN_BEARBEITUNG);
        createTask("Backend-Implementierung", "Server-Logik implementieren", mobile, leiter1, null, TaskStatus.OFFEN);
        createTask("Datenmigration", "Alte Daten ins neue System migrieren", migration, leiter2, mitarbeiter3, TaskStatus.OFFEN);
    }

    private User createUser(String username, String password, String email, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);
        user.setActive(true);
        return userRepository.save(user);
    }

    private Project createProject(String name, String description, User manager, ProjectStatus status) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setProjectManager(manager);
        project.setStatus(status);
        return projectRepository.save(project);
    }

    private void createMember(Project project, User user) {
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        projectMemberRepository.save(member);
    }

    private void createTask(String title,
                            String description,
                            Project project,
                            User createdBy,
                            User assignedTo,
                            TaskStatus status) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setProject(project);
        task.setCreatedBy(createdBy);
        task.setAssignedTo(assignedTo);
        task.setStatus(status);
        taskRepository.save(task);
    }
}
