package pl.volleylove.antenka.user.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.volleylove.antenka.entity.User;
import pl.volleylove.antenka.enums.RegisterInfo;
import pl.volleylove.antenka.enums.Role;
import pl.volleylove.antenka.user.UserService;

import java.util.List;

@Service
public class RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RegisterValidator registerValidator;


    public RegisterResponse registerAttempt(RegisterRequest registerRequest) {

        // credentials check
        List<RegisterInfo> info = registerValidator.validate(registerRequest);


        if(info.contains(RegisterInfo.OK)){

            User user = User.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .birthday(registerRequest.getBirthday())
                    .role(Role.ROLE_USER)
                    .build();

            userService.save(user);
        }

        return RegisterResponse.builder()
                .email(registerRequest.getEmail())
                .registerInfo(info).build();

    }





}
