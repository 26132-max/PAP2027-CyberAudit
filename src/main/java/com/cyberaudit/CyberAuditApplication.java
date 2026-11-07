package com.cyberaudit;

import com.cyberaudit.domain.Organization;
import com.cyberaudit.repository.OrganizationRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class CyberAuditApplication extends Application {

    private final OrganizationRepository repository = new OrganizationRepository();
    private final ObservableList<Organization> organizations = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        Label title = new Label("CyberAudit");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #a78bfa;");
        Label subtitle = new Label("Gestão local de organizações");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        TableView<Organization> table = createOrganizationTable();
        table.setItems(organizations);

        TextField nameField = new TextField();
        nameField.setPromptText("Nome da organização");
        TextField sectorField = new TextField();
        sectorField.setPromptText("Setor");
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("Ativa", "Em revisão", "Inativa");
        statusField.setValue("Ativa");
        Button addButton = new Button("Adicionar organização");
        addButton.setDefaultButton(true);
        addButton.setOnAction(event -> addOrganization(nameField, sectorField, statusField));
        Button deleteButton = new Button("Eliminar selecionada");
        deleteButton.setOnAction(event -> deleteSelected(table));

        HBox form = new HBox(10, nameField, sectorField, statusField, addButton);
        HBox.setHgrow(nameField, Priority.ALWAYS);
        HBox.setHgrow(sectorField, Priority.ALWAYS);
        HBox actions = new HBox(deleteButton);

        Label note = new Label("Os dados são guardados localmente no ficheiro data/cyberaudit.db.");
        note.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        VBox header = new VBox(4, title, subtitle);
        VBox content = new VBox(16, header, form, table, actions, note);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: #0f172a;");
        table.setStyle("-fx-background-color: #111827;");
        nameField.setPrefWidth(220);
        sectorField.setPrefWidth(200);

        BorderPane root = new BorderPane(content);
        Scene scene = new Scene(root, 950, 620);
        stage.setTitle("CyberAudit — Aplicação de Auditoria de Cibersegurança");
        stage.setMinWidth(760);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();

        refreshOrganizations();
    }

    private TableView<Organization> createOrganizationTable() {
        TableView<Organization> table = new TableView<>();
        TableColumn<Organization, String> nameColumn = new TableColumn<>("Organização");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(300);
        TableColumn<Organization, String> sectorColumn = new TableColumn<>("Setor");
        sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sector"));
        sectorColumn.setPrefWidth(260);
        TableColumn<Organization, String> statusColumn = new TableColumn<>("Estado");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(160);
        TableColumn<Organization, String> dateColumn = new TableColumn<>("Criada em");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        dateColumn.setPrefWidth(160);
        table.getColumns().addAll(nameColumn, sectorColumn, statusColumn, dateColumn);
        VBox.setVgrow(table, Priority.ALWAYS);
        return table;
    }

    private void addOrganization(TextField name, TextField sector, ComboBox<String> status) {
        String orgName = name.getText().trim();
        String orgSector = sector.getText().trim();
        if (orgName.isEmpty() || orgSector.isEmpty()) {
            showMessage(Alert.AlertType.WARNING, "Preenche o nome e o setor da organização.");
            return;
        }
        try {
            repository.save(new Organization(null, orgName, orgSector, status.getValue(), ""));
            name.clear();
            sector.clear();
            status.setValue("Ativa");
            refreshOrganizations();
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível guardar a organização: " + exception.getMessage());
        }
    }

    private void deleteSelected(TableView<Organization> table) {
        Organization selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage(Alert.AlertType.INFORMATION, "Seleciona uma organização primeiro.");
            return;
        }
        try {
            repository.deleteById(selected.id());
            refreshOrganizations();
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível eliminar a organização: " + exception.getMessage());
        }
    }

    private void refreshOrganizations() {
        try {
            List<Organization> results = repository.findAll();
            organizations.setAll(results);
        } catch (SQLException exception) {
            showMessage(Alert.AlertType.ERROR, "Não foi possível carregar as organizações: " + exception.getMessage());
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
