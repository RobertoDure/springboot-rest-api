package pt.com.springboot.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.com.springboot.api.error.BadRequestException;
import pt.com.springboot.api.model.Teacher;
import pt.com.springboot.api.service.TeacherService;
import pt.com.springboot.api.util.ServiceValidator;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/teacher")
@Api(value = "Teacher Endpoint", description = "A REST API for Teacher", tags = {"Teacher Endpoint"})
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherDAO) {
        this.teacherService = teacherDAO;
    }

    @GetMapping
    @ApiOperation(value = "Return a list with all teachers", response = Teacher[].class)
    public ResponseEntity<?> listAll(Pageable pageable) {
        return new ResponseEntity<>(teacherService.listAll(pageable), HttpStatus.OK);
    }

    /**
     * Get teacher by id and name filter
     * @param filter  = id or name
     * @param filterValue =
     *                    if filter = id, filterValue = Long
     *                    if filter = name, filterValue = String
     * @return List of students
     */
    @GetMapping (path = "/query")
    public ResponseEntity<?> getTeacherQuery(@RequestParam(value = "filter", required = false) String filter,
                                             @RequestParam(value = "filterValue", required = false) String filterValue) {
        if(ServiceValidator.filterValidation(filter, filterValue)){
            throw new BadRequestException("Filter not Valid: " + filter);
        }
        List<Teacher> teacher = teacherService.getTeacherQueryFilter(filter, filterValue);
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Save a teacher", response = Teacher.class)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Teacher teacher) {
        return new ResponseEntity<>(teacherService.saveTeacher(teacher),HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update a teacher", response = Teacher.class)
    public ResponseEntity<?> update(@RequestBody Teacher teacher) {
        teacherService.updateTeacher(teacher);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete a teacher")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
