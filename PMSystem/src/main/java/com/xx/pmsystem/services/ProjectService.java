package com.xx.pmsystem.services;

import com.xx.pmsystem.domain.Backlog;
import com.xx.pmsystem.domain.Project;
import com.xx.pmsystem.domain.User;
import com.xx.pmsystem.exceptions.ProjectIdException;
import com.xx.pmsystem.repositories.BacklogRepository;
import com.xx.pmsystem.repositories.ProjectRepository;
import com.xx.pmsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists!");
        }
    }

    public Project findProjectByIdentifier(String projectId){

        Project project = projectRepository.findProjectByProjectIdentifier(projectId.toUpperCase());
        if(project == null) {
            throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' didn't exists!");
        }
        return project;

    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findProjectByProjectIdentifier(projectId);

        if(project == null){
            throw new ProjectIdException("Cannot delete Project with ID '" + projectId.toUpperCase() + "'. This project didn't exists!");
        }
        projectRepository.delete(project);
    }


}
