package expo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.EventService;

@RestController
public class FreeInfoController {

    private static final Logger logger = LoggerFactory.getLogger(FreeInfoController.class);

    @Autowired
    EventService eventService;

    @PostMapping("/back/user/api/user/geteventFree")
    public ResponseEntity<?> getEventFree() {
        logger.info("in");
        return new ResponseEntity<>(eventService.getEventFree(),
                HttpStatus.OK);
    }
}
