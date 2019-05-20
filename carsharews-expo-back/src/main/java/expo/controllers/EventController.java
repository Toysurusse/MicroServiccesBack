package expo.controllers;

import expo.security.jwt.JwtProvider;
import message.request.EventForm;
import message.response.ResponseMessage;
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
import services.CityService;
import services.EventService;
import services.UserService;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    EventService eventService;

    @Autowired
    CityService cityService;

    @Autowired
    UserService userservice;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/back/user/api/user/event")
    public ResponseEntity<?> addEvent(@Valid @RequestBody EventForm eventRequest) throws Exception {
        jwtProvider.validateJwtToken(eventRequest.getToken());
        Optional<User> user = userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()));
        User userNotOpt = user.get();

        if (!eventService.getEventByName(eventRequest.getNameEvent())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Cet évènement existe déjà"),
                    HttpStatus.BAD_REQUEST);
        }

        if (cityService.checkIfCodePostalExist(eventRequest.getCodePostal())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Ce code postal n'existe pas"),
                    HttpStatus.BAD_REQUEST);
        }
        eventService.add(eventRequest,userNotOpt);
        return new ResponseEntity<>(eventService.getEventByUser(
                userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()))),
                HttpStatus.OK);
    }

    @PostMapping("/back/user/api/user/updateevent")
    public ResponseEntity<?> updateEvent(@Valid @RequestBody EventForm eventRequest) throws Exception {
        jwtProvider.validateJwtToken(eventRequest.getToken());
        Optional<User> user = userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()));
        User userNotOpt = user.get();
        eventService.update(eventRequest, userNotOpt);
        return new ResponseEntity<>(eventService.getEventByUser(
                userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()))),
                HttpStatus.OK);
    }

    @PostMapping("/back/user/api/user/deleteevent")
    public ResponseEntity<?> deleteEvent(@Valid @RequestBody EventForm eventRequest) throws Exception {
        jwtProvider.validateJwtToken(eventRequest.getToken());
        Optional<User> user = userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()));
        User userNotOpt = user.get();
        eventService.delete(eventRequest, userNotOpt);
        return new ResponseEntity<>(eventService.getEventByUser(
                userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()))),
                HttpStatus.OK);
    }

    @PostMapping("/back/user/api/user/deleteactiveevent")
    public ResponseEntity<?> deleteactiveEvent(@Valid @RequestBody EventForm eventRequest) throws Exception {
        jwtProvider.validateJwtToken(eventRequest.getToken());
        Optional<User> user = userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()));
        User userNotOpt = user.get();
        eventService.deleteactive(eventRequest, userNotOpt);
        return new ResponseEntity<>(eventService.getEventByUser(
                userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(eventRequest.getToken()))),
                HttpStatus.OK);
    }

    @PostMapping("/back/user/api/user/getevent")
    public ResponseEntity<?> getEventByUser(@Valid @RequestBody String token) {
        jwtProvider.validateJwtToken(token);
        return new ResponseEntity<>(eventService.getEventByUser(
                userservice.userByUserName(jwtProvider.getUserNameFromJwtToken(token))),
                HttpStatus.OK);
    }
}
