package pl.volleylove.antenka.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;
import java.util.Set;

//single event - match with random people, who applied on RandomMatch
@Entity(name = "match")
@Table(name = "matches")
@OnDelete(action = OnDeleteAction.CASCADE)
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class Match extends Event {

    //number of wanted players
    @Column(name = "players_num")
    private int playersNum;

    //list for players' slots - table "match_slots
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "match", cascade = CascadeType.REMOVE)
    @JsonProperty("slots")
    private Set<Slot> slots;

    @Column(name = "free_slots") //number of free slots left
    private int freeSlots;

    @Override
    public String toString() {
        return "Match{" +
                super.toString() +
                "playersNum=" + playersNum +
                ", slots=" + slots +
                ", freeSlots=" + freeSlots +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(getEventID(), match.getEventID()) && playersNum == match.playersNum
                && Objects.equals(freeSlots, match.getFreeSlots())
                && Objects.equals(slots.size(), match.slots.size());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playersNum, freeSlots, slots.size());
    }
}
