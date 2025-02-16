package pt.com.springboot.api.service;

import org.springframework.data.domain.Page;
import pt.com.springboot.api.model.Student;

import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface for Student Service
 */
public interface StudentService {

    List<Student> getStudentQueryFilter(String filter, String filterValue);
    Page<Student> listAll(Pageable pageable);
    boolean saveStudent(Student student);
    boolean deleteStudent(String id);
    boolean updateStudent(Student student);
}
