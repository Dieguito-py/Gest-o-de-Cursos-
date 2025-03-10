package senac.senacfx.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import senac.senacfx.application.Main;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Course;
import senac.senacfx.model.entities.Student;
import senac.senacfx.model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CoursesListController implements Initializable, DataChangeListener {

    private DepartmentService service;

    @FXML
    private TableView<Course> tableViewDepartment;

    @FXML
    private TableColumn<Course, Integer> tableColumnId;

    @FXML
    private TableColumn<Course, String> tableColumnName;

    @FXML
    private TableColumn<Course, Integer> tableColumnSemester;

    @FXML
    private TableColumn<Course, Integer> tableColumnStudentCount;

    @FXML
    private Button btNew;

    private ObservableList<Course> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Course obj = new Course();
        createDialogForm(obj, "/gui/CoursesForm.fxml", parentStage);
    }

    public void setCourseService(DepartmentService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        tableColumnStudentCount.setCellValueFactory(new PropertyValueFactory<>("studentCount"));

        tableViewDepartment.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                Course obj = tableViewDepartment.getSelectionModel().getSelectedItem();
                if(obj != null) {
                    Stage parentStage = Utils.currentStage(event);
                    createDialogForm(obj, "/gui/CoursesForm.fxml", parentStage);
                }
            }
        });

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service is null!");
        }
        List<Course> list = service.findAllWithStudentCount();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obsList);
//        initEditButtons();
//        initRemoveButtons();
    }

    private void createDialogForm(Course obj, String absoluteName, Stage parentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            CoursesFormController controller = loader.getController();
            controller.setCourse(obj);
            controller.setCourseService(new DepartmentService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Cursos");
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

    // Adiciona o botão na coluna

//    private void initEditButtons() {
//        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//        tableColumnEDIT.setCellFactory(param -> new TableCell<Course, Course>() {
//            private final Button button = new Button("Editar");
//            @Override
//            protected void updateItem(Course obj, boolean empty) {
//                super.updateItem(obj, empty);
//                if (obj == null) {
//                    setGraphic(null);
//                    return;
//                }
//                setGraphic(button);
//                button.setOnAction(
//                        event -> createDialogForm(
//                                obj, "/gui/CoursesForm.fxml",Utils.currentStage(event)));
//            }
//        });
//    }

//    private void initRemoveButtons() {
//        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//        tableColumnREMOVE.setCellFactory(param -> new TableCell<Course, Course>() {
//            private final Button button = new Button("Remover");
//
//            @Override
//            protected void updateItem(Course obj, boolean empty) {
//                super.updateItem(obj, empty);
//                if (obj == null) {
//                    setGraphic(null);
//                    return;
//                }
//                setGraphic(button);
//                button.setOnAction(event -> removeEntity(obj));
//            }
//        });
//    }

    private void removeEntity(Course obj) {
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
