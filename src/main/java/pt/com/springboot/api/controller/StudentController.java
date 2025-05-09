package pt.com.springboot.api.controller;

import io.swagger.annotations.Api;
import org.slf4j.MDC;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.com.springboot.api.error.BadRequestException;
import pt.com.springboot.api.model.Student;
import pt.com.springboot.api.service.StudentService;
import pt.com.springboot.api.util.HttpHeadersUtil;
import pt.com.springboot.api.util.ServiceValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * Methods POST, PUT and DELETE are only allowed for users with ADMIN role
 */
@RestController
@RequestMapping("api/v1/student")
@Api(value = "Student Endpoint", description = "A REST API for students", tags = {"Student Endpoint"})
public class StudentController {

    final private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * List all students
     *
     * @param page is the page number
     * @param size is the number of elements per page
     *             Default values are page = 1 and size = 10
     *             Example: <a href="http://localhost:8080/api/v1/student?page=1&size=10">...</a>
     *             Example: <a href="http://localhost:8080/api/v1/student?page=2&size=5">...</a>
     * @return List of students
     */
    @GetMapping
    public ResponseEntity<?> listAll(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        Pageable pageable = new PageRequest(page, size);
        return new ResponseEntity<>(studentService.listAll(pageable), HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }

    /**
     * Get student by id and name filter
     *
     * @param filter      = id or name
     * @param filterValue =
     *                    if filter = id, filterValue = Long
     *                    if filter = name, filterValue = String
     * @return List of students
     */
    @GetMapping(path = "/query")
    public ResponseEntity<?> getStudentQuery(@RequestParam(value = "filter", required = false) String filter,
                                             @RequestParam(value = "filterValue", required = false) String filterValue) {
        if (ServiceValidator.filterValidation(filter, filterValue)) {
            throw new BadRequestException("Filter not Valid: " + filter);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        List<Student> student = studentService.getStudentQueryFilter(filter, filterValue);
        return new ResponseEntity<>(student, HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        boolean savedStudent = studentService.saveStudent(student);
        if (!savedStudent) {
            throw new BadRequestException("Student not saved: " + student);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        return new ResponseEntity<>(HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (ServiceValidator.idValid(id)) {
            throw new BadRequestException("ID not Valid: " + id);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody Student student) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        studentService.updateStudent(student);
        return new ResponseEntity<>(HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }

}