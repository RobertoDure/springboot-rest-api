package pt.com.springboot.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.model.Teacher;
import pt.com.springboot.api.repository.TeacherRepository;
import pt.com.springboot.api.service.TeacherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Teacher Service Implementation
 * API Version: 1.0
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class.getName());

    private final TeacherRepository teacherDAO;

    public TeacherServiceImpl(TeacherRepository teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public List<Teacher> getTeacherQueryFilter(String filter, String filterValue) {
        List<Teacher> teachers = new ArrayList<>();
        // Filter by id
        if (filter.equals("id")) {
            try {
                Long id = Long.parseLong(filterValue);
                Optional<Teacher> teacher = Optional.ofNullable(teacherDAO.findOne(id));
                if (!teacher.isPresent()) {
                    logger.debug("Teacher not found for ID: {}", id);
                    throw new ResourceNotFoundException("Teacher not found for ID: " + id);
                }
                teachers.add(teacher.get());
                return teachers;
            } catch (InternalServerErrorException e) {
                logger.error("Error trying to get Teacher by ID: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        }
        // Filter by name
        Optional<List<Teacher>> teacher = Optional.ofNullable(teacherDAO.findByNameIgnoreCaseContaining(filterValue));
        if (!teacher.isPresent()) {
            logger.debug("Student not found for name: {}", filterValue);
            throw new ResourceNotFoundException("Student not found for Name: " + filterValue);
        }
        teachers = teacher.get();

        return teachers;
    }

    @Override
    public Page<Teacher> listAll(Pageable pageable) {
        List<Teacher> teacher = Collections.emptyList();
        try {
            Page<Teacher> studentPage;
            return studentPage = teacherDAO.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error trying to list all teachers: {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean saveTeacher(Teacher teacher) {
        try {
            teacherDAO.save(teacher);
            return true;
        } catch (Exception e) {
            logger.error("Error trying to save teacher: {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public boolean deleteTeacher(Long id) {
        boolean result = false;
        if (teacherDAO.exists(id)) {
            try {
                teacherDAO.delete(id);
                return result = true;
            } catch (Exception e) {
                logger.error("Error trying to delete teacher: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        } else {
            throw new ResourceNotFoundException("Teacher not found for ID: " + id);
        }
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        if (teacherDAO.exists(teacher.getId())) {
            try {
                teacherDAO.save(teacher);
                return true;
            } catch (Exception e) {
                logger.error("Error trying to update teacher: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        } else {
            throw new ResourceNotFoundException("Teacher not found for ID: " + teacher.getId());
        }

    }
}
