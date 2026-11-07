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
import java.util.Optional;

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

    public long count() throws SQLException {
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users")) {
            return resultSet.next() ? resultSet.getLong(1) : 0;
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
                users.add(mapUser(resultSet));
            }
        }
        return users;
    }

    public Optional<UserAccount> authenticate(String email, String password) throws SQLException {
        String sql = """
                SELECT u.id, u.full_name, u.email, u.password_hash, u.role, u.organization_id,
                       o.name AS organization_name, u.created_at
                FROM users u
                JOIN organizations o ON o.id = u.organization_id
                WHERE u.email = ? COLLATE NOCASE
                """;
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                String passwordHash = resultSet.getString("password_hash");
                if (!BCrypt.checkpw(password, passwordHash)) {
                    return Optional.empty();
                }
                return Optional.of(mapUser(resultSet));
            }
        }
    }

    public void save(String fullName, String email, String password, String role, long organizationId)
            throws SQLException {
        String sql = """
                INSERT INTO users (full_name, email, password_hash, role, organization_id)
                VALUES (?, ?, ?, ?, ?)
                """;
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fullName.trim());
            statement.setString(2, email.trim());
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

    private UserAccount mapUser(ResultSet resultSet) throws SQLException {
        return new UserAccount(
                resultSet.getLong("id"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getString("role"),
                resultSet.getLong("organization_id"),
                resultSet.getString("organization_name"),
                resultSet.getString("created_at"));
    }

    private Connection openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }
}
