package com.high.highblog.api;

import com.high.highblog.bloc.MailBloc;
import com.high.highblog.model.entity.ConfirmationCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final MailBloc mailBloc;

    public TestController(final MailBloc mailBloc) {
        this.mailBloc = mailBloc;
    }

    @GetMapping("/auth")
    public ResponseEntity<?> testAuthorization() {
        return ResponseEntity.ok("SUCCEEDED");
    }

    @PostMapping("/cors")
    public ResponseEntity<?> testCors() {
        return ResponseEntity.ok("SUCCEEDED");
    }

    @PostMapping("/send-mail")
    public ResponseEntity<?> testMail(@RequestParam("email") String email) {
        mailBloc.sendConfirmRegistrationMailTo(email,
                                               "http://localhost:3000",
                                               ConfirmationCode.builder()
                                                               .id(1L)
                                                               .code("Test code")
                                                               .build());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/debug")
    public ResponseEntity<?> testDebug(HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }
}
