package pl.volleylove.antenka.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;
import pl.volleylove.antenka.event.PlayerWanted;

import java.util.Objects;

@Entity(name = "slot")
@Table(name = "match_slots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slotID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eventID")
    @JsonIncludeProperties(value = {"eventID", "address", "price", "name", "dateTime"}) //annotation to prevent recursion
    private Match match;

    //number unique for every slot in ONE match
    @Column(name = "order_num")
    private int orderNum;

    @Embedded
    private PlayerWanted playerWanted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_appliedID")
    @JsonIncludeProperties(value = {"id"})
    private PlayerProfile playerApplied;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;

        Long eventID = match==null ? 0 : match.getEventID();
        Long playerID = playerApplied==null? 0: playerApplied.getPlayerProfileID();

        Long eventID2 = slot.match==null ? 0 : slot.match.getEventID();
        Long playerID2 = slot.playerApplied==null? 0: slot.playerApplied.getPlayerProfileID();


        return orderNum == orderNum && Objects.equals(id, slot.id)
                && Objects.equals(eventID, eventID2)
                && Objects.equals(playerWanted, slot.playerWanted)
                && Objects.equals(playerID, playerID2);
    }

    @Override
    public int hashCode() {

        Long eventID = match==null ? 0 : match.getEventID();
        Long playerID = playerApplied==null? 0: playerApplied.getPlayerProfileID();

        return Objects.hash(id, eventID, orderNum, playerWanted, playerID);
    }

    @Override
    public String toString() {

        Long eventID = match==null ? 0 : match.getEventID();
        Long playerID = playerApplied==null? 0: playerApplied.getPlayerProfileID();

        return "RandomMatchSlot{" +
                "id=" + id +
                ", match=" + eventID +
                ", matchSlotOrderNum=" + orderNum +
                ", playerWanted=" + playerWanted +
                ", playerApplied=" + playerID +
                '}';
    }
}
