package pl.volleylove.antenka.map;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@RequiredArgsConstructor
public class GoogleMapsApiHelper {

    @Autowired
    private final GoogleMapsApiProperties googleMapsApiProperties;

    public String createURI(String street, String number, String zipCode, String locality){

        return googleMapsApiProperties.getApiPath()+"json?address="+ street + "+" + number
                + "+" + zipCode + "+" +locality + "&key=" + googleMapsApiProperties.getApiKey();
    }


}
