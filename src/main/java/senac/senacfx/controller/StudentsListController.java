package senac.senacfx.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import senac.senacfx.application.Main;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Student;
import senac.senacfx.model.services.DepartmentService;
import senac.senacfx.model.services.SellerService;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentsListController implements Initializable, DataChangeListener {

    private SellerService service;

    @FXML
    private TableView<Student> tableViewSeller;

    @FXML
    private TableColumn<Student, Integer> tableColumnId;

    @FXML
    private TableColumn<Student, String> tableColumnName;

    @FXML
    private TableColumn<Student, String> tableColumnEmail;

    @FXML
    private TableColumn<Student, Date> tableColumnBirthDate;

    @FXML
    private TableColumn<Student, Date> tableColumnJoinDate;

    @FXML
    private TableColumn<Student, String> tableColumnCpf;

    @FXML
    private TableColumn<Student, Student> tableColumnCourse;

    @FXML
    private TableColumn<Student, Student> tableColumnEDIT;

    @FXML
    private TableColumn<Student, Student> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<Student> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Student obj = new Student();
        createDialogForm(obj, "/gui/StudentsForm.fxml", parentStage);
    }

    public void setSellerService(SellerService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnJoinDate.setCellValueFactory(new PropertyValueFactory<>("JoinDate"));
        Utils.formatTableColumnDate(tableColumnJoinDate, "dd/MM/yyyy");
        tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnCourse.setCellValueFactory(new PropertyValueFactory<>("course"));

        tableViewSeller.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                Student obj = tableViewSeller.getSelectionModel().getSelectedItem();
                if(obj != null) {
                    Stage parentStage = Utils.currentStage(event);
                    createDialogForm(obj, "/gui/StudentsForm.fxml", parentStage);
                }
            }
        });

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service is null!");
        }
        List<Student> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(obsList);
    }

    private void createDialogForm(Student obj, String absoluteName, Stage parentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            StudentsFormController controller = loader.getController();
            controller.setSeller(obj);
            controller.setServices(new SellerService(), new DepartmentService());
            controller.loadAssociatedObjects();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter seller data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void removeEntity(Student obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Confirma que quer deletar?");

        if (result.get() == ButtonType.OK){
            if (service == null){
                throw new IllegalStateException("Service estava null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbException e){
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

}
