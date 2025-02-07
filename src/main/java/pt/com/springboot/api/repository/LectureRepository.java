package pt.com.springboot.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pt.com.springboot.api.model.Lecture;

public interface LectureRepository extends PagingAndSortingRepository<Lecture, Long> {
}
