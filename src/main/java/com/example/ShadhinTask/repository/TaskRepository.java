package com.example.ShadhinTask.repository;

import com.example.ShadhinTask.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbc;

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Task> rowMapper = (rs, rowNum) -> {
        Task t = new Task();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setPriority(rs.getInt("priority"));
        t.setCompleted(rs.getBoolean("completed"));
        return t;
    };

    public int create(Task task) {
        String sql = "INSERT INTO tasks (title, description, priority, completed) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setInt(3, task.getPriority());
            ps.setBoolean(4, task.isCompleted());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : -1;
    }

    public List<Task> findAll() {
        return jdbc.query("SELECT * FROM tasks", rowMapper);
    }

    public Optional<Task> findById(int id) {
        List<Task> list = jdbc.query("SELECT * FROM tasks WHERE id = ?", rowMapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public int markCompleted(int id) {
        return jdbc.update("UPDATE tasks SET completed = TRUE WHERE id = ?", id);
    }

    public int deleteById(int id) {
        return jdbc.update("DELETE FROM tasks WHERE id = ?", id);
    }

    public Optional<Task> findHighestPriority() {
        List<Task> list = jdbc.query("SELECT * FROM tasks ORDER BY priority DESC, id ASC LIMIT 1", rowMapper);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public int countAll() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM tasks", Integer.class);
    }

    public int countCompleted() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM tasks WHERE completed = TRUE", Integer.class);
    }

    public int countPending() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM tasks WHERE completed = FALSE", Integer.class);
    }
}
