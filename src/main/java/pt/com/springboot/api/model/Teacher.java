package pt.com.springboot.api.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Teacher extends AbstractEntity {

    @NotEmpty(message = "Not Empty")
    private String name;
    @NotEmpty
    @Email(message = "Not an Email")
    private String email;
    @NotEmpty
    private String subject;
    @NotNull
    private int idClassroom;


    public Teacher() {}
    public Teacher(String name, String email, String subject, int idClassroom) {
        this.name = name;
        this.email = email;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getIdClassroom() {
        return idClassroom;
    }

    public void setIdClassroom(int idClassroom) {
        this.idClassroom = idClassroom;
    }
}