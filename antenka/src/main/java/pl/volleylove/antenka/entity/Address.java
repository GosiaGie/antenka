package pl.volleylove.antenka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.volleylove.antenka.map.resultcomponents.Location;
import pl.volleylove.antenka.enums.AddressType;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressID", nullable = false)
    private Long addressID;

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "flat_number")
    private String flatNumber;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "locality")
    private String locality;

    @Embedded
    @JsonProperty(namespace = "results")
    private Location location;

    @Column(name = "description")
    private String description;

    public Address(){

    }


    @Override
    public String toString() {
        return "Address{" +
                "addressType='" + addressType + '\'' +
                "street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", flatNumber='" + flatNumber + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", locality='" + locality + '\'' +
                ", location=" + location +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        Address address = (Address) obj;

        return addressType.toString().equals(address.addressType.toString())
                && street.equals(address.street)
                && number.equals(address.number)
                && flatNumber.equals(address.flatNumber)
                && zipCode.equals(address.zipCode)
                && locality.equals(address.locality);


    }
}
