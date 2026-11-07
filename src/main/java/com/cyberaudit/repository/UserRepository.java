package com.cyberaudit.repository;

import com.cyberaudit.domain.UserAccount;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String DATABASE_URL = "jdbc:sqlite:data/cyberaudit.db";

    public UserRepository() {
        try (Connection connection = openConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        full_name TEXT NOT NULL,
                        email TEXT NOT NULL COLLATE NOCASE UNIQUE,
                        password_hash TEXT NOT NULL,
                        role TEXT NOT NULL,
                        organization_id INTEGER NOT NULL,
                        created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
                        FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
                    )
                    """);
        } catch (SQLException exception) {
            throw new IllegalStateException("Não foi possível inicializar a tabela de utilizadores.", exception);
        }
    }

    public List<UserAccount> findAll() throws SQLException {
        List<UserAccount> users = new ArrayList<>();
        String sql = """
                SELECT u.id, u.full_name, u.email, u.role, u.organization_id,
                       o.name AS organization_name, u.created_at
                FROM users u
                JOIN organizations o ON o.id = u.organization_id
                ORDER BY u.full_name COLLATE NOCASE
                """;
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(new UserAccount(
                        resultSet.getLong("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getLong("organization_id"),
                        resultSet.getString("organization_name"),
                        resultSet.getString("created_at")));
            }
        }
        return users;
    }

    public void save(String fullName, String email, String password, String role, long organizationId)
            throws SQLException {
        String sql = """
                INSERT INTO users (full_name, email, password_hash, role, organization_id)
                VALUES (?, ?, ?, ?, ?)
                """;
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setString(4, role);
            statement.setLong(5, organizationId);
            statement.executeUpdate();
        }
    }

    public void deleteById(long id) throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Connection openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }
}
