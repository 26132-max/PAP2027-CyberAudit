package com.cyberaudit.repository;

import com.cyberaudit.domain.Organization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrganizationRepository {

    private static final String DATABASE_URL = "jdbc:sqlite:data/cyberaudit.db";

    public OrganizationRepository() {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Path.of("data"));
            try (Connection connection = openConnection(); Statement statement = connection.createStatement()) {
                statement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS organizations (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            sector TEXT NOT NULL,
                            status TEXT NOT NULL,
                            created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))
                        )
                        """);
            }
        } catch (SQLException | java.io.IOException exception) {
            throw new IllegalStateException("Não foi possível inicializar a base de dados local.", exception);
        }
    }

    public List<Organization> findAll() throws SQLException {
        List<Organization> organizations = new ArrayList<>();
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, name, sector, status, created_at FROM organizations ORDER BY id DESC");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                organizations.add(new Organization(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("sector"),
                        resultSet.getString("status"),
                        resultSet.getString("created_at")));
            }
        }
        return organizations;
    }

    public void save(Organization organization) throws SQLException {
        String sql = "INSERT INTO organizations (name, sector, status) VALUES (?, ?, ?)";
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, organization.name());
            statement.setString(2, organization.sector());
            statement.setString(3, organization.status());
            statement.executeUpdate();
        }
    }

    public void deleteById(Long id) throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM organizations WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
