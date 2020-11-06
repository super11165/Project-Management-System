package com.xx.pmsystem.services;

import com.xx.pmsystem.domain.Backlog;
import com.xx.pmsystem.domain.Project;
import com.xx.pmsystem.domain.ProjectTask;
import com.xx.pmsystem.exceptions.ProjectNotFoundException;
import com.xx.pmsystem.repositories.BacklogRepository;
import com.xx.pmsystem.repositories.ProjectRepository;
import com.xx.pmsystem.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){



        //Pts to be add to a specific project, project != null, bl exist
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
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

    public Iterable<ProjectTask> findBacklogById(String id, String username){
        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username){
        projectService.findProjectByIdentifier(backlog_id, username);

           ProjectTask projectTask = projectTaskRepository.findProjectTasksByProjectSequence(pt_id);
        if(projectTask == null) {
            throw new ProjectNotFoundException("project task '" + pt_id +"' not found!");
        }
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("project task '" + pt_id +"' does not exist in project '" + backlog_id + "'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTask = updatedTask;
        return projectTaskRepository.save((projectTask));
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete((projectTask));

    }

}
