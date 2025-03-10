package senac.senacfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Constraints;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Course;
import senac.senacfx.model.entities.Student;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.DepartmentService;
import senac.senacfx.model.services.SellerService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class StudentsFormController implements Initializable {

    private Student entity;

    private SellerService service;

    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private DatePicker dpJoinDate;

    @FXML
    private TextField txtCpf;

    @FXML
    private ComboBox<Course> comboBoxDepartment;
    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorJoinDate;

    @FXML
    private Label labelErrorCpf;

    @FXML
    private Button btSave;

    @FXML
    private Button btRemove;

    private ObservableList<Course> obsList;

    //Contolador agora tem uma instancia do departamento
    public void setSeller(Student entity){
        this.entity = entity;
    }

    public void setServices(SellerService service, DepartmentService departmentService){
        this.service = service;
        this.departmentService = departmentService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null){
            throw new IllegalStateException("Entidade nula");
        }
        if (service == null){
            throw new IllegalStateException("Servico nulo");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e){
            Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e){
            setErrorMessages(e.getErrors());
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Student getFormData() {
        Student obj = new Student();

        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "campo nao pode ser vazio");
        }
        obj.setName(txtName.getText());

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("email", "campo nao pode ser vazio");
        }
        obj.setEmail(txtEmail.getText());

        if (dpBirthDate.getValue() == null){
            exception.addError("birthDate", "data nao selecionada");
        } else {
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setBirthDate(Date.from(instant));
        }

        if (dpJoinDate.getValue() == null){
            exception.addError("joinDate", "data nao selecionada");
        } else {
            Instant instant = Instant.from(dpJoinDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setJoinDate(Date.from(instant));
        }

        if (txtCpf.getText() == null || txtCpf.getText().trim().equals("")){
            exception.addError("cpf", "campo nao pode ser vazio");
        }
        obj.setCpf(txtCpf.getText());

        obj.setCourse(comboBoxDepartment.getValue());

        if (exception.getErrors().size() > 0){
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtRemoveAction(ActionEvent event) {
        if (entity == null){
            Alerts.showAlert("Erro ao remover objeto", null, "Entidade nula", Alert.AlertType.ERROR);
        }
        if (service == null){
            Alerts.showAlert("Erro ao remover objeto", null, "Servico nulo", Alert.AlertType.ERROR);
        }

        try {
            service.remove(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e){
            Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        Utils.formatDatePicker(dpJoinDate, "dd/MM/yyyy");
        Constraints.setTextFieldMaxLength(txtCpf, 60);
        initializeComboBoxDepartment();

    }

    public void updateFormData(){

        if (entity == null){
            throw new IllegalStateException("Entidade nula");
        }

        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        txtCpf.setText(entity.getCpf());
        Locale.setDefault(Locale.US);

        if (entity.getJoinDate() != null) {
            dpJoinDate.setValue(LocalDate.ofInstant(entity.getJoinDate().toInstant(), ZoneId.systemDefault()));
        }
        if (entity.getBirthDate() != null) {
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }

        if (entity.getCourse() == null) {
            comboBoxDepartment.getSelectionModel().selectFirst();
        } else {
            comboBoxDepartment.setValue(entity.getCourse());
        }

    }

    public void loadAssociatedObjects(){

        if (departmentService == null){
            throw new IllegalStateException("DepartmentService was null");
        }

        List<Course> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
        labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
        labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
        labelErrorJoinDate.setText((fields.contains("joinDate") ? errors.get("joinDate") : ""));
        labelErrorCpf.setText((fields.contains("cpf") ? errors.get("cpf") : ""));
        labelErrorName.getStyleClass().add("button");

    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Course>, ListCell<Course>> factory = lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

}
