package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Course;
import senac.senacfx.model.entities.Student;
import java.util.List;

public interface StudentDao {

    void insert(Student obj);
    void update(Student obj);


    void deleteById(Integer id);
    Student findById(Integer id);
    List<Student> findAll();
    List<Student> findByDepartment(Course department);

}
