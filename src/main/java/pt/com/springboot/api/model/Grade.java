package pt.com.springboot.api.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@ToString
@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotEmpty(message = "Grade Date cannot be empty")
    private Date gradeDate;

    @NotEmpty(message = "Grade cannot be empty")
    private float grade;

    @NotEmpty(message = "Grade Comment cannot be empty")
    private String gradeComment;

    @NotEmpty(message = "Grade Comment cannot be empty")
    private Long teacherId;

    @NotEmpty(message = "Lecture Comment cannot be empty")
    private Long lectureId;

}
