package expo.controllers;


import expo.security.jwt.JwtProvider;
import message.request.PassengerForm;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import services.PassengerService;
import services.TravelService;
import services.UserService;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PassagerController {


    private static final Logger logger = LoggerFactory.getLogger(PassagerController.class);

    @Autowired
    PassengerService passagerService;

    @Autowired
    TravelService travelService;

    @Autowired
    UserService userservice;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/back/user/api/user/passager")
    public ResponseEntity<?> addPassager(@Valid @RequestBody PassengerForm passagerForm) {
        logger.info("in Add Passager : "+ passagerForm.getTravelCode()+ passagerForm.getToken() + passagerForm.getUserInfo() );
        jwtProvider.validateJwtToken(passagerForm.getToken());
        Optional<User> user = userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(passagerForm.getToken()));
        User userNotOpt = user.get();
        logger.info("Test : "+passagerForm);
        logger.info(passagerForm.getTravelCode());
        logger.info(passagerForm.getUserInfo());
        logger.info(passagerForm.getToken());

        passagerService.add(passagerForm, userNotOpt);
        return new ResponseEntity<>(passagerService.getPassagerByTravel(travelService.getTravelByCode(passagerForm.getTravelCode())),
                HttpStatus.OK);
    }

    @PostMapping("/back/user/api/user/getpassagerbytravel")
    public ResponseEntity<?> getpassagerbytravel(@Valid @RequestBody String travelCode) {
        logger.info("in passager by travel");
        logger.info("Taille : "+passagerService.getPassagerByTravel(travelService.getTravelByCode(travelCode)).size());
        return new ResponseEntity<>(passagerService.getPassagerByTravel(travelService.getTravelByCode(travelCode)),
                HttpStatus.OK);
    }

    @PostMapping("/back/user/api/user/deletepassager")
    public ResponseEntity<?> deleteTravel(@Valid @RequestBody PassengerForm passagerRequest) {
        logger.info("in Delete Passager");
        jwtProvider.validateJwtToken(passagerRequest.getToken());
        Optional<User> user = userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(passagerRequest.getToken()));
        User userNotOpt = user.get();
        passagerService.delete(passagerRequest, userNotOpt);
        return new ResponseEntity<>(passagerService.getPassagerByTravel(travelService.getTravelByCode(passagerRequest.getTravelCode())),
                HttpStatus.OK);
    }
}
