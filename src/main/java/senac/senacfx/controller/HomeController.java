package senac.senacfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import senac.senacfx.model.entities.Course;
import senac.senacfx.model.services.DepartmentService;

import java.util.List;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private DepartmentService service = new DepartmentService();

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label totalCoursesLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillPieChart();
        int totalStudents = service.getTotalStudents();
        totalStudentsLabel.setText(String.valueOf(totalStudents));
        int totalCourses = service.getTotalCourses();
        totalCoursesLabel.setText(String.valueOf(totalCourses));
    }

    public void fillPieChart() {
        List<Course> courses = service.findAllWithStudentCount();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Course course : courses) {
            pieChartData.add(new PieChart.Data(course.getName(), course.getStudentCount()));
        }

        pieChart.setData(pieChartData);
    }
}
