package pt.com.springboot.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pt.com.springboot.api.model.Classroom;

public interface ClassroomRepository extends PagingAndSortingRepository<Classroom, Long> {


}
