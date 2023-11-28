package pl.volleylove.antenka.event.match.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import pl.volleylove.antenka.entity.Address;
import pl.volleylove.antenka.event.PlayerWanted;
import pl.volleylove.antenka.event.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Builder
public class AddMatchRequest {

    private final int ageMinAllowed = 16;
    private final int ageMaxAllowed = 150;

    @NotNull
    @Size(min = 3, max = 30, message = "Name has to contain from 3 to 30 characters")
    private String name;
    private LocalDateTime dateTime;
    private Price price;
    private Address address;
    @NotNull
    @Min(value = 1, message = "Match needs at least one player slot")
    @Max(value = 20, message = "Match can have max 20 players")
    private int playersNum;
    //Here I'm using List, not Set - to allow adding players with the same requirements
    @JsonProperty("players")
    private List<PlayerWanted> players;


    @AssertTrue(message = "Incorrect price. Price can't be under 0.0 and regular price can't be higher than benefit price")
    public boolean isPriceCorrect(){
        if(price==null || price.getRegularPrice()==null || price.getBenefitPrice()==null){
            return false;
        }
        int pReg = price.getRegularPrice().compareTo(new BigDecimal("0.0"));
        int pBen = price.getBenefitPrice().compareTo(new BigDecimal("0.0"));
        int regHigherThanBen = price.getRegularPrice().compareTo(getPrice().getBenefitPrice());

        return pReg>=0 && pBen>=0 && regHigherThanBen>=0;
    }

    @AssertTrue(message = "Date can't be past date or date after 6 months from now")
    public boolean isDateTimeCorrect(){

        return dateTime!=null && dateTime.isAfter(LocalDateTime.now()) && dateTime.isBefore(LocalDateTime.now().plus(6, ChronoUnit.MONTHS));
    }

    @AssertTrue(message = "Incorrect address")
    public boolean isAddressCorrect(){
        if(address==null) {
            return false;
        }
        boolean correctNum = address.getNumber().length()>=1 && address.getNumber().length()<=10;
        boolean correctStreet = address.getStreet().length()>=3 && address.getStreet().length()<=30;
        boolean correctZipCode = address.getZipCode().length()==5 && address.getZipCode().chars().allMatch(Character::isDigit);
        boolean correctLocality = address.getLocality().length()>=3 && address.getLocality().chars().allMatch(Character::isLetter);

        return correctNum && correctStreet && correctZipCode && correctLocality;

    }

    @AssertTrue(message = "Incorrect age: under 16, over 150 or min>max")
    public boolean isAgeRangeCorrect(){

        if(players==null) {
            return false;
        }

        for(PlayerWanted player: players) {

            if(player.getAgeRange()==null) {
                return false;
            }

            int minAge = player.getAgeRange().getAgeMin();
            int maxAge = player.getAgeRange().getAgeMax();

            if (minAge<=ageMinAllowed && maxAge>=ageMaxAllowed || minAge>=maxAge) {
                return false;
            }
        }
        return true;
    }

    @AssertTrue(message = "Check your slots number")
    public boolean isPlayerNumEqualPlayerListSize(){

        if(players==null) {
            return false;
        }
        else {
            return playersNum == players.size();
        }

    }

    @AssertTrue(message = "Fill all player's requirements")
    public boolean isListWithRequirements() {

        if(players==null) {
            return false;
        }

        for (PlayerWanted playerWanted: players) {
            if(playerWanted.getPosition()==null || playerWanted.getAgeRange()==null || playerWanted.getGender()==null) {
                return false;
            }
        }
        return true;
    }


}
