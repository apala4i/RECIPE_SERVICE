package recipes.busineslayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.RecipeCrudRepository;

import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeCrudRepository repository;

    @Autowired
    public RecipeService(RecipeCrudRepository repository) {
        this.repository = repository;
    }

    public Optional<Recipe> findById(Long id) {
        return repository.findById(id);
    }

    public Recipe save(Recipe recipe) {
        return repository.save(recipe);
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public boolean existsById(long id) { return repository.existsById(id); }
}
