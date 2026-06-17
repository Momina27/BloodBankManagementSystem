package com.bloodbank.ui;

import com.bloodbank.model.Donor;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.interfaces.IAlertObserver;
import com.bloodbank.service.*;
import com.bloodbank.util.SessionManager;
import com.bloodbank.util.ReportGenerator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Map;

public class MainApplication extends Application implements IAlertObserver {

    private AlertService alertService = new AlertService();
    private BloodInventory bloodInventory = new BloodInventory(alertService);
    private DonorManager donorManager = new DonorManager();
    private RequestManager requestManager = new RequestManager(bloodInventory);

    private Stage window;
    private TableView<Donor> donorTable;
    private TableView<BloodRequest> requestTable;
    private VBox stockGridContainer;
    private Label alertStatusLabel;

    private final String MAIN_COLOR = "#8B0000";
    private final String TEXT_FIELD_STYLE = "-fx-background-radius: 5; -fx-padding: 8; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-color: #FFFFFF;";
    private final String BUTTON_STYLE = "-fx-background-color: " + MAIN_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-background-radius: 5; -fx-cursor: hand;";
    private final String SECONDARY_BUTTON_STYLE = "-fx-background-color: #4A4A4A; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-background-radius: 5; -fx-cursor: hand;";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("🔴 Blood Bank Management System");
        alertService.addObserver(this);
        showLoginScreen();
    }

    @Override
    public void onStockCritical(String bloodGroup, int currentStock) {
        Platform.runLater(() -> {
            alertStatusLabel.setText("⚠️ CRITICAL ALERT: Blood Group " + bloodGroup + " is running extremely low! Stock: " + currentStock + " units.");
            alertStatusLabel.setStyle("-fx-text-fill: white; -fx-background-color: #D2143A; -fx-font-weight: bold; -fx-padding: 10px; -fx-background-radius: 5; -fx-font-size: 13px;");
        });
    }

    private void showLoginScreen() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #F9F9F9; -fx-font-family: 'Segoe UI';");

        Label logoLabel = new Label("🔴");
        logoLabel.setStyle("-fx-font-size: 40px;");

        Label titleLabel = new Label("LIFE SAVE");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + MAIN_COLOR + "; -fx-letter-spacing: 2px;");

        Label subTitleLabel = new Label("Blood Bank Management System");
        subTitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #777777; -fx-padding: 0 0 15 0;");

        TextField userField = new TextField();
        userField.setPromptText("Username (admin / staff)");
        userField.setStyle(TEXT_FIELD_STYLE);
        userField.setMaxWidth(260);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle(TEXT_FIELD_STYLE);
        passField.setMaxWidth(260);

        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle("-fx-background-color: " + MAIN_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 40 10 40; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 13px;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        loginBtn.setOnAction(e -> {
            try {
                SessionManager.getInstance().login(userField.getText(), passField.getText());
                showDashboard();
            } catch (InvalidLoginException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        layout.getChildren().addAll(logoLabel, titleLabel, subTitleLabel, userField, passField, loginBtn, errorLabel);
        Scene scene = new Scene(layout, 380, 420);
        window.setScene(scene);
        window.show();
    }

    private void showDashboard() {
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-tab-min-width: 150px; -fx-tab-max-width: 200px; -fx-tab-min-height: 35px;");

        Tab donorTab = new Tab("👥 Manage Donors", createDonorTab());
        donorTab.setClosable(false);

        Tab inventoryTab = new Tab("🩸 Blood Inventory", createInventoryTab());
        inventoryTab.setClosable(false);

        Tab requestTab = new Tab("🏥 Hospital Requests", createRequestTab());
        requestTab.setClosable(false);

        tabPane.getTabs().addAll(donorTab, inventoryTab, requestTab);

        HBox headerBar = new HBox();
        headerBar.setPadding(new Insets(15));
        headerBar.setStyle("-fx-background-color: " + MAIN_COLOR + "; -fx-alignment: center-left;");

        Label appTitle = new Label("🔴 BBMS Dashboard");
        appTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Welcome, " + SessionManager.getInstance().getCurrentUser().getRoleName() + " (" + SessionManager.getInstance().getCurrentUser().getUsername() + ")");
        userLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 0 15 0 0;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            SessionManager.getInstance().logout();
            showLoginScreen();
        });

        headerBar.getChildren().addAll(appTitle, spacer, userLabel, logoutBtn);

        alertStatusLabel = new Label("🟢 System Status: All Blood Stock Levels are Secure.");
        alertStatusLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold; -fx-padding: 10px; -fx-background-color: #E8F8F5; -fx-background-radius: 5; -fx-font-size: 13px;");

        VBox topContainer = new VBox(10, headerBar, alertStatusLabel);
        topContainer.setPadding(new Insets(0, 15, 0, 15));
        topContainer.setStyle("-fx-background-color: #F4F6F6;");

        VBox mainLayout = new VBox(10, topContainer, tabPane);
        mainLayout.setStyle("-fx-background-color: #F4F6F6;");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 950, 700);
        window.setScene(scene);
    }

    private VBox createDonorTab() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FFFFFF;");

        Label formTitle = new Label("Add / Update Donor Record");
        formTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MAIN_COLOR + ";");

        TextField idInput = new TextField(); idInput.setPromptText("Donor ID (Numeric)"); idInput.setStyle(TEXT_FIELD_STYLE);
        TextField nameInput = new TextField(); nameInput.setPromptText("Full Name"); nameInput.setStyle(TEXT_FIELD_STYLE);
        ComboBox<String> groupInput = new ComboBox<>(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
        groupInput.setPromptText("Select Blood Group");
        groupInput.setStyle(TEXT_FIELD_STYLE + " -fx-background-color: #FFFFFF;");
        TextField contactInput = new TextField(); contactInput.setPromptText("Contact Number"); contactInput.setStyle(TEXT_FIELD_STYLE);

        HBox inputForm = new HBox(15, idInput, nameInput, groupInput, contactInput);
        inputForm.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("💾 Save Record"); addBtn.setStyle(BUTTON_STYLE);
        Button deleteBtn = new Button("🗑️ Delete Selected"); deleteBtn.setStyle(SECONDARY_BUTTON_STYLE);
        Button searchBtn = new Button("🔍 Search by Name"); searchBtn.setStyle(BUTTON_STYLE);
        Button clearSearchBtn = new Button("🔄 View All"); clearSearchBtn.setStyle(SECONDARY_BUTTON_STYLE);

        HBox actions = new HBox(10, addBtn, deleteBtn, searchBtn, clearSearchBtn);

        donorTable = new TableView<>();
        donorTable.setStyle("-fx-background-color: transparent; -fx-border-color: #E0E0E0;");

        TableColumn<Donor, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("donorId"));
        idCol.setPrefWidth(80);

        TableColumn<Donor, String> nameCol = new TableColumn<>("Donor Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(220);

        TableColumn<Donor, String> groupCol = new TableColumn<>("Blood Group");
        groupCol.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        groupCol.setPrefWidth(120);

        TableColumn<Donor, String> contactCol = new TableColumn<>("Contact Details");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactCol.setPrefWidth(200);

        donorTable.getColumns().addAll(idCol, nameCol, groupCol, contactCol);
        refreshDonorTable();

        addBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idInput.getText());
                String group = groupInput.getValue();
                if (group == null || nameInput.getText().isEmpty()) throw new IllegalArgumentException();

                if(donorManager.searchDonor(id) != null) {
                    donorManager.updateDonor(id, nameInput.getText(), group, contactInput.getText());
                } else {
                    donorManager.addDonor(new Donor(id, nameInput.getText(), group, contactInput.getText(), "user"+id, "pass"));
                    bloodInventory.addBloodUnit(group, 5);
                }
                resetAlertStatusBar();
                refreshDonorTable();
                refreshInventoryGrid();

                idInput.clear(); nameInput.clear(); contactInput.clear(); groupInput.setValue(null);
            } catch (Exception ex) {
                showAlert("Form Validation Error", "Please enter correct details!");
            }
        });

        deleteBtn.setOnAction(e -> {
            Donor selected = donorTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                donorManager.deleteDonor(selected.getDonorId());
                refreshDonorTable();
            }
        });

        searchBtn.setOnAction(e -> {
            donorTable.setItems(FXCollections.observableArrayList(donorManager.searchDonor(nameInput.getText())));
        });

        clearSearchBtn.setOnAction(e -> refreshDonorTable());

        layout.getChildren().addAll(formTitle, inputForm, actions, donorTable);
        VBox.setVgrow(donorTable, Priority.ALWAYS); // Fix: setVgrow is used correctly for vertical sizing
        return layout;
    }

    private VBox createInventoryTab() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FFFFFF;");

        Label inventoryTitle = new Label("Real-time In-Memory Blood Stock Levels");
        inventoryTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MAIN_COLOR + ";");

        stockGridContainer = new VBox(10);
        stockGridContainer.setPadding(new Insets(10, 0, 10, 0));

        refreshInventoryGrid();

        Button refreshBtn = new Button("🔄 Sync Stock Data");
        refreshBtn.setStyle(BUTTON_STYLE);
        refreshBtn.setOnAction(e -> refreshInventoryGrid());

        layout.getChildren().addAll(inventoryTitle, stockGridContainer, refreshBtn);
        return layout;
    }

    private VBox createRequestTab() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FFFFFF;");

        Label reqTitle = new Label("Dispatch Hospital Blood Emergency Requests");
        reqTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MAIN_COLOR + ";");

        TextField reqIdInput = new TextField(); reqIdInput.setPromptText("Request ID"); reqIdInput.setStyle(TEXT_FIELD_STYLE);
        TextField hospInput = new TextField(); hospInput.setPromptText("Hospital Name"); hospInput.setStyle(TEXT_FIELD_STYLE);
        ComboBox<String> reqGroupInput = new ComboBox<>(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
        reqGroupInput.setPromptText("Blood Group Needed");
        reqGroupInput.setStyle(TEXT_FIELD_STYLE + " -fx-background-color: #FFFFFF;");
        TextField qtyInput = new TextField(); qtyInput.setPromptText("Required Units"); qtyInput.setStyle(TEXT_FIELD_STYLE);

        HBox reqForm = new HBox(15, reqIdInput, hospInput, reqGroupInput, qtyInput);

        Button reqBtn = new Button("🚀 Dispatch Request"); reqBtn.setStyle(BUTTON_STYLE);
        Button approveBtn = new Button("✅ Approve and Issue"); approveBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-background-radius: 5; -fx-cursor: hand;");
        Button rejectBtn = new Button("❌ Decline Request"); rejectBtn.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-background-radius: 5; -fx-cursor: hand;");

        HBox reqActions = new HBox(10, reqBtn, approveBtn, rejectBtn);

        requestTable = new TableView<>();
        requestTable.setStyle("-fx-background-color: transparent; -fx-border-color: #E0E0E0;");

        TableColumn<BloodRequest, Integer> rIdCol = new TableColumn<>("Req ID"); rIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId")); rIdCol.setPrefWidth(80);
        TableColumn<BloodRequest, String> hospCol = new TableColumn<>("Hospital Destination"); hospCol.setCellValueFactory(new PropertyValueFactory<>("hospitalName")); hospCol.setPrefWidth(200);
        TableColumn<BloodRequest, String> bgCol = new TableColumn<>("Blood Group"); bgCol.setCellValueFactory(new PropertyValueFactory<>("bloodGroup")); bgCol.setPrefWidth(120);
        TableColumn<BloodRequest, Integer> qtyCol = new TableColumn<>("Requested Units"); qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity")); qtyCol.setPrefWidth(120);
        TableColumn<BloodRequest, String> statusCol = new TableColumn<>("Process Status"); statusCol.setCellValueFactory(new PropertyValueFactory<>("status")); statusCol.setPrefWidth(150);

        requestTable.getColumns().addAll(rIdCol, hospCol, bgCol, qtyCol, statusCol);

        reqBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(reqIdInput.getText());
                int qty = Integer.parseInt(qtyInput.getText());
                if(hospInput.getText().isEmpty() || reqGroupInput.getValue() == null) throw new IllegalArgumentException();
                requestManager.createRequest(new BloodRequest(id, hospInput.getText(), reqGroupInput.getValue(), qty));
                refreshRequestTable();

                reqIdInput.clear(); hospInput.clear(); qtyInput.clear(); reqGroupInput.setValue(null);
            } catch(Exception ex) {
                showAlert("Input Error", "Verify request form data!");
            }
        });

        approveBtn.setOnAction(e -> {
            BloodRequest selected = requestTable.getSelectionModel().getSelectedItem();
            if(selected != null) {
                requestManager.approveRequest(selected.getRequestId());
                refreshRequestTable();
                refreshInventoryGrid();
            }
        });

        rejectBtn.setOnAction(e -> {
            BloodRequest selected = requestTable.getSelectionModel().getSelectedItem();
            if(selected != null) {
                requestManager.rejectRequest(selected.getRequestId());
                refreshRequestTable();
            }
        });

        layout.getChildren().addAll(reqTitle, reqForm, reqActions, requestTable);
        return layout;
    }

    private void refreshDonorTable() {
        donorTable.setItems(FXCollections.observableArrayList(donorManager.getAllDonors()));
    }

    private void refreshRequestTable() {
        requestTable.setItems(FXCollections.observableArrayList(requestManager.getAllRequests()));
    }

    private void refreshInventoryGrid() {
        stockGridContainer.getChildren().clear();

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        int column = 0;
        int row = 0;

        for (Map.Entry<String, Integer> entry : bloodInventory.getAllStock().entrySet()) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(15));
            card.setPrefWidth(180);
            card.setAlignment(Pos.CENTER);

            String cardBgColor = "#FFFFFF";
            String textColor = "#333333";
            if (entry.getValue() <= 2) {
                cardBgColor = "#FADBD8";
                textColor = "#7B241C";
            } else {
                cardBgColor = "#EAFAF1";
                textColor = "#196F3D";
            }

            card.setStyle("-fx-background-color: " + cardBgColor + "; -fx-background-radius: 8; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

            Label groupLabel = new Label("Type " + entry.getKey());
            groupLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");

            Label qtyLabel = new Label(entry.getValue() + " Units");
            qtyLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");

            card.getChildren().addAll(groupLabel, qtyLabel);
            grid.add(card, column, row);

            column++;
            if (column == 4) {
                column = 0;
                row++;
            }
        }
        stockGridContainer.getChildren().add(grid);
    }

    private void resetAlertStatusBar() {
        alertStatusLabel.setText("🟢 System Status: All Blood Stock Levels are Secure.");
        alertStatusLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold; -fx-padding: 10px; -fx-background-color: #E8F8F5; -fx-background-radius: 5; -fx-font-size: 13px;");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}