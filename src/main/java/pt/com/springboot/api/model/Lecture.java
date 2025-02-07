package pt.com.springboot.api.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Lecture Name cannot be empty")
    private String lectureName;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Starting Date cannot be null")
    private Date startDate;

    @NotNull(message = "Ending Date cannot be null")
    private Date endDate;

}
