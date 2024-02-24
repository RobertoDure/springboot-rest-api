package pt.com.springboot.api.endpoint;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.model.Teacher;
import pt.com.springboot.api.repository.TeacherRepository;
import javax.validation.Valid;

@RestController
@RequestMapping("v1/teachers")
public class TeacherEndpoint {

    private final TeacherRepository teacherDAO;

    @Autowired
    public TeacherEndpoint(TeacherRepository teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @GetMapping
    @ApiOperation(value = "Return a list with all teachers", response = Teacher[].class)
    public ResponseEntity<?> listAll(Pageable pageable) {
        return new ResponseEntity<>(teacherDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id, Authentication authentication) {
        System.out.println(authentication);
        verifyIfStudentExists(id);
        Teacher teacher = teacherDAO.findOne(id);
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Teacher teacher) {
        return new ResponseEntity<>(teacherDAO.save(teacher),HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody Teacher teacher) {
        verifyIfStudentExists(teacher.getId());
        teacherDAO.save(teacher);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfStudentExists(id);
        teacherDAO.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyIfStudentExists(Long id){
        if (teacherDAO.findOne(id) == null)
            throw new ResourceNotFoundException("Teacher not found for ID: "+id);
    }
}
