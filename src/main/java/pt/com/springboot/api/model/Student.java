package pt.com.springboot.api.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Student extends AbstractEntity {
    @NotEmpty(message = "Not Empty")
    private String name;
    @NotEmpty
    @Email(message = "Not an Email")
    private String email;
    @NotNull
    private int idTeacher;
    @NotNull
    private int idClassroom;

    public Student() {}
    public Student(String name, String email, int idTeacher, int idClassroom) {
        this.name = name;
        this.email = email;
        this.idTeacher = idTeacher;
        this.idClassroom = idClassroom;
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

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public int getIdClassroom() {
        return idClassroom;
    }

    public void setIdClassroom(int idClassroom) {
        this.idClassroom = idClassroom;
    }
}
