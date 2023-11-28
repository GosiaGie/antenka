package pl.volleylove.antenka.playerprofile;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.volleylove.antenka.enums.Gender;
import pl.volleylove.antenka.enums.Level;
import pl.volleylove.antenka.enums.Position;

import java.util.EnumSet;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProfileRequest {

    @NotNull
    private EnumSet<Position> positions;
    @NotNull
    private Level level;
    @NotNull
    private Gender gender;
    private String benefitCardNumber;


}
