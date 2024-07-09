package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.StudentDao;
import senac.senacfx.model.entities.Student;

import java.util.List;

public class SellerService {

    //dependencia injetada usando padrao factory
    private StudentDao dao = DaoFactory.createSellerDao();

    public List<Student> findAll() {
        return dao.findAll();

        //Dados MOCK (fake) so para testar, sem puxar do banco por hora
//        List<Student> list = new ArrayList<>();
//        list.add(new Student(1,"Computadores"));
//        list.add(new Student(2,"Alimentação"));
//        list.add(new Student(3,"Financeiro"));
//        return list;

    }
    public void saveOrUpdate(Student obj){
        if (obj.getId() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
            }
        }

        public void remove(Student obj){
            dao.deleteById(obj.getId());
        }
    }

