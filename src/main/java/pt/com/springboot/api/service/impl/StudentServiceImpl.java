package pt.com.springboot.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.model.Lecture;
import pt.com.springboot.api.model.Student;
import pt.com.springboot.api.repository.LectureRepository;
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

    LectureRepository lectureDAO;

    public StudentServiceImpl(StudentRepository studentDAO, LectureRepository lectureDAO) {
        this.studentDAO = studentDAO;
        this.lectureDAO = lectureDAO;
    }

    @Override
    public List<Student> getStudentQueryFilter(String filter, String filterValue) {
        List<Student> students = new ArrayList<>();
        logger.info("Starting Listing query students service");
        // Filter by id
        if (filter.equals("id")) {
            try {
                Long id = Long.parseLong(filterValue);
                Optional<Student> student = Optional.ofNullable(studentDAO.findOne(id));
                if (!student.isPresent()) {
                    logger.error("Student not found for ID: {}", id);
                    throw new ResourceNotFoundException("Student not found for ID: " + id);
                }

                logger.info("Finishing Listing query students service by ID");
                logger.debug("Student found for ID: {}", student.get());
                students.add(student.get());
                return students;
            } catch (InternalServerErrorException e) {
                logger.error("Error trying to get student by ID: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        }
        // Filter by name
        Optional<List<Student>> student = Optional.ofNullable(studentDAO.findByNameIgnoreCaseContaining(filterValue));
        if (!student.isPresent()) {
            logger.debug("Student not found for name: {}", filterValue);
            throw new ResourceNotFoundException("Student not found for Name: " + filterValue);

        }
        logger.info("Finishing Listing query students service by Name");
        student.get().forEach(st -> logger.debug("Student not found for name: {}", st));
        students = student.get();
        return students;
    }


    @Override
    public Page<Student> listAll(Pageable pageable) {
        List<Student> students = Collections.emptyList();
        logger.info("Starting Listing all students service");
        try {
            Page<Student> studentPage;
            studentPage = studentDAO.findAll(pageable);
            logger.info("Finishing Listing all students service");
            return studentPage;
        } catch (Exception e) {
            logger.error("Error trying to list all students: {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean saveStudent(Student student) {
        try {
            logger.info("Starting Save student service");
            logger.debug("Student to save: {}", student);
            // Update lectures to have managed references

            studentDAO.save(updateStudentLectures(student));

            logger.info("Finishing Save student service");
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
            logger.info("Starting Delete student service");
            logger.debug("Student to Delete with ID: {}", id);
            idLong = Long.parseLong(id);
            if (studentDAO.exists(idLong)) {
                try {
                    studentDAO.delete(idLong);
                    logger.info("Finishing Delete student service");
                    return result = true;
                } catch (Exception e) {
                    logger.error("Error trying to delete student: {}", e.getMessage());
                    throw new InternalServerErrorException(e.getMessage());
                }
            } else {
                throw new ResourceNotFoundException("Student not found for ID: " + id);
            }
        } catch (NumberFormatException e) {
            logger.error("ID not valid: {}", id);
            throw new ResourceNotFoundException("ID not valid: " + id);
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        logger.info("Starting Update student service");
        if (studentDAO.exists(student.getId())) {
            try {
                logger.debug("Student to Update with ID: {}", student);
                studentDAO.save(updateStudentLectures(student));
                logger.info("Finishing Update student service");
                return true;
            } catch (Exception e) {
                logger.error("Error trying to update student: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        } else {
            throw new ResourceNotFoundException("Student not found for ID: " + student.getId());
        }


    }
    // Helper method to update student's lecture list:

    /**
     * Helper method to update student's lecture list.
     * Lectures can only be saved and updated on Student record if they already exist in the database.
     *
     * @param student
     * @return Student object with updated lecture list.
     */
    private Student updateStudentLectures(Student student) {
        List<Lecture> managedLectures = new ArrayList<>();
        for (Lecture lecture : student.getLectures()) {
            if (lecture != null && lecture.getId() != null) {
                Optional<Lecture> optLecture = Optional.ofNullable(lectureDAO.findById(lecture.getId()));
                if (optLecture.isPresent()) {
                    managedLectures.add(optLecture.get());
                } else {
                    logger.error("Lecture not found for ID: {}", lecture.getId());
                    throw new ResourceNotFoundException("Lecture not found for ID: " + lecture.getId());
                }
            }
        }
        student.setLectures(managedLectures);
        return student;
    }
}
