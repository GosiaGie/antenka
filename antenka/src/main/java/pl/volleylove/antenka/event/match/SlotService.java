package pl.volleylove.antenka.event.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.volleylove.antenka.entity.Match;
import pl.volleylove.antenka.entity.PlayerProfile;
import pl.volleylove.antenka.entity.Slot;
import pl.volleylove.antenka.enums.Gender;
import pl.volleylove.antenka.enums.Level;
import pl.volleylove.antenka.enums.Position;
import pl.volleylove.antenka.event.PlayerWanted;
import pl.volleylove.antenka.repository.SlotRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    public Set<Slot> saveSlotsInMatch(Long randomMatchID, List<PlayerWanted> players) {

        //1. Every PlayerWanted will get be in slot, with infos about match and orderNum
        //2. changing type from List to Set, for preventing duplicates
        //3. setting every slot its order number for this match - to better order
        //and to prevent replacing slots with the same requirements (Set can't have duplicates)
        //4. setting every slot its eventID

        int orderNum = 1;
        Match match = Match.builder()
                .eventID(randomMatchID)
                .build();


        Set<Slot> slots = new HashSet<>();
        Slot slot;

        for(PlayerWanted player:players) {
            slot = Slot.builder()
                    .match(match)
                    .playerWanted(player)
                    .orderNum(orderNum)
                    .build();
            slots.add(slot);

            orderNum++;
        }

        slotRepository.saveAll(slots);

        return slots;

    }

    public Set<Slot> findSlots(Gender gender, Level level, int age, Set<Position> positions,
                                          BigDecimal maxPrice, boolean isActiveBenefit ) {

        if(isActiveBenefit){
            return slotRepository.findByPlayerWantedBenefit(gender, level, age, positions, maxPrice);
        }
        else {
            return slotRepository.findByPlayerWantedNoBenefit(gender, level, age, positions, maxPrice);
        }


    }


    public Slot savePlayerApp(Slot slot, PlayerProfile player){

        slot.setPlayerApplied(player);
        return slotRepository.save(slot);

    }

    public Set <Slot> findPlayersSlots(PlayerProfile playerProfile) {

        return slotRepository.findByPlayerApplied(playerProfile);

    }

    public Slot findSlotByMatchAndOrderNum(Match match, int orderNum) {
        return slotRepository.findSlotByMatchAndOrderNum(match, orderNum);
    }

    public Set <Slot> findFreeMatchSlots(Long eventID) {
        return slotRepository.findFreeMatchSlots(eventID);
    }




}
