package senac.senacfx.model.dao;

import senac.senacfx.db.DB;
import senac.senacfx.model.dao.impl.CourseDaoJDBC;
import senac.senacfx.model.dao.impl.StudentDaoJDBC;

public class DaoFactory {

    public static StudentDao createSellerDao(){
        return new StudentDaoJDBC(DB.getConnection());
    }

    public static CourseDao createDepartmentDao(){
        return new CourseDaoJDBC(DB.getConnection());
    }

}
