package pl.volleylove.antenka.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Admin {

    @Column(name = "admin_from")
    private LocalDate adminFrom;

    //constructor for Hibernate
    protected Admin(){

    }

}
