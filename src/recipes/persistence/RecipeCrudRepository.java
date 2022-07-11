package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.busineslayer.Recipe;

@Repository
public interface RecipeCrudRepository extends CrudRepository<Recipe, Long> {
}
