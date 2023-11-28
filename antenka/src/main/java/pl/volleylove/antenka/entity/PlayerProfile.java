package pl.volleylove.antenka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.volleylove.antenka.enums.Gender;
import pl.volleylove.antenka.enums.Level;
import pl.volleylove.antenka.enums.Position;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "player_profiles")
public class PlayerProfile {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "playerID")
    @JsonProperty("id")
    private Long playerProfileID;

    @OneToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;

    @ElementCollection(targetClass = Position.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "player_positions", joinColumns = @JoinColumn(name = "playerID"))
    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private EnumSet<Position> positions;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "benefit_card_number")
    private String benefitCardNumber;

    @OneToMany(mappedBy = "playerApplied", fetch = FetchType.EAGER)
    private Set<Slot> randomMatchApps;

    @Transient
    private int age;

    @Transient
    private boolean activeBenefit;

    protected PlayerProfile() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerProfile that = (PlayerProfile) o;
        return age == that.age && activeBenefit == that.activeBenefit && Objects.equals(this.playerProfileID, that.playerProfileID) && Objects.equals(user, that.user) && Objects.equals(positions, that.positions) && level == that.level && gender == that.gender && Objects.equals(benefitCardNumber, that.benefitCardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerProfileID, user, positions, level, gender, benefitCardNumber, age, activeBenefit);
    }

    @Override
    public String toString() {
        return "PlayerProfile{" +
                "playerProfileID=" + playerProfileID +
                ", user=" + user.getUserID() +
                ", positions=" + positions +
                ", level=" + level +
                ", gender=" + gender +
                ", benefitCardNumber='" + benefitCardNumber + '\'' +
                ", age=" + age +
                ", activeBenefit=" + activeBenefit +
                '}';
    }
}
