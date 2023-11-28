package pl.volleylove.antenka.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.volleylove.antenka.entity.Match;
import pl.volleylove.antenka.entity.PlayerProfile;
import pl.volleylove.antenka.entity.Slot;
import pl.volleylove.antenka.enums.Gender;
import pl.volleylove.antenka.enums.Level;
import pl.volleylove.antenka.enums.Position;
import pl.volleylove.antenka.event.PlayerWanted;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface SlotRepositoryCustom<T,S> {

    @Query("SELECT s FROM slot s " +
            "WHERE s.playerWanted.gender = :gender " +
            "AND s.playerWanted.level = :level " +
            "AND s.playerWanted.ageRange.ageMin <= :age AND s.playerWanted.ageRange.ageMax >= :age " +
            "AND s.playerWanted.position IN :positions " +
            "AND s.match.isOpen = true " +
            "AND s.match.price.benefitPrice <= :maxPrice " +
            "AND s.playerApplied IS NULL")
    Set<Slot> findByPlayerWantedBenefit(Gender gender, Level level, int age, Set<Position> positions, BigDecimal maxPrice);



    @Query("SELECT s FROM slot s " +
            "WHERE s.playerWanted.gender = :gender " +
            "AND s.playerWanted.level = :level " +
            "AND s.playerWanted.ageRange.ageMin <= :age AND s.playerWanted.ageRange.ageMax >= :age " +
            "AND s.playerWanted.position IN :positions " +
            "AND s.match.isOpen = true " +
            "AND s.match.price.regularPrice <= :maxPrice " +
            "AND s.playerApplied IS NULL")
    Set<Slot> findByPlayerWantedNoBenefit(Gender gender, Level level, int age, Set<Position> positions, BigDecimal maxPrice);


    Set <Slot> findByPlayerApplied(PlayerProfile playerProfile);

    Slot findSlotByMatchAndOrderNum(Match match, int orderNum);

    @Query("SELECT s FROM slot s " +
            "WHERE s.match.eventID = :eventID " +
            "AND s.playerApplied = NULL")
    Set <Slot> findFreeMatchSlots(Long eventID);


}
