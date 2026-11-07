package com.cyberaudit;

import com.cyberaudit.domain.Organization;
import com.cyberaudit.domain.UserAccount;
import com.cyberaudit.repository.OrganizationRepository;
import com.cyberaudit.repository.UserRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class CyberAuditApplication extends Application {

    private final OrganizationRepository organizationRepository = new OrganizationRepository();
    private final UserRepository userRepository = new UserRepository();
    private final ObservableList<Organization> organizations = FXCollections.observableArrayList();
    private final ObservableList<UserAccount> users = FXCollections.observableArrayList();
    private TableView<Organization> organizationTable;
    private TableView<UserAccount> userTable;

    @Override
    public void start(Stage stage) {
        Label title = new Label("CyberAudit");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #a78bfa;");
        Label subtitle = new Label("Gestão local de organizações e utilizadores");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        TabPane tabs = new TabPane(createOrganizationsTab(), createUsersTab());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Label note = new Label("Os dados são guardados localmente em data/cyberaudit.db.");
        note.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");

        VBox content = new VBox(16, new VBox(4, title, subtitle), tabs, note);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: #0f172a;");
        VBox.setVgrow(tabs, Priority.ALWAYS);

        Scene scene = new Scene(content, 1000, 680);
        stage.setTitle("CyberAudit — Aplicação de Auditoria de Cibersegurança");
        stage.setMinWidth(820);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();

        refreshOrganizations();
        refreshUsers();
    }

    private Tab createOrganizationsTab() {
        organizationTable = new TableView<>(organizations);
        TableColumn<Organization, String> nameColumn = new TableColumn<>("Organização");
        nameColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().name()));
        nameColumn.setPrefWidth(270);
        TableColumn<Organization, String> sectorColumn = new TableColumn<>("Setor");
        sectorColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().sector()));
        sectorColumn.setPrefWidth(240);
        TableColumn<Organization, String> statusColumn = new TableColumn<>("Estado");
        statusColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().status()));
        statusColumn.setPrefWidth(140);
        TableColumn<Organization, String> dateColumn = new TableColumn<>("Criada em");
        dateColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().createdAt()));
        dateColumn.setPrefWidth(190);
        organizationTable.getColumns().addAll(nameColumn, sectorColumn, statusColumn, dateColumn);

        TextField nameField = new TextField();
        nameField.setPromptText("Nome da organização");
        TextField sectorField = new TextField();
        sectorField.setPromptText("Setor");
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("Ativa", "Em revisão", "Inativa");
        statusField.setValue("Ativa");
        Button addButton = new Button("Adicionar organização");
        addButton.setOnAction(event -> addOrganization(nameField, sectorField, statusField));
        Button deleteButton = new Button("Eliminar selecionada");
        deleteButton.setOnAction(event -> deleteSelectedOrganization());

        HBox form = new HBox(10, nameField, sectorField, statusField, addButton);
        HBox.setHgrow(nameField, Priority.ALWAYS);
        HBox.setHgrow(sectorField, Priority.ALWAYS);
        VBox box = new VBox(14, form, organizationTable, deleteButton);
        box.setPadding(new Insets(16));
        VBox.setVgrow(organizationTable, Priority.ALWAYS);
        return new Tab("Organizações", box);
    }

    private Tab createUsersTab() {
        userTable = new TableView<>(users);
        TableColumn<UserAccount, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().fullName()));
        nameColumn.setPrefWidth(190);
        TableColumn<UserAccount, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().email()));
        emailColumn.setPrefWidth(220);
        TableColumn<UserAccount, String> roleColumn = new TableColumn<>("Função");
        roleColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().role()));
        roleColumn.setPrefWidth(130);
        TableColumn<UserAccount, String> organizationColumn = new TableColumn<>("Organização");
        organizationColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().organizationName()));
        organizationColumn.setPrefWidth(210);
        TableColumn<UserAccount, String> dateColumn = new TableColumn<>("Criado em");
        dateColumn.setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().createdAt()));
        dateColumn.setPrefWidth(180);
        userTable.getColumns().addAll(nameColumn, emailColumn, roleColumn, organizationColumn, dateColumn);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Nome completo");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Palavra-passe (mínimo 8 caracteres)");
        ComboBox<String> roleField = new ComboBox<>();
        roleField.getItems().addAll("ADMIN", "ANALISTA", "LEITOR");
        roleField.setValue("ANALISTA");
        ComboBox<String> organizationField = new ComboBox<>();
        organizationField.setPromptText("Organização");
        Button addButton = new Button("Criar utilizador");
        addButton.setOnAction(event -> addUser(fullNameField, emailField, passwordField, roleField, organizationField));
        Button deleteButton = new Button("Eliminar selecionado");
        deleteButton.setOnAction(event -> deleteSelectedUser());

        HBox form = new HBox(10, fullNameField, emailField, passwordField, roleField, organizationField, addButton);
        HBox.setHgrow(fullNameField, Priority.ALWAYS);
        HBox.setHgrow(emailField, Priority.ALWAYS);
        VBox box = new VBox(14, form, userTable, deleteButton);
        box.setPadding(new Insets(16));
        VBox.setVgrow(userTable, Priority.ALWAYS);
        return new Tab("Utilizadores", box);
    }

    private void addOrganization(TextField name, TextField sector, ComboBox<String> status) {
        String orgName = name.getText().trim();
        String orgSector = sector.getText().trim();
        if (orgName.isEmpty() || orgSector.isEmpty()) {
            showMessage(Alert.AlertType.WARNING, "Preenche o nome e o setor da organização.");
            return;
        }
        try {
            organizationRepository.save(new Organization(null, orgName, orgSector, status.getValue(), ""));
            name.clear();
            sector.clear();
            refreshOrganizations();
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível guardar a organização: " + exception.getMessage());
        }
    }

    private void addUser(TextField fullName, TextField email, PasswordField password,
                         ComboBox<String> role, ComboBox<String> organization) {
        String nameValue = fullName.getText().trim();
        String emailValue = email.getText().trim();
        String passwordValue = password.getText();
        String organizationName = organization.getValue();
        if (nameValue.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty() || organizationName == null) {
            showMessage(Alert.AlertType.WARNING, "Preenche todos os campos e escolhe uma organização.");
            return;
        }
        if (!emailValue.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            showMessage(Alert.AlertType.WARNING, "Introduz um endereço de email válido.");
            return;
        }
        if (passwordValue.length() < 8) {
            showMessage(Alert.AlertType.WARNING, "A palavra-passe deve ter pelo menos 8 caracteres.");
            return;
        }
        Organization selectedOrganization = organizations.stream()
                .filter(item -> item.name().equals(organizationName))
                .findFirst().orElse(null);
        if (selectedOrganization == null) {
            showMessage(Alert.AlertType.WARNING, "A organização selecionada já não existe. Atualiza a lista.");
            return;
        }
        try {
            userRepository.save(nameValue, emailValue, passwordValue, role.getValue(), selectedOrganization.id());
            fullName.clear();
            email.clear();
            password.clear();
            role.setValue("ANALISTA");
            refreshUsers();
            showMessage(Alert.AlertType.INFORMATION, "Utilizador criado. A palavra-passe foi guardada de forma segura.");
        } catch (SQLException exception) {
            if (exception.getMessage() != null && exception.getMessage().toLowerCase().contains("unique")) {
                showMessage(Alert.AlertType.WARNING, "Já existe um utilizador com esse email.");
            } else {
                showMessage(Alert.AlertType.ERROR, "Não foi possível criar o utilizador: " + exception.getMessage());
            }
        }
    }

    private void deleteSelectedOrganization() {
        Organization selected = organizationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage(Alert.AlertType.INFORMATION, "Seleciona uma organização primeiro.");
            return;
        }
        try {
            organizationRepository.deleteById(selected.id());
            refreshOrganizations();
            refreshUsers();
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível eliminar a organização: " + exception.getMessage());
        }
    }

    private void deleteSelectedUser() {
        UserAccount selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage(Alert.AlertType.INFORMATION, "Seleciona um utilizador primeiro.");
            return;
        }
        try {
            userRepository.deleteById(selected.id());
            refreshUsers();
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível eliminar o utilizador: " + exception.getMessage());
        }
    }

    private void refreshOrganizations() {
        try {
            organizations.setAll(organizationRepository.findAll());
            if (userTable != null) {
                @SuppressWarnings("unchecked")
                ComboBox<String> organizationField = (ComboBox<String>) userTable.getProperties().get("organizationField");
                if (organizationField != null) {
                    organizationField.getItems().setAll(organizations.stream().map(Organization::name).toList());
                }
            }
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível carregar as organizações: " + exception.getMessage());
        }
    }

    private void refreshUsers() {
        try {
            users.setAll(userRepository.findAll());
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível carregar os utilizadores: " + exception.getMessage());
        }
    }

    private void showMessage(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.setTitle("CyberAudit");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
