package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.models.User;
import prince.aktiveskochbuch.domain.usecases.CreateUserUseCase;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<HttpResponse> createUser(@RequestBody User user) throws EMailSend {
        User newUser = createUserUseCase.saveUser(user);

        return getHttpResponseCreateEntity(newUser);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) {
        Boolean isSuccess = createUserUseCase.verifyToken(token);

        return getHttpResponseConfirmEntity(isSuccess);
    }

    private static ResponseEntity<HttpResponse> getHttpResponseConfirmEntity(Boolean isSuccess) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Success", isSuccess))
                        .message("Account Verified")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    private static ResponseEntity<HttpResponse> getHttpResponseCreateEntity(User newUser) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("user", newUser))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
}
