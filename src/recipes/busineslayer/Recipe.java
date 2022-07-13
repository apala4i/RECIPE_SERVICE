package recipes.busineslayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recipe {
    @Id
    @JsonIgnore
    @GeneratedValue
    Long id;


    @NotBlank
    String name;

    @NotBlank
    String description;

    @Size(min=1)
    @NotNull
    @ElementCollection
    List<String> ingredients;

    @Size(min=1)
    @NotNull
    @ElementCollection
    List<String> directions;

    @NotBlank
    String category;

    LocalDateTime date;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", directions=" + directions +
                '}';
    }

    public void copyFields(Recipe recipe) {
        setCategory(recipe.getCategory());
        setDescription(recipe.getDescription());
        setDirections(recipe.getDirections());
        setIngredients(recipe.getIngredients());
        setName(recipe.getName());
    }
}
