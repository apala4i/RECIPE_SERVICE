package recipes.presentationlayer;


import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.busineslayer.Recipe;
import recipes.busineslayer.RecipeService;

import javax.validation.Valid;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
