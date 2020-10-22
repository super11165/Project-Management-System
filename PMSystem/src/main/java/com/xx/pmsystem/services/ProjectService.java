package com.xx.pmsystem.services;

import com.xx.pmsystem.domain.Project;
import com.xx.pmsystem.exceptions.ProjectIdException;
import com.xx.pmsystem.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
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
