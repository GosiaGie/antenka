package pl.volleylove.antenka.event;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class Price {


    @Column(name = "regular_price")
    private BigDecimal regularPrice;

    @Column(name = "benefit_price")
    private BigDecimal benefitPrice;

    //constructor for Hibernate
    protected Price() {
    }

    public Price(String priceRegular, String priceBenefit) {
        this.regularPrice = new BigDecimal(priceBenefit);
        this.benefitPrice = new BigDecimal(priceBenefit);
    }


    @Override
    public String toString() {
        return "Price{" +
                "regularPrice=" + regularPrice +
                ", benefitPrice=" + benefitPrice +
                '}';
    }

}
