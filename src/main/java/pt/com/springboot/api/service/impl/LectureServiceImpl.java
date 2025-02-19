package pt.com.springboot.api.service.impl;

import org.springframework.stereotype.Service;
import pt.com.springboot.api.model.Lecture;
import pt.com.springboot.api.service.LectureService;

import java.util.Collections;
import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {

    @Override
    public boolean saveLecture(Lecture lecture) {
        return false;
    }

    @Override
    public List<Lecture> listAll() {
        return Collections.emptyList();
    }

    @Override
    public boolean deleteLecture(String id) {
        return false;
    }

    @Override
    public Lecture findLectureById(String id) {
        return null;
    }

    @Override
    public boolean updateLecture(Lecture lecture) {
        return false;
    }
}
