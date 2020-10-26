package com.xx.pmsystem.services;

import com.xx.pmsystem.domain.Backlog;
import com.xx.pmsystem.domain.ProjectTask;
import com.xx.pmsystem.repositories.BacklogRepository;
import com.xx.pmsystem.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //Pts to be add to a specific project, project != null, bl exist
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set bl to PT
        projectTask.setBacklog(backlog);
        //set project sequence-unique increase
        Integer BackLogSequence = backlog.getPTSequence();
        BackLogSequence++;
        backlog.setPTSequence(BackLogSequence);
        //add dequence to project task
        projectTask.setProjectSequence(projectIdentifier+"-"+BackLogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);
        //initial priority when priority null
        if(projectTask.getPriority() == null || projectTask.getPriority() == 0){
            projectTask.setPriority(3);
        }

        //initial status when status is null
        if(projectTask.getStatus() == null || projectTask.getStatus() ==""){
            projectTask.setStatus("TO-DO");
        }
        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id){
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

}
