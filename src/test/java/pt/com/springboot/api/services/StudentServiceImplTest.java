package pt.com.springboot.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.model.Grade;
import pt.com.springboot.api.model.Lecture;
import pt.com.springboot.api.model.Student;
import pt.com.springboot.api.repository.LectureRepository;
import pt.com.springboot.api.repository.StudentRepository;
import pt.com.springboot.api.service.impl.StudentServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentDAO;

    @Mock
    private LectureRepository lectureDAO;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetStudentQueryFilterById() {
        Student student = new Student();
        student.setId(1L);
        when(studentDAO.findOne(1L)).thenReturn(student);

        List<Student> result = studentService.getStudentQueryFilter("id", "1");

        assertEquals(1, result.size());
        assertEquals(student, result.get(0));
    }

    @Test
    void testGetStudentQueryFilterByIdNotFound() {
        when(studentDAO.findOne(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentQueryFilter("id", "1"));
    }

    @Test
    void testGetStudentQueryFilterByName() {
        List<Student> students = Arrays.asList(new Student(), new Student());
        when(studentDAO.findByNameIgnoreCaseContaining("test")).thenReturn(students);

        List<Student> result = studentService.getStudentQueryFilter("name", "test");

        assertEquals(students, result);
    }

    @Test
    void testGetStudentQueryFilterByNameNotFound() {
        when(studentDAO.findByNameIgnoreCaseContaining("test")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentQueryFilter("name", "test"));
    }

    @Test
    void testListAll() {
        Page<Student> expectedPage = new PageImpl<>(Collections.singletonList(new Student()));
        when(studentDAO.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Student> result = studentService.listAll(new PageRequest(0, 10));

        assertEquals(expectedPage, result);
    }

    @Test
    void testListAllException() {
        when(studentDAO.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(ResourceNotFoundException.class, () -> studentService.listAll(new PageRequest(0, 10)));
    }

    @Test
    void testSaveStudent() {
        Student student = new Student();
        student.setIdTeacher(1L);
        student.setGuardianName("John Doe");
        student.setGuardianContact("123456789");
        Grade grade = new Grade();
        grade.setGrade(1);
        grade.setGradeComment("Test");
        grade.setGradeDate(new Date());
        student.setGrades(Arrays.asList(grade));
        student.setLectures(Arrays.asList(new Lecture(), new Lecture()));

        when(studentDAO.save(any(Student.class))).thenReturn(student);

        assertTrue(studentService.saveStudent(student));
    }

    @Test
    void testSaveStudentException() {
        Student student = new Student();
        when(studentDAO.save(any(Student.class))).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(InternalServerErrorException.class, () -> studentService.saveStudent(student));
    }

    @Test
    void testDeleteStudent() {
        when(studentDAO.exists(1L)).thenReturn(true);
        doNothing().when(studentDAO).delete(1L);

        assertTrue(studentService.deleteStudent("1"));
    }

    @Test
    void testDeleteStudentNotFound() {
        when(studentDAO.exists(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent("1"));
    }

    @Test
    void testDeleteStudentException() {
        when(studentDAO.exists(1L)).thenReturn(true);
        doThrow(new RuntimeException("Test Exception")).when(studentDAO).delete(1L);

        assertThrows(InternalServerErrorException.class, () -> studentService.deleteStudent("1"));
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setId(1L);
        when(studentDAO.exists(1L)).thenReturn(true);
        when(studentDAO.save(any(Student.class))).thenReturn(student);
        student.setIdTeacher(1L);
        student.setGuardianName("John Doe");
        student.setGuardianContact("123456789");
        Grade grade = new Grade();
        grade.setGrade(1);
        grade.setGradeComment("Test");
        grade.setGradeDate(new Date());
        student.setGrades(Arrays.asList(grade));
        student.setLectures(Arrays.asList(new Lecture(), new Lecture()));

        assertTrue(studentService.updateStudent(student));
    }

    @Test
    void testUpdateStudentNotFound() {
        Student student = new Student();
        student.setId(1L);
        when(studentDAO.exists(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(student));
    }

    @Test
    void testUpdateStudentException() {
        Student student = new Student();
        student.setId(1L);
        when(studentDAO.exists(1L)).thenReturn(true);
        when(studentDAO.save(any(Student.class))).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(InternalServerErrorException.class, () -> studentService.updateStudent(student));
    }
}