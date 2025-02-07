package pt.com.springboot.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.model.Student;
import pt.com.springboot.api.repository.StudentRepository;
import pt.com.springboot.api.service.StudentService;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class.getName());

    StudentRepository studentDAO;

    public StudentServiceImpl(StudentRepository studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public Student getStudentById(Long id) {
        Optional<Student> student = Optional.ofNullable(studentDAO.findOne(id));
        if(!student.isPresent()){
            logger.debug("Student not found for ID: {}", id);
            throw new ResourceNotFoundException("Student not found for ID: " + id);
        }
        return student.get();
    }

    @Override
    public List<Student> findByNameIgnoreCaseContaining(String name) {
        List<Student> students = new ArrayList<>();
        Optional<List<Student>> student = Optional.ofNullable(studentDAO.findByNameIgnoreCaseContaining(name));
        if(!student.isPresent()){
            logger.debug("Student not found for name: {}", name);
            throw new ResourceNotFoundException("Student not found for name: " + name);
        }
        students = student.get();

        return students;
    }

    @Override
    public Page<Student> listAll(Pageable pageable) {
        List<Student> students = Collections.emptyList();
        try {
            Page<Student> studentPage;
            return studentPage = studentDAO.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error trying to list all students: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean saveStudent(Student student) {
        try {
            studentDAO.save(student);
            return true;
        } catch (Exception e) {
            logger.error("Error trying to save student: {}", e.getMessage());
        }
        return false;

    }

    @Override
    public boolean deleteStudent(Long id) {
        boolean result = false;
        if(studentDAO.exists(id)){
            studentDAO.delete(id);
            return result = true;

        }
        return false;
    }

    @Override
    public boolean updateStudent(Student student) {
        if(studentDAO.exists(student.getId())){
            studentDAO.save(student);
            return true;
        }
        return false;
    }
}
