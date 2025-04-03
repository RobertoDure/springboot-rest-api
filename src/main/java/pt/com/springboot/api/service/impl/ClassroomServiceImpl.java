package pt.com.springboot.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.model.Classroom;
import pt.com.springboot.api.repository.ClassroomRepository;
import pt.com.springboot.api.service.ClassroomService;
import java.util.Collections;
import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private static final Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class.getName());

    private final ClassroomRepository classroomDAO;

    public ClassroomServiceImpl(ClassroomRepository classroomDAO) {
        this.classroomDAO = classroomDAO;
    }

    @Override
    public Page<Classroom> listAll(Pageable pageable) {
        List<Classroom> classroom = Collections.emptyList();
        logger.info("Starting Listing all Classrooms service");
        try {
            Page<Classroom> classroomPage;
            classroomPage = classroomDAO.findAll(pageable);
            logger.info("Finishing Listing all Classrooms service");
            return classroomPage;
        } catch (Exception e) {
            logger.error("Error trying to list all Classrooms: {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean saveClassroom(Classroom classroom) {
        try {
            logger.info("Starting Save classroom service");
            logger.debug("Classroom to save: {}", classroom);

            classroomDAO.save(classroom);

            logger.info("Finishing Save classroom service");
            return true;
        } catch (Exception e) {
            logger.error("Error trying to save student: {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public boolean deleteClassroom(Long id) {
        boolean result = false;
        try {
            logger.info("Starting Delete classroom service");
            logger.debug("classroom to Delete with ID: {}", id);

            if (classroomDAO.exists(id)) {
                try {
                    classroomDAO.delete(id);
                    logger.info("Finishing Delete classroom service");
                    return result = true;
                } catch (Exception e) {
                    logger.error("Error trying to delete classroom: {}", e.getMessage());
                    throw new InternalServerErrorException(e.getMessage());
                }
            } else {
                throw new ResourceNotFoundException("Classroom not found for ID: " + id);
            }
        } catch (NumberFormatException e) {
            logger.error("ID not valid: {}", id);
            throw new ResourceNotFoundException("ID not valid: " + id);
        }
    }

    @Override
    public boolean updateClassroom(Classroom classroom) {
        if(classroomDAO.exists(classroom.getId())){
            try{
                classroomDAO.save(classroom);
                return true;
            } catch (Exception e) {
                logger.error("Error trying to update classroom: {}", e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
        }
        else{
            throw new ResourceNotFoundException("Classroom not found for ID: " + classroom.getId());
        }
    }
}
