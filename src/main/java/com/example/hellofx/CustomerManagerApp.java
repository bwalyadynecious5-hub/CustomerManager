package com.example.hellofx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application {
    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");
        Label nameLabel = new Label("Customer name");
        nameLabel.setLabelFor(nameField);

        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll("Central", "Lusaka", "Copperbelt");
        provinceBox.setPromptText("Choose a province");
        Label provinceLabel = new Label("Province");
        provinceLabel.setLabelFor(provinceBox);

        Button saveButton = new Button("Save customer");
        saveButton.setDefaultButton(true);
        Button deleteButton = new Button("Delete selected");
        Label status = new Label();

        TableView<Customer> table = new TableView<>();
        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        table.getColumns().addAll(nameCol, provinceCol);
        table.setItems(customers);

        saveButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                status.setText("Enter the customer name.");
                nameField.requestFocus();
                return;
            }
            String province = provinceBox.getValue();
            if (province == null) {
                status.setText("Choose a province.");
                provinceBox.requestFocus();
                return;
            }
            customers.add(new Customer(name, province));
            status.setText("Customer saved.");
            nameField.clear();
            provinceBox.getSelectionModel().clearSelection();
            nameField.requestFocus();
        });

        deleteButton.setOnAction(event -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                status.setText("Select a customer first.");
                return;
            }
            ButtonType delete = new ButtonType("Delete");
            Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete the selected customer?", delete, ButtonType.CANCEL);
            ask.setHeaderText("Confirm deletion");
            if (ask.showAndWait().orElse(ButtonType.CANCEL) == delete) {
                customers.remove(selected);
                status.setText("Customer deleted.");
            } else {
                status.setText("Deletion cancelled.");
            }
        });

        VBox form = new VBox(8, nameLabel, nameField, provinceLabel,
                provinceBox, new HBox(10, saveButton, deleteButton), status);
        VBox root = new VBox(15, form, table);
        root.setPadding(new Insets(15));

        stage.setTitle("Customer Manager");
        stage.setScene(new Scene(root, 450, 500));
        stage.show();
        nameField.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}