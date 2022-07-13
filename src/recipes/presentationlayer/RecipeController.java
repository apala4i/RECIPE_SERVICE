package recipes.presentationlayer;


import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.busineslayer.Recipe;
import recipes.busineslayer.RecipeService;

import java.util.*;

import javax.validation.Valid;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;





    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable long id) {
        if (!recipeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return recipeService.findById(id).orElse(new Recipe());
    }


    @GetMapping("/search")
    public Collection<Recipe> getByNameOrCategory(@RequestParam(required = false) String category,
                                                  @RequestParam(required = false) String name) {
        if (category == null && name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        synchronized (this){
            System.out.println("\n\n FINDING NAME = " + name != null ? name : "" + "\n\n");
            System.out.println("\n\n FINDING CATEGORY = " + category != null ? category : "" + "\n\n");
        }

        category = category == null ? "" : category;
        name = name == null ? "" : name;

        if (category.isEmpty() && name.isEmpty() ||
            !category.isEmpty() && !name.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!category.isEmpty()) {
            return recipeService.findAllWithCategory(category);
        }

        if (!name.isEmpty()) {
            return recipeService.findAllWithNameInclude(name);
        }

        return Collections.emptyList();

    }

    @PutMapping("/{id}")
    public void updateRecipe(@PathVariable long id, @RequestBody @Valid Recipe recipe) {
        if (!recipeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            var realRecipe = recipeService.findById(id).get();
            realRecipe.copyFields(recipe);
            recipeService.save(realRecipe);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/new")
    public AbstractMap.SimpleEntry<String, Long> addRecipe(@RequestBody @Valid Recipe recipe) {
        return new AbstractMap.SimpleEntry<>("id", recipeService.save(recipe).getId());
    }

    @DeleteMapping("/{id}")
    public void deleteRecipeById(@PathVariable long id) {
        if (recipeService.existsById(id)) {
            recipeService.deleteById(id);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
