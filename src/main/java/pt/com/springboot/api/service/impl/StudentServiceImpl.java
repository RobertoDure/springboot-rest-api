package pt.com.springboot.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pt.com.springboot.api.config.TransactionContextHolder;
import pt.com.springboot.api.error.InternalServerErrorException;
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
    public List<Student> getStudentQueryFilter(String filter, String filterValue) {
        List<Student> students = new ArrayList<>();
        String transactionId = org.slf4j.MDC.get("transactionId");
        // Filter by id
        if(filter.equals("id")){
            try {
                Long id = Long.parseLong(filterValue);
                Optional<Student> student = Optional.ofNullable(studentDAO.findOne(id));
                if(!student.isPresent()){
                    logger.debug("Student not found for ID: {}", TransactionContextHolder.getTransactionId());
                    throw new ResourceNotFoundException("Student not found for ID: " + id);
                }
                logger.debug("Student not found for ID: {}", student.get());
                students.add(student.get());
                return students;
            } catch (InternalServerErrorException e) {
                logger.error("Error trying to get student by ID: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        }
        // Filter by name
        Optional<List<Student>> student = Optional.ofNullable(studentDAO.findByNameIgnoreCaseContaining(filterValue));
        if(!student.isPresent()){
            logger.debug("Student not found for name: {}", filterValue);
            throw new ResourceNotFoundException("Student not found for Name: " + filterValue);

        }
        student.get().forEach(st -> logger.debug("Student not found for name: {}", st));
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
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean saveStudent(Student student) {
        try {
            studentDAO.save(student);
            return true;
        } catch (Exception e) {
            logger.error("Error trying to save student: {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }

    }

    @Override
    public boolean deleteStudent(String id) {
        boolean result = false;
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
            if(studentDAO.exists(idLong)){
                try{
                    studentDAO.delete(idLong);
                    return result = true;
                } catch (Exception e) {
                    logger.error("Error trying to delete student: {}", e.getMessage());
                    throw new InternalServerErrorException(e.getMessage());
                }
            }
            else{
                throw new ResourceNotFoundException("Student not found for ID: " + id);
            }
        } catch (NumberFormatException e) {
            logger.error("ID not valid: {}", id);
            throw new ResourceNotFoundException("ID not valid: " + id);
        }
    }

    @Override
    public boolean updateStudent(Student student) {

        if(studentDAO.exists(student.getId())){
            try{
            studentDAO.save(student);
            return true;
            } catch (Exception e) {
                logger.error("Error trying to update student: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        }
        else{
            throw new ResourceNotFoundException("Student not found for ID: " + student.getId());
        }


    }
}
