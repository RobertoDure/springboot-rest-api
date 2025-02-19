package pt.com.springboot.api.service;

import pt.com.springboot.api.model.Lecture;

import java.util.List;

public interface LectureService {

    boolean saveLecture(Lecture lecture);
    List<Lecture> listAll();
    boolean deleteLecture(String id);
    Lecture findLectureById(String id);
    boolean updateLecture(Lecture lecture);
}
