package pt.com.springboot.api.model;

import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import java.util.List;


@Entity
public class Teacher extends AbstractEntity{

    @NotEmpty(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotEmpty(message = "Subject cannot be empty")
    private String subject;

    @NotEmpty(message = "Highest degree cannot be empty")
    private String highestDegree;

    @NotEmpty(message = "Years of experience cannot be empty")
    private String yearsOfExperience;

    @OneToMany
    private List<Lecture> lectures;

}