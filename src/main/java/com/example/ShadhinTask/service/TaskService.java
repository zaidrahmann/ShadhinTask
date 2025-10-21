package com.example.ShadhinTask.service;

import com.example.ShadhinTask.model.Task;
import com.example.ShadhinTask.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public int create(Task t) {
        if (t.getPriority() < 1 || t.getPriority() > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
        return repo.create(t);
    }

    public List<Task> getAll() { return repo.findAll(); }

    public Optional<Task> getById(int id) { return repo.findById(id); }

    public boolean completeTask(int id) {
        return repo.markCompleted(id) > 0;
    }

    public boolean deleteTask(int id) {
        return repo.deleteById(id) > 0;
    }

    public Optional<Task> highestPriority() {
        return repo.findHighestPriority();
    }

    public TaskStats stats() {
        return new TaskStats(repo.countAll(), repo.countCompleted(), repo.countPending());
    }

    public static class TaskStats {
        public final int total;
        public final int completed;
        public final int pending;

        public TaskStats(int total, int completed, int pending) {
            this.total = total;
            this.completed = completed;
            this.pending = pending;
        }
    }
}
