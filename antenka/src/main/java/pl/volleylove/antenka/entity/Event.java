package pl.volleylove.antenka.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.volleylove.antenka.enums.CloseReason;
import pl.volleylove.antenka.event.Price;

import java.time.LocalDateTime;

//upper class for every training and every match
@SuperBuilder
@Getter
@Setter
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

    //true if at least 1 slot left or organiser didn't close this event before filling all slots
    @Column(name = "is_open")
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(name = "close_reason")
    private CloseReason closeReason;

    public Event() {

    }

    //todo - check this
//    @JsonGetter
//    public Long getUserID(){
//        return organizer.getUserID();
//    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", organizer=" + organizer.getUserID() +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", price=" + price +
                ", address=" + address +
                ", open=" + isOpen +
                ", closeReason=" + closeReason +
                '}';
    }
}
