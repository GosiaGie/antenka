package pl.volleylove.antenka.event.match.signup;

import lombok.*;
import pl.volleylove.antenka.entity.Slot;
import pl.volleylove.antenka.enums.SignUpInfo;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForMatchResponse {

    private SignUpInfo info;
    private Slot slot;

}
