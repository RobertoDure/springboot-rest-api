package pt.com.springboot.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pt.com.springboot.api.model.Student;

import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    List<Student> findByNameIgnoreCaseContaining(String name);

}
