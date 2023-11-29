package pl.volleylove.antenka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.volleylove.antenka.event.match.add.AddMatchRequest;
import pl.volleylove.antenka.event.match.add.AddMatchResponse;
import pl.volleylove.antenka.event.match.find.FindMatchRequest;
import pl.volleylove.antenka.event.match.find.FindMatchResponse;
import pl.volleylove.antenka.event.match.find.FindSlotsResponse;
import pl.volleylove.antenka.event.match.signup.SignUpForMatchRequest;
import pl.volleylove.antenka.event.match.signup.SignUpForMatchResponse;
import pl.volleylove.antenka.benefit.BenefitService;
import pl.volleylove.antenka.event.match.MatchService;
import pl.volleylove.antenka.user.UserService;

import java.io.IOException;

@RestController
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private BenefitService benefitService;

    @Autowired
    private UserService userService;



    @PostMapping("/findMatch")
    public FindMatchResponse findMatch(@RequestBody FindMatchRequest request) {

        return matchService.findMatch(request);

    }


    @PostMapping("/findSlots")
    public FindSlotsResponse findSlotsOfMatch(@RequestBody FindMatchRequest request) {

       return matchService.findSlots(request);

    }




    @PostMapping("/addMatch")
    public AddMatchResponse add(@RequestBody @Validated AddMatchRequest request, Errors errors) throws IOException, InterruptedException {

        return matchService.add(request, errors);

    }

    @PostMapping("/signUpForMatch")
    public SignUpForMatchResponse sign(@RequestBody SignUpForMatchRequest request) {

         return matchService.signUpForMatch(request);

    }







}





