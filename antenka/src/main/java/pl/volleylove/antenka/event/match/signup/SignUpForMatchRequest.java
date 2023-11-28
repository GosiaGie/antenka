package pl.volleylove.antenka.event.match.signup;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SignUpForMatchRequest {

    @NotNull
    private Long eventID;
    @NotNull
    private int slotNum;

}
