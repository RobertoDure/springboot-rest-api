package pt.com.springboot.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.com.springboot.api.model.Classroom;

import java.util.List;

public interface ClassroomService {

    Page<Classroom> listAll(Pageable pageable);
    boolean saveClassroom(Classroom classroom);
    boolean deleteClassroom(Long id);
    boolean updateClassroom(Classroom classroom);
}
