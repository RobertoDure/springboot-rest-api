package pt.com.springboot.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pt.com.springboot.api.model.Teacher;

import java.util.List;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {

    List<Teacher> findByNameIgnoreCaseContaining(String name);
}
