package pl.volleylove.antenka.map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("google.maps")
public class GoogleMapsApiProperties {

    private String apiKey;
    private String apiPath;

}
