package recipes.busineslayer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.UserCrudRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserCrudRepository userCrudRepository;

    @Autowired
    public UserService(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    public User save(User user) {
        return userCrudRepository.save(user);
    }

    public boolean userContainsRecipeId(String username, Long Id) {
        var user =  findById(username);
        return user.map(value -> value.getAddedRecipes().contains(Id)).orElse(false);
    }

    public Optional<User> findById(String username) {
        return userCrudRepository.findById(username);
    }

    public boolean existsById(String username) {
        return userCrudRepository.existsById(username);
    }



}
