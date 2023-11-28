package pl.volleylove.antenka;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import pl.volleylove.antenka.entity.User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@SpringBootApplication
@EnableJpaRepositories("pl.volleylove.antenka.repository")
public class AntenkaApplication {

	public static void main(String[] args) {


		SpringApplication.run(AntenkaApplication.class, args);



	}

}
