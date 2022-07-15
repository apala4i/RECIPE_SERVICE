package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.busineslayer.security.User;

@Repository
public interface UserCrudRepository extends CrudRepository<User, String> {
}
