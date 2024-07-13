package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.CourseDao;
import senac.senacfx.model.entities.Course;

import java.util.List;

public class DepartmentService {

    //dependencia injetada usando padrao factory
    private CourseDao dao = DaoFactory.createDepartmentDao();

    public List<Course> findAll() {
        return dao.findAll();

        //Dados MOCK (fake) so para testar, sem puxar do banco por hora
//        List<Course> list = new ArrayList<>();
//        list.add(new Course(1,"Computadores"));
//        list.add(new Course(2,"Alimentação"));
//        list.add(new Course(3,"Financeiro"));
//        return list;

    }
    public void saveOrUpdate(Course obj){
        if (obj.getId() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
            }
        }

        public void remove(Course obj){
            dao.deleteById(obj.getId());
        }

    public List<Course> findAllWithStudentCount() {
        return dao.findAllWithStudentCount();
    }

    public int getTotalCourses() {
        return dao.findAll().size();
    }

    public int getTotalStudents() {
        int totalStudents = 0;
        List<Course> courses = dao.findAllWithStudentCount();
        for (Course course : courses) {
            totalStudents += course.getStudentCount();
        }
        return totalStudents;
    }
}

