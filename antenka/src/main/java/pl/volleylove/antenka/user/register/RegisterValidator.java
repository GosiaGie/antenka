package pl.volleylove.antenka.user.register;

import org.springframework.beans.factory.annotation.Autowired;
import pl.volleylove.antenka.enums.RegisterInfo;

import pl.volleylove.antenka.user.UserService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RegisterValidator {

    private final int minEmailLength = 8;
    private final int minPassLength = 8;
    private final List<CharSequence> emailSpecialCharSeq = Arrays.asList(new String[]{"@", "."});
    private final int[][] specialCharAscii = new int[][]{{32, 47}, {58, 65}, {91, 96}, {123, 126}};
    private final int minAge = 16;
    private final int maxAge = 150;

    @Autowired
    private UserService userService;

    private List<RegisterInfo> info = new LinkedList<>();

    protected List<RegisterInfo> validate(RegisterRequest registerRequest) {

        info.clear();

        checkCredentialsReq(registerRequest.getEmail(), registerRequest.getPassword());
        checkName(registerRequest.getFirstName(), registerRequest.getLastName());
        checkBirthday(registerRequest.getBirthday());

        if (info.isEmpty()) {
            info.add(RegisterInfo.OK);
        }
        return info;

    }

    private void checkBirthday(LocalDate birthday) {

        if (!isBirthdayCorrect(birthday)) {
            info.add(RegisterInfo.AGE_UNDER_16);
        }

    }

    private void checkName(String firstName, String lastName) {
        if (!isNameCorrect(firstName, lastName)) {
            info.add(RegisterInfo.INCORRECT_NAME);
        }
    }


    private void checkCredentialsReq(String email, String password) {

        //password requirements
        if (!isPasswordMeetingReq(password)) {
            info.add(RegisterInfo.PASSWORD_DOES_NOT_MEET_REQ);
        }

        //email - is correct?
        if (!isEmailCorrectFormat(email)) {
            info.add(RegisterInfo.INCORRECT_EMAIL);
        }  //if yes,is unique?
        else if (userService.findByEmail(email).isPresent()) {
            info.add(RegisterInfo.EMAIL_ALREADY_EXISTS);
        }

    }


    protected boolean isEmailCorrectFormat(String email) {

        for (CharSequence sequence : emailSpecialCharSeq) {
            if (!email.contains(sequence)) {
                return false;
            }
        }

        //if email has all requirement chars seq, then check length
        return email.length() >= minEmailLength;

    }


    private boolean isPasswordMeetingReq(String password) {

        return password.length() >= minPassLength && (password.chars().anyMatch(Character::isDigit)) && hasPasswordSpecialCharacter(password);

    }


    private boolean isNameCorrect(String firstName, String lastName) {

        return firstName.chars().allMatch(Character::isLetter) && lastName.chars().allMatch(Character::isLetter)
                && firstName.length() >= 3 && firstName.length() <= 20
                && lastName.length() >= 3 && lastName.length() <= 20;
    }

    private boolean hasPasswordSpecialCharacter(String password) {

        for (char c : password.toCharArray()) {
            for (int[] a : specialCharAscii) {
                if (c > a[0] && c < a[1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBirthdayCorrect(LocalDate birthday) {

        LocalDate lastDateAllowed = LocalDate.now().minus(minAge, ChronoUnit.YEARS);
        LocalDate firstDateAllowed = LocalDate.now().minus(maxAge, ChronoUnit.YEARS);

        return birthday.isBefore(lastDateAllowed) && birthday.isAfter(firstDateAllowed);

    }

}


