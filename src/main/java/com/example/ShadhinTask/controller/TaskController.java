package com.example.ShadhinTask.controller;

import com.example.ShadhinTask.model.Task;
import com.example.ShadhinTask.service.TaskService;
import com.example.ShadhinTask.service.TaskService.TaskStats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService svc;

    public TaskController(TaskService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Task t) {
        try {
            int id = svc.create(t);
            return ResponseEntity.created(URI.create("/tasks/" + id)).body(t);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public List<Task> getAll() {
        return svc.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable int id) {
        return svc.completeTask(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        return svc.deleteTask(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/highest-priority")
    public ResponseEntity<?> highest() {
        return svc.highestPriority()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/stats")
    public TaskStats stats() {
        return svc.stats();
    }
}
