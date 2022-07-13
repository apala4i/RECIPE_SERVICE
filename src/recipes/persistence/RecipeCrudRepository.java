package recipes.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import recipes.busineslayer.Recipe;

import java.util.Collection;

@Repository
public interface RecipeCrudRepository extends CrudRepository<Recipe, Long> {


    //FIXME: add key param to the request
    @Query(value = "SELECT * FROM Recipe WHERE UPPER(name) LIKE UPPER(concat('%',:name,'%')) ORDER BY date DESC",
            nativeQuery = true)
    Collection<Recipe> findAllWithNameInclude(@Param("name") String key);

    @Query(value="SELECT * FROM Recipe WHERE UPPER(category) = UPPER(:category) ORDER BY date DESC",
            nativeQuery=true)
    Collection<Recipe> findAllWithCategory(@Param("category") String key);



}
