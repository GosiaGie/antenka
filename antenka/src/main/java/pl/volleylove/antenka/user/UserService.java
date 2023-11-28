package pl.volleylove.antenka.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.volleylove.antenka.entity.User;
import pl.volleylove.antenka.repository.UserRepository;
import pl.volleylove.antenka.repository.UserRepositoryCustom;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {

        User user = userRepository.findUserByEmail(email);

        if(user!=null){
            var userVar = User.builder().build();
            userVar.setUserID(user.getUserID());
            userVar.setEmail(user.getEmail());
            userVar.setPassword(user.getPassword());
            userVar.setRole(user.getRole());
            return Optional.of(userVar);
        }
        else {
            System.out.println("user not found");
            return Optional.empty();
        }
    }

    public User findByID(Long ID){

        Optional<User> userOptional = userRepository.findById(ID);

        return User.builder()
                .userID(userOptional.orElseThrow().getUserID())
                .email(userOptional.orElseThrow().getEmail())
                .password(userOptional.orElseThrow().getPassword())
                .birthday(userOptional.orElseThrow().getBirthday())
                .firstName(userOptional.orElseThrow().getFirstName())
                .lastName(userOptional.orElseThrow().getLastName())
                .role(userOptional.orElseThrow().getRole())
                .playerProfile(userOptional.get().getPlayerProfile())
                .build();


    }


}
