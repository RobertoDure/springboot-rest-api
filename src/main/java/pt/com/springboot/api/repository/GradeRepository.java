package pt.com.springboot.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pt.com.springboot.api.model.Grade;


public interface GradeRepository extends PagingAndSortingRepository<Grade, Long> {
}
