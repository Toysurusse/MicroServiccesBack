package expo.controllers;

import expo.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import services.EventService;
import services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class EventRestAPIs {

    private static final Logger logger = LoggerFactory.getLogger(EventRestAPIs.class);

    @Autowired
    EventService eventService;

    @Autowired
    UserService userservice;

    @Autowired
    JwtProvider jwtProvider;

    @GetMapping("/back/user/api/test/user")
    public String userAccess() {
        return ">>> User Contents!";
    }

    @GetMapping("/back/user/api/test/pm")
    public String projectManagementAccess() {
        return ">>> Project Management Board";
    }

    @GetMapping("/back/user/api/test/admin")
    public String adminAccess() {
        return ">>> Admin Contents";
    }


}