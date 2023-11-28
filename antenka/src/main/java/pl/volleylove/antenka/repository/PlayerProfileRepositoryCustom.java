package pl.volleylove.antenka.repository;

import org.springframework.stereotype.Repository;
import pl.volleylove.antenka.entity.PlayerProfile;
import pl.volleylove.antenka.entity.User;

@Repository
public interface PlayerProfileRepositoryCustom <T,S> {

    PlayerProfile findByUser(User user);

}
