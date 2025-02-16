package pt.com.springboot.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.com.springboot.api.model.Teacher;

import java.util.List;

public interface TeacherService {

    List<Teacher> getTeacherQueryFilter(String filter, String filterValue);
    Page<Teacher> listAll(Pageable pageable);
    boolean saveTeacher(Teacher teacher);
    boolean deleteTeacher(Long id);
    boolean updateTeacher(Teacher teacher);
}
