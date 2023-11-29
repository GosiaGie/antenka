package pl.volleylove.antenka.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.volleylove.antenka.enums.CloseReason;
import pl.volleylove.antenka.event.Price;

import java.time.LocalDateTime;
import java.util.Objects;

//upper class for every match and training
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "events")
@Entity(name = "event")
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventID")
    private Long eventID;

    @ManyToOne
    @JoinColumn(name = "organizerID")
    @JsonBackReference
    private User organizer;

    @Column(name = "name")
    private String name;

    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTime;

    @Embedded
    private Price price;

    @JoinColumn(name = "addressID")
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    //true if at least 1 free slot left, organiser didn't close this event before filling all slots
    //and event date is not in the past
    @Column(name = "is_open")
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(name = "close_reason")
    private CloseReason closeReason;

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", organizerID=" + organizer.getUserID() +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", price=" + price +
                ", address=" + address +
                ", isOpen=" + isOpen +
                ", closeReason=" + closeReason +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return isOpen == event.isOpen && Objects.equals(eventID, event.eventID) && Objects.equals(organizer.getUserID(), event.organizer.getUserID()) && Objects.equals(name, event.name) && Objects.equals(dateTime, event.dateTime) && Objects.equals(price, event.price) && Objects.equals(address, event.address) && closeReason == event.closeReason;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, organizer.getUserID(), name, dateTime, price, address, isOpen, closeReason);
    }
}
