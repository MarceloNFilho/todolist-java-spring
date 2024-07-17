package com.lozzato.todolist.task;

import com.lozzato.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired TaskRepository repository;

    @PostMapping("/")
    public ResponseEntity createTask(@RequestBody Task task, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        task.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())  ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início/término deve ser maior que a data atual.");
        }
        if(task.getStartAt().isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser menor que a data de término.");
        }

        var newTask = this.repository.save(task);

        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }

    @GetMapping("/")
    public List<Task> listTasks(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        return this.repository.findByUserId((UUID) userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@PathVariable UUID id, @RequestBody Task task, HttpServletRequest request ){
        var taskToBeUpdate = this.repository.findById(id).orElseThrow(null);

        if( taskToBeUpdate == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada.");
        }

        var userId = request.getAttribute("userId");
        if(!taskToBeUpdate.getUserId().equals(userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa.");
        }

        Utils.copyNonNullProperties(task, taskToBeUpdate);

        var taskUpdated = this.repository.save(taskToBeUpdate);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
