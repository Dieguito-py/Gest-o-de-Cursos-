package senac.senacfx.model.entities;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String email;
    private Date birthDate;
    private Date joinDate;
    private String cpf;

    private Course course;

    public Student() {
    }

    public Student(Integer id, String name, String email, Date birthDate, Date joinDate, String cpf, Course course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.cpf = cpf;
        this.course = course;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Student other = (Student) o;
        if (id == null){
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", joinDate=" + joinDate +
                ", cpf='" + cpf + '\'' +
                ", course=" + course +
                '}';
    }
}
