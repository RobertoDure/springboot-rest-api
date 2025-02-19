package pt.com.springboot.api.controller;

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
import pt.com.springboot.api.model.Classroom;
import pt.com.springboot.api.repository.ClassroomRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/classrooms")
public class ClassroomController {

    private final ClassroomRepository classDAO;

    @Autowired
    public ClassroomController(ClassroomRepository classDAO) {
        this.classDAO = classDAO;
    }

    @GetMapping
    @ApiOperation(value = "Return a list with all Classrooms", response = Classroom[].class)
    public ResponseEntity<?> listAll(Pageable pageable) {
        return new ResponseEntity<>(classDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id, Authentication authentication) {
        System.out.println(authentication);
        verifyIfStudentExists(id);
        Classroom classroom = classDAO.findOne(id);
        return new ResponseEntity<>(classroom, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Classroom classroom) {
        return new ResponseEntity<>(classDAO.save(classroom),HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody Classroom classroom) {
        verifyIfStudentExists(classroom.getId());
        classDAO.save(classroom);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyIfStudentExists(Long id){
        if (classDAO.findOne(id) == null)
            throw new ResourceNotFoundException("Teacher not found for ID: "+id);
    }
}
