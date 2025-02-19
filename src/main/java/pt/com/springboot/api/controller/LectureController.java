package pt.com.springboot.api.controller;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.com.springboot.api.error.BadRequestException;
import pt.com.springboot.api.model.Lecture;
import pt.com.springboot.api.service.LectureService;
import pt.com.springboot.api.util.HttpHeadersUtil;
import pt.com.springboot.api.util.ServiceValidator;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/lecture")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {

        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        return new ResponseEntity<>(lectureService.listAll(), HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") String id) {
        if(ServiceValidator.idValid(id)){
            throw new BadRequestException("ID not Valid: " + id);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        return new ResponseEntity<>(lectureService.findLectureById(id),
                HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Lecture lecture) {
        boolean lectureSaved = lectureService.saveLecture(lecture);
        if (!lectureSaved) {
            throw new BadRequestException("Lecture not saved: " + lectureSaved);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        return new ResponseEntity<>(HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(Lecture lecture) {

        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        lectureService.updateLecture(lecture);
        return new ResponseEntity<>(HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(String id) {

        if(ServiceValidator.idValid(id)){
            throw new BadRequestException("ID not Valid: " + id);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        lectureService.deleteLecture(id);
        return new ResponseEntity<>(HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }
}
