package pt.com.springboot.api.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Data
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    @ToString.Exclude
    private String password;

    @NotEmpty
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean admin;

    @NotEmpty
    @Column(unique = true)
    private String email;
}
