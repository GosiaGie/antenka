package pl.volleylove.antenka.event;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Getter
@Setter
@Embeddable
public class AgeRange {

    @Column(name = "age_min")
    int ageMin;
    @Column(name = "age_max")
    int ageMax;

    public AgeRange() {
    }

    @Override
    public String toString() {
        return "AgeRange{" +
                "ageMin=" + ageMin +
                ", ageMax=" + ageMax +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeRange ageRange = (AgeRange) o;
        return ageMin == ageRange.ageMin && ageMax == ageRange.ageMax;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ageMin, ageMax);
    }
}
