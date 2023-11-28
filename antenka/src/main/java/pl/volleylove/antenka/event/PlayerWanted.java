package pl.volleylove.antenka.event;

import jakarta.persistence.*;
import lombok.Getter;
import pl.volleylove.antenka.enums.Gender;
import pl.volleylove.antenka.enums.Level;
import pl.volleylove.antenka.enums.Position;

import java.util.Objects;

@Embeddable
public class PlayerWanted {

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @AttributeOverrides({
            @AttributeOverride(name="ageMin",
                    column=@Column(name="age_min")),
            @AttributeOverride(name="ageMax",
                    column=@Column(name="age_max"))
    })
    @Embedded
    private AgeRange ageRange;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    public PlayerWanted() {
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PlayerWanted{" +
                "positions=" + position +
                ", gender=" + gender +
                ", ageRange=" + ageRange +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerWanted that = (PlayerWanted) o;
        return Objects.equals(position, that.position) && gender == that.gender && Objects.equals(ageRange, that.ageRange) && level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, gender, ageRange, level);
    }

}
