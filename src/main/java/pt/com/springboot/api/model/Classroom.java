package pt.com.springboot.api.model;

import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Classroom extends AbstractEntity{
    @NotNull
    private int capacity;
    @NotEmpty
    private String classroomName;

    public Classroom() {}
    public Classroom(int capacity, String classroomName) {
        this.capacity = capacity;
        this.classroomName = classroomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }
}
