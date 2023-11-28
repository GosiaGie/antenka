package pl.volleylove.antenka.playerprofile;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.volleylove.antenka.benefit.BenefitService;
import pl.volleylove.antenka.entity.PlayerProfile;
import pl.volleylove.antenka.entity.Slot;
import pl.volleylove.antenka.entity.User;
import pl.volleylove.antenka.enums.PlayerProfileInfo;
import pl.volleylove.antenka.event.match.SlotService;
import pl.volleylove.antenka.repository.PlayerProfileRepository;
import pl.volleylove.antenka.user.UserService;
import pl.volleylove.antenka.user.auth.AuthService;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Service
public class PlayerProfileService {

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private BenefitService benefitService;

    @Autowired
    private SlotService slotService;

    @Transactional
    public PlayerProfileResponse addOrUpdateProfile(PlayerProfileRequest request){

        User user = userService.findByID(authService.getAuthenticatedUserID());

        PlayerProfile playerProfile = findUsersProfile(user);

        if(playerProfile==null){
            playerProfile = PlayerProfile.builder().build();

        }
        else {
            //player can't change his profile if is signed up for a match
            Set<Slot> slotsPlayerApplied = slotService.findPlayersSlots(playerProfile);

            if(slotsPlayerApplied.size()!= 0) {
                return PlayerProfileResponse.builder()
                        .info(PlayerProfileInfo.YOU_ARE_SIGNED_UP_FOR_MATCH)
                        .build();
            }

            if(!benefitService.isCorrect(request.getBenefitCardNumber())) {
                return PlayerProfileResponse.builder()
                        .info(PlayerProfileInfo.INCORRECT_BENEFIT_NUMBER)
                        .build();
            }

        }

        playerProfile.setUser(user);
        playerProfile.setPositions(request.getPositions());
        playerProfile.setLevel(request.getLevel());
        playerProfile.setGender(request.getGender());
        playerProfile.setBenefitCardNumber(request.getBenefitCardNumber());



       return PlayerProfileResponse.builder()
               .info(PlayerProfileInfo.OK)
               .playerProfile(playerProfileRepository.save(playerProfile))
               .build();


    }

    public int calculateAge(LocalDate birthDate) {

        LocalDate currentDate = LocalDate.now();

        if ((birthDate != null && birthDate.isBefore(currentDate))) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    public PlayerProfile findUsersProfile(User user){
        return playerProfileRepository.findByUser(user);
    }



}
