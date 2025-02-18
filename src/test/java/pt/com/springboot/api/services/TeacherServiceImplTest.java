package pt.com.springboot.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.model.Teacher;
import pt.com.springboot.api.repository.TeacherRepository;
import pt.com.springboot.api.service.impl.TeacherServiceImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImplTest.class.getName());

    @Mock
    private TeacherRepository teacherDAO;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetTeacherQueryFilterById() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherDAO.findOne(1L)).thenReturn(teacher);

        List<Teacher> result = teacherService.getTeacherQueryFilter("id", "1");

        assertEquals(1, result.size());
        assertEquals(teacher, result.get(0));
    }

    @Test
    void testGetTeacherQueryFilterByIdNotFound() {
        when(teacherDAO.findOne(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> teacherService.getTeacherQueryFilter("id", "1"));
    }

    @Test
    void testGetTeacherQueryFilterByName() {
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        when(teacherDAO.findByNameIgnoreCaseContaining("test")).thenReturn(teachers);

        List<Teacher> result = teacherService.getTeacherQueryFilter("name", "test");

        assertEquals(teachers, result);
    }


    @Test
    void testGetTeacherQueryFilterByNameNotFound() {
        when(teacherDAO.findByNameIgnoreCaseContaining("test")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> teacherService.getTeacherQueryFilter("name", "test"));
    }


    @Test
    void testListAll() {
        Page<Teacher> expectedPage = new PageImpl<>(Collections.singletonList(new Teacher()));
        when(teacherDAO.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Teacher> result = teacherService.listAll(new PageRequest(0, 10));

        assertEquals(expectedPage, result);
    }

    @Test
    void testListAllException() {
        when(teacherDAO.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(ResourceNotFoundException.class, () -> teacherService.listAll(new PageRequest(0, 10)));
    }

    @Test
    void testSaveTeacher() {
        Teacher teacher = new Teacher();
        when(teacherDAO.save(teacher)).thenReturn(teacher);

        assertTrue(teacherService.saveTeacher(teacher));
    }

    @Test
    void testSaveTeacherException() {
        Teacher teacher = new Teacher();
        when(teacherDAO.save(teacher)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(InternalServerErrorException.class, () -> teacherService.saveTeacher(teacher));
    }

    @Test
    void testDeleteTeacher() {
        when(teacherDAO.exists(1L)).thenReturn(true);
        doNothing().when(teacherDAO).delete(1L);

        assertTrue(teacherService.deleteTeacher(1L));
    }

    @Test
    void testDeleteTeacherNotFound() {
        when(teacherDAO.exists(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> teacherService.deleteTeacher(1L));
    }


    @Test
    void testDeleteTeacherException() {
        when(teacherDAO.exists(1L)).thenReturn(true);
        doThrow(new RuntimeException("Test Exception")).when(teacherDAO).delete(1L);

        assertThrows(InternalServerErrorException.class, () -> teacherService.deleteTeacher(1L));
    }

    @Test
    void testUpdateTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherDAO.exists(1L)).thenReturn(true);
        when(teacherDAO.save(teacher)).thenReturn(teacher);

        assertTrue(teacherService.updateTeacher(teacher));
    }

    @Test
    void testUpdateTeacherNotFound() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherDAO.exists(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> teacherService.updateTeacher(teacher));
    }

    @Test
    void testUpdateTeacherException() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherDAO.exists(1L)).thenReturn(true);
        when(teacherDAO.save(teacher)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(InternalServerErrorException.class, () -> teacherService.updateTeacher(teacher));
    }
}