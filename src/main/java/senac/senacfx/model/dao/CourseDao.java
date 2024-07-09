package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Course;

import java.util.List;

public interface CourseDao {

    void insert(Course obj);
    void update(Course obj);
    void deleteById(Integer id);
    Course findById(Integer id);
    List<Course> findAll();

}
