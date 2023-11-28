package pl.volleylove.antenka.map.resultcomponents;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultRecord {

    private Geometry geometry;

}
