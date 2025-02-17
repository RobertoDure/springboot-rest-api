package pt.com.springboot.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.Date;

@Data
@ToString
@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    @Past(message = "Grade date must be in the past")
    @NotNull(message = "Grade Date cannot be empty")
    private Date gradeDate;

    @NotNull(message = "Grade cannot be empty")
    @DecimalMin(value = "0.0", inclusive = true, message = "Grade must be at least 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Grade must be at most 10.0")
    private float grade;

    @NotEmpty(message = "Grade Comment cannot be empty")
    private String gradeComment;

    @NotNull(message = "Grade Comment cannot be empty")
    @Min(value = 1, message = "Teacher ID must be greater than zero")
    private Long teacherId;

    @NotNull(message = "Lecture Comment cannot be empty")
    @Min(value = 1, message = "Teacher ID must be greater than zero")
    private Long lectureId;

}
