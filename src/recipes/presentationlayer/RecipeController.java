package recipes.presentationlayer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.busineslayer.Recipe;
import recipes.busineslayer.RecipeService;
import recipes.busineslayer.security.User;
import recipes.busineslayer.security.UserDetailsImpl;
import recipes.busineslayer.security.UserService;

import java.util.*;

import javax.validation.Valid;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class RecipeController {

    private final RecipeService recipeService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;



    @Autowired
    public RecipeController(RecipeService recipeService,
                            UserService userService,
                            PasswordEncoder passwordEncoder) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("recipe/{id}")
    public Recipe getRecipeById(@PathVariable long id) {
        if (!recipeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return recipeService.findById(id).orElse(new Recipe());
    }


    @GetMapping("recipe/search")
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

    @PutMapping("recipe/{id}")
    public void updateRecipe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             @PathVariable long id,
                             @RequestBody @Valid Recipe recipe) {
        if (!recipeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            if (userService.userContainsRecipeId(userDetails.getUsername(), id)) {
                var realRecipe = recipeService.findById(id).get();
                realRecipe.copyFields(recipe);
                recipeService.save(realRecipe);
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }

    @DeleteMapping("recipe/{id}")
    public void deleteRecipeById(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable long id) {
        if (recipeService.existsById(id)) {
            if (userService.userContainsRecipeId(userDetails.getUsername(), id)) {
                recipeService.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("recipe/new")
    public AbstractMap.SimpleEntry<String, Long> addRecipe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody @Valid Recipe recipe) {
        Recipe savedRecipe = recipeService.save(recipe);
        User user = userService.findById(userDetails.getUsername()).get();
        user.getAddedRecipes().add(savedRecipe.getId());
        userService.save(user);
        return new AbstractMap.SimpleEntry<>("id", savedRecipe.getId());
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid User user) {
        if (userService.findById(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
        }
    }
}
