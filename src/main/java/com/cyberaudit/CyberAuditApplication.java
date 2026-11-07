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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CyberAuditApplication extends Application {

    private final OrganizationRepository organizationRepository = new OrganizationRepository();
    private final UserRepository userRepository = new UserRepository();
    private final ObservableList<Organization> organizations = FXCollections.observableArrayList();
    private final ObservableList<UserAccount> users = FXCollections.observableArrayList();
    private Stage stage;
    private UserAccount currentUser;
    private TableView<Organization> organizationTable;
    private TableView<UserAccount> userTable;
    private ComboBox<Organization> userOrganizationField;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("CyberAudit");
        stage.setMinWidth(820);
        stage.setMinHeight(560);
        showLoginOrFirstRun();
        stage.show();
    }

    private void showLoginOrFirstRun() {
        try {
            if (userRepository.count() == 0) {
                showFirstRunSetup();
            } else {
                showLogin();
            }
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível verificar os utilizadores guardados.");
        }
    }

    private void showLogin() {
        Label title = heading("Iniciar sessão");
        Label subtitle = new Label("Entra na tua conta CyberAudit.");
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Palavra-passe");
        Label error = new Label();
        error.setStyle("-fx-text-fill: #f87171;");
        Button loginButton = new Button("Entrar");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event -> {
            if (email.getText().isBlank() || password.getText().isBlank()) {
                error.setText("Introduz o email e a palavra-passe.");
                return;
            }
            try {
                Optional<UserAccount> account = userRepository.authenticate(email.getText(), password.getText());
                if (account.isEmpty()) {
                    error.setText("Email ou palavra-passe incorretos.");
                    password.clear();
                    return;
                }
                currentUser = account.get();
                showMainScreen();
            } catch (SQLException exception) {
                error.setText("Não foi possível iniciar sessão. Tenta novamente.");
            }
        });

        VBox card = new VBox(14, title, subtitle, email, password, error, loginButton);
        card.setPadding(new Insets(28));
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 12px;");
        VBox root = new VBox(card);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #0f172a;");
        root.setAlignment(javafx.geometry.Pos.CENTER);
        stage.setScene(new Scene(root, 900, 600));
    }

    private void showFirstRunSetup() {
        Label title = heading("Configuração inicial");
        Label subtitle = new Label("Cria a organização e a conta de administrador para começar.");
        TextField organizationName = new TextField();
        organizationName.setPromptText("Nome da organização");
        TextField sector = new TextField();
        sector.setPromptText("Setor da organização");
        TextField fullName = new TextField();
        fullName.setPromptText("Nome do administrador");
        TextField email = new TextField();
        email.setPromptText("Email do administrador");
        PasswordField password = new PasswordField();
        password.setPromptText("Palavra-passe (mínimo 12 caracteres)");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirmar palavra-passe");
        Label error = new Label();
        error.setStyle("-fx-text-fill: #f87171;");
        Button createButton = new Button("Criar conta de administrador");
        createButton.setDefaultButton(true);
        createButton.setOnAction(event -> {
            String orgName = organizationName.getText().trim();
            String orgSector = sector.getText().trim();
            String nameValue = fullName.getText().trim();
            String emailValue = email.getText().trim();
            if (orgName.isEmpty() || orgSector.isEmpty() || nameValue.isEmpty() || emailValue.isEmpty()
                    || password.getText().isEmpty()) {
                error.setText("Preenche todos os campos.");
                return;
            }
            if (!isValidEmail(emailValue)) {
                error.setText("Introduz um endereço de email válido.");
                return;
            }
            if (password.getText().length() < 12) {
                error.setText("A palavra-passe deve ter pelo menos 12 caracteres.");
                return;
            }
            if (!password.getText().equals(confirmPassword.getText())) {
                error.setText("As palavras-passe não coincidem.");
                return;
            }
            try {
                organizationRepository.save(new Organization(null, orgName, orgSector, "Ativa", ""));
                Organization organization = organizationRepository.findAll().stream()
                        .filter(item -> item.name().equalsIgnoreCase(orgName))
                        .findFirst().orElseThrow();
                userRepository.save(nameValue, emailValue, password.getText(), "ADMIN", organization.id());
                currentUser = userRepository.authenticate(emailValue, password.getText()).orElseThrow();
                showMainScreen();
            } catch (SQLException exception) {
                error.setText("Não foi possível concluir a configuração. Verifica se o email já está registado.");
            }
        });

        VBox card = new VBox(12, title, subtitle, organizationName, sector, fullName, email,
                password, confirmPassword, error, createButton);
        card.setPadding(new Insets(28));
        card.setMaxWidth(480);
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 12px;");
        VBox root = new VBox(card);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #0f172a;");
        root.setAlignment(javafx.geometry.Pos.CENTER);
        stage.setScene(new Scene(root, 900, 700));
    }

    private void showMainScreen() {
        refreshOrganizations();
        refreshUsers();

        Label title = heading("CyberAudit");
        Label subtitle = new Label("Sessão iniciada como " + currentUser.fullName() + " — " + currentUser.role());
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        Button logoutButton = new Button("Terminar sessão");
        logoutButton.setOnAction(event -> {
            currentUser = null;
            showLoginOrFirstRun();
        });
        HBox top = new HBox(12, new VBox(4, title, subtitle), logoutButton);
        top.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox.setHgrow(top.getChildren().get(0), Priority.ALWAYS);

        TabPane tabs = new TabPane(createOrganizationsTab(), createUsersTab());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Label note = new Label("Os dados são guardados localmente em data/cyberaudit.db.");
        note.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        VBox content = new VBox(16, top, tabs, note);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: #0f172a;");
        VBox.setVgrow(tabs, Priority.ALWAYS);
        stage.setScene(new Scene(content, 1000, 680));
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
        boolean admin = isAdmin();
        nameField.setDisable(!admin);
        sectorField.setDisable(!admin);
        statusField.setDisable(!admin);
        addButton.setDisable(!admin);
        deleteButton.setDisable(!admin);

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
        passwordField.setPromptText("Palavra-passe (mínimo 12 caracteres)");
        ComboBox<String> roleField = new ComboBox<>();
        roleField.getItems().addAll("ADMIN", "ANALISTA", "LEITOR");
        roleField.setValue("ANALISTA");
        userOrganizationField = new ComboBox<>();
        userOrganizationField.setItems(organizations);
        userOrganizationField.setPromptText("Organização");
        userOrganizationField.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Organization organization) {
                return organization == null ? "" : organization.name();
            }

            @Override
            public Organization fromString(String value) {
                return organizations.stream().filter(item -> item.name().equals(value)).findFirst().orElse(null);
            }
        });
        Button addButton = new Button("Criar utilizador");
        addButton.setOnAction(event -> addUser(fullNameField, emailField, passwordField, roleField, userOrganizationField));
        Button deleteButton = new Button("Eliminar selecionado");
        deleteButton.setOnAction(event -> deleteSelectedUser());
        boolean admin = isAdmin();
        fullNameField.setDisable(!admin);
        emailField.setDisable(!admin);
        passwordField.setDisable(!admin);
        roleField.setDisable(!admin);
        userOrganizationField.setDisable(!admin);
        addButton.setDisable(!admin);
        deleteButton.setDisable(!admin);

        HBox form = new HBox(10, fullNameField, emailField, passwordField, roleField, userOrganizationField, addButton);
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
            showMessage(Alert.AlertType.ERROR, "Não foi possível guardar a organização.");
        }
    }

    private void addUser(TextField fullName, TextField email, PasswordField password,
                         ComboBox<String> role, ComboBox<Organization> organization) {
        String nameValue = fullName.getText().trim();
        String emailValue = email.getText().trim();
        String passwordValue = password.getText();
        Organization selectedOrganization = organization.getValue();
        if (nameValue.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty() || selectedOrganization == null) {
            showMessage(Alert.AlertType.WARNING, "Preenche todos os campos e escolhe uma organização.");
            return;
        }
        if (!isValidEmail(emailValue)) {
            showMessage(Alert.AlertType.WARNING, "Introduz um endereço de email válido.");
            return;
        }
        if (passwordValue.length() < 12) {
            showMessage(Alert.AlertType.WARNING, "A palavra-passe deve ter pelo menos 12 caracteres.");
            return;
        }
        try {
            userRepository.save(nameValue, emailValue, passwordValue, role.getValue(), selectedOrganization.id());
            fullName.clear();
            email.clear();
            password.clear();
            role.setValue("ANALISTA");
            refreshUsers();
            showMessage(Alert.AlertType.INFORMATION, "Utilizador criado com sucesso.");
        } catch (SQLException exception) {
            if (exception.getMessage() != null && exception.getMessage().toLowerCase().contains("unique")) {
                showMessage(Alert.AlertType.WARNING, "Já existe um utilizador com esse email.");
            } else {
                showMessage(Alert.AlertType.ERROR, "Não foi possível criar o utilizador.");
            }
        }
    }

    private void deleteSelectedOrganization() {
        Organization selected = organizationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage(Alert.AlertType.INFORMATION, "Seleciona uma organização primeiro.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Eliminar esta organização também elimina os utilizadores associados. Continuar?");
        confirmation.setHeaderText(null);
        confirmation.setTitle("Confirmar eliminação");
        if (confirmation.showAndWait().orElse(javafx.scene.control.ButtonType.CANCEL)
                != javafx.scene.control.ButtonType.OK) {
            return;
        }
        try {
            organizationRepository.deleteById(selected.id());
            refreshOrganizations();
            refreshUsers();
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível eliminar a organização.");
        }
    }

    private void deleteSelectedUser() {
        UserAccount selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage(Alert.AlertType.INFORMATION, "Seleciona um utilizador primeiro.");
            return;
        }
        if (selected.id().equals(currentUser.id())) {
            showMessage(Alert.AlertType.WARNING, "Não podes eliminar a conta que está atualmente em sessão.");
            return;
        }
        try {
            userRepository.deleteById(selected.id());
            refreshUsers();
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível eliminar o utilizador.");
        }
    }

    private void refreshOrganizations() {
        try {
            organizations.setAll(organizationRepository.findAll());
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível carregar as organizações.");
        }
    }

    private void refreshUsers() {
        try {
            users.setAll(userRepository.findAll());
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível carregar os utilizadores.");
        }
    }

    private boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.role());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private Label heading(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #a78bfa;");
        return label;
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
