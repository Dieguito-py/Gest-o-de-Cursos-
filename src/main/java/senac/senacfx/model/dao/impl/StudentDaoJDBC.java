package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DB;
import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.StudentDao;
import senac.senacfx.model.entities.Course;
import senac.senacfx.model.entities.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDaoJDBC implements StudentDao {
    private Connection conn;

    public StudentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Student obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "insert into student " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "values (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Error! No rows affected!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Student obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "update student " +
                            "set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "where id = ?");

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("delete from student where Id = ?");

            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("Vendedor inexistente!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Student findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select student.*, course.Name as DepName " +
                    "from student inner join course " +
                    "on student.CourseId = course.Id " +
                    "where student.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()){
                Course dep = instantiateDepartment(rs);
                Student obj = instantiateSeller(rs, dep);
                return obj;

            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Course instantiateDepartment(ResultSet rs) throws SQLException {
        Course dep = new Course();
        dep.setId(rs.getInt("CourseId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Student instantiateSeller(ResultSet rs, Course dep) throws SQLException{
        Student obj = new Student();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
        obj.setDepartment(dep);
        return obj;
    }
    @Override
    public List<Student> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select student.*, course.Name as DepName " +
                    "from student inner join course " +
                    "on student.CourseId = course.Id " +
                    "order by Name");

            rs = st.executeQuery();

            List<Student> list = new ArrayList<>();
            Map<Integer, Course> map = new HashMap<>();

            while (rs.next()){

                Course dep = map.get(rs.getInt("CourseId"));

                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("CourseId"), dep);
                }

                Student obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Student> findByDepartment(Course department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select student.*, course.Name as DepName " +
                    "from student inner join course " +
                    "on student.CourseId = course.Id " +
                    "where CourseId = ? " +
                    "order by Name");

            st.setInt(1, department.getId());

            rs = st.executeQuery();

            List<Student> list = new ArrayList<>();
            Map<Integer, Course> map = new HashMap<>();

            while (rs.next()){

                Course dep = map.get(rs.getInt("CourseId"));

                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("CourseId"), dep);
                }

                Student obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
