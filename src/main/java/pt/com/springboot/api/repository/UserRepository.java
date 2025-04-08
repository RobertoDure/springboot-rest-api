package pt.com.springboot.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pt.com.springboot.api.model.User;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);

    List<User> findAll();

    User findByEmail(String username);
}
