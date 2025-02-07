package pt.com.springboot.api.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
@Entity
public class Student extends AbstractEntity {

    @NotNull(message = "Teacher ID cannot be null")
    private Long idTeacher;

    @NotEmpty(message = "Guardian name cannot be empty")
    private String guardianName;

    @NotEmpty(message = "Guardian contact cannot be empty")
    private String guardianContact;

    @OneToMany
    private List<Grade> grades;

    @OneToMany
    private List<Lecture> lectures;

}
