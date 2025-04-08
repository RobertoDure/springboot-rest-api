package pt.com.springboot.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
public class Student extends AbstractEntity {

    @NotNull(message = "Teacher ID cannot be null")
    private Long idTeacher;

    @NotEmpty(message = "Guardian name cannot be empty")
    private String guardianName;

    @NotEmpty(message = "Guardian contact cannot be empty")
    private String guardianContact;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades;

    @ManyToMany
    private List<Lecture> lectures;

}
