package pt.com.springboot.api.service;

import org.springframework.data.domain.Page;
import pt.com.springboot.api.model.Student;

import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface for Student Service
 */
public interface StudentService {

    Student getStudentById(Long id);
    List<Student> findByNameIgnoreCaseContaining(String name);
    Page<Student> listAll(Pageable pageable);
    boolean saveStudent(Student student);
    boolean deleteStudent(Long id);
    boolean updateStudent(Student student);
}
