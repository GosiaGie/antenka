package pl.volleylove.antenka.event.match;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import pl.volleylove.antenka.benefit.BenefitService;
import pl.volleylove.antenka.entity.*;
import pl.volleylove.antenka.enums.AddressType;
import pl.volleylove.antenka.enums.CloseReason;
import pl.volleylove.antenka.enums.FindMatchInfo;
import pl.volleylove.antenka.enums.SignUpInfo;
import pl.volleylove.antenka.event.PlayerWanted;
import pl.volleylove.antenka.event.match.add.AddMatchRequest;
import pl.volleylove.antenka.event.match.add.AddMatchResponse;
import pl.volleylove.antenka.event.match.find.FindMatchRequest;
import pl.volleylove.antenka.event.match.find.FindMatchResponse;
import pl.volleylove.antenka.event.match.find.FindSlotsResponse;
import pl.volleylove.antenka.event.match.signup.SignUpForMatchRequest;
import pl.volleylove.antenka.event.match.signup.SignUpForMatchResponse;
import pl.volleylove.antenka.map.LocationService;
import pl.volleylove.antenka.playerprofile.PlayerProfileService;
import pl.volleylove.antenka.repository.MatchRepository;
import pl.volleylove.antenka.user.UserService;
import pl.volleylove.antenka.user.auth.AuthService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//class for business logic
@Service
//main service class for Match
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;


    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerProfileService playerProfileService;

    @Autowired
    private BenefitService benefitService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private SlotService slotService;

    @Transactional
    public AddMatchResponse add(AddMatchRequest request, Errors errors) throws IOException, InterruptedException {

        if (errors.hasErrors()) {
            List<String> errList = new LinkedList<>();

            for (ObjectError err:errors.getAllErrors()) {
                errList.add(err.getDefaultMessage());
            }

            return AddMatchResponse.builder()
                    .addMatchInfo(errList)
                    .build();
        }

        //1. getting authenticated userID
        Long authenticatedUserID = authService.getAuthenticatedUserID();

        //2. after getting userID, we find user and set him as organizer
        User userOrganizer = userService.findByID(authenticatedUserID);

        //3. finding geo data and address type
        Address address = locationService.setLocationInAddress(request.getAddress());
        address.setAddressType(AddressType.EVENT);


        Match match = Match.builder()
                .organizer(userOrganizer)
                .address(address)
                .name(request.getName())
                .dateTime(request.getDateTime())
                .price(request.getPrice())
                .playersNum(request.getPlayersNum())
                .freeSlots(request.getPlayersNum()) //because at the start freeSlots = playersNum
                .isOpen(true)
                .build();

        Match savedMatch = matchRepository.save(match);

        // 4. after match got ID in DB, we can save slots for players - now they can have matchID
        savedMatch.setSlots(slotService.saveSlotsInMatch(savedMatch.getEventID(), request.getPlayers()));

        return AddMatchResponse.builder()
                .addMatchInfo(List.of("OK"))
                .match(savedMatch)
                .build();

    }


    public FindMatchResponse findMatch(FindMatchRequest request) {

        PlayerProfile playerProfile = getLoggedUsersPlayerProfile();

        if (playerProfile == null) {
            return FindMatchResponse
                    .builder()
                    .findMatchInfo(FindMatchInfo.COMPLETE_PLAYER_OR_TEAM_PROFILE)
                    .build();
        }

        Set <Match> matches;

        if(playerProfile.isActiveBenefit()){
            matches = matchRepository.findByPlayerWantedBenefit(
                    playerProfile.getGender(),
                    playerProfile.getLevel(),
                    playerProfile.getAge(),
                    playerProfile.getPositions(),
                    request.getMaxPrice());
        }
        else {
            matches = matchRepository.findByPlayerWantedNoBenefit(
                    playerProfile.getGender(),
                    playerProfile.getLevel(),
                    playerProfile.getAge(),
                    playerProfile.getPositions(),
                    request.getMaxPrice());
        }

        return FindMatchResponse.builder()
                .findMatchInfo(FindMatchInfo.OK)
                .matches(matches)
                .build();
    }

    //returning SLOTS and match info
    public FindSlotsResponse findSlots(FindMatchRequest request) {

        PlayerProfile playerProfile = getLoggedUsersPlayerProfile();

        if (playerProfile == null) {
            return FindSlotsResponse
                    .builder()
                    .info(FindMatchInfo.COMPLETE_PLAYER_OR_TEAM_PROFILE)
                    .build();
        }


        Set<Slot> slots = slotService.findSlots(playerProfile.getGender(), playerProfile.getLevel(), playerProfile.getAge(),
                playerProfile.getPositions(), request.getMaxPrice(), playerProfile.isActiveBenefit());

        return FindSlotsResponse
                .builder()
                .info(FindMatchInfo.OK)
                .slots(slots)
                .build();

    }

    @Transactional
    public SignUpForMatchResponse signUpForMatch(SignUpForMatchRequest request) {


        //1. checking if the user has player profile - only users with player profile can apply for a match
        PlayerProfile playerProfile = getLoggedUsersPlayerProfile();

        if(playerProfile==null) {
            return SignUpForMatchResponse.builder()
                    .info(SignUpInfo.COMPLETE_PLAYER_OR_TEAM_PROFILE)
                    .build();
        }

        //2. the event type - Match, which contains particular slot for player, here is used to find its slot
        Match match = Match.builder()
                .eventID(request.getEventID())
                .build();


        Slot slotToSignUp = slotService.findSlotByMatchAndOrderNum(match, request.getSlotNum());

        if(slotToSignUp==null) {
            return SignUpForMatchResponse.builder()
                    .info(SignUpInfo.INCORRECT_EVENT_ID_OR_SLOT_NUM)
                    .build();
        }
        else {
            match = slotToSignUp.getMatch();
        }


        //3. checking if the event is open - organizer 1) didn't close it 2) event has any slot without player
        if(!slotToSignUp.getMatch().isOpen()) {
            return SignUpForMatchResponse.builder()
                    .info(SignUpInfo.EVENT_IS_CLOSED)
                    .build();
        }


        //4. checking if slot does not have player already
        if(!(slotToSignUp.getPlayerApplied()==null)) {
            return SignUpForMatchResponse.builder()
                    .info(SignUpInfo.SLOT_HAS_PLAYER_ALREADY)
                    .build();
        }

        //5. for every match player can only sign up once
        if(isPlayerSignUpForThisMatch(match, playerProfile)) {
            return SignUpForMatchResponse.builder()
                    .info(SignUpInfo.YOU_ARE_SINGED_UP_FOR_THIS_EVENT)
                    .build();
        }
        //6. checking if this user, with this player profile can sign in for this match's slot
        //comparing player's profile with player wanted for this slot
        //*level
        //*gender
        //*age
        //*positions
        PlayerWanted playerWanted = slotToSignUp.getPlayerWanted();

        if(!isPlayerMeetReq(playerProfile, playerWanted)) {
            return SignUpForMatchResponse.builder()
                    .info(SignUpInfo.YOU_DO_NOT_MEET_EVENT_REQ)
                    .build();
        }
        else {
            //1. sign up for this match and its particular slot
            //2. change a number of free slots -1; if it's last free slot, then close a match for signing up

            match.setFreeSlots(match.getFreeSlots()-1);

            if(match.getFreeSlots()==0) {
                closeMatchOutOfFreeSlots(match);
            }

            return SignUpForMatchResponse.builder()
                    .info(SignUpInfo.OK)
                    .slot(slotService.savePlayerApp(slotToSignUp, playerProfile))
                    .build();
        }

    }


    //additional, useful methods:

    public Match closeMatchOutOfFreeSlots(Match match) {
        match.setCloseReason(CloseReason.OUT_OF_PLAYER_SLOTS);
        match.setOpen(false);
        return matchRepository.save(match);
    }

    private boolean isPlayerSignUpForThisMatch(Match match, PlayerProfile playerProfile) {

        for(Slot slot: match.getSlots()) {
            if(playerProfile.equals(slot.getPlayerApplied())) {
                return true;
            }
        }
        return false;
    }


    public PlayerProfile getLoggedUsersPlayerProfile(){

        //1.setting user from request
        User user = userService.findByID(authService.getAuthenticatedUserID());

        //2. finding users's player profile in DB
        PlayerProfile playerProfile = playerProfileService.findUsersProfile(user);


        if (playerProfile == null) {
            return null;
        }

        //3. setting user's age - age isn't stored in DB, only birthday
        playerProfile.setAge(playerProfileService.calculateAge(user.getBirthday()));
        //4. setting user's benefit status, basing on the benefit card number; if is active, then only benefit price will be checked
        playerProfile.setActiveBenefit(benefitService.isActive(playerProfile.getBenefitCardNumber()));

        return playerProfile;

    }

    private boolean isPlayerMeetReq(PlayerProfile playerProfile, PlayerWanted playerWanted) {

         return playerWanted.getLevel() == playerProfile.getLevel() //level
                && playerWanted.getGender() == playerProfile.getGender() //gender
                && playerProfile.getPositions().contains(playerWanted.getPosition()) //position
                && (playerWanted.getAgeRange().getAgeMin() <= playerProfile.getAge()  //age
                && playerWanted.getAgeRange().getAgeMax() >= playerProfile.getAge());

    }



}
