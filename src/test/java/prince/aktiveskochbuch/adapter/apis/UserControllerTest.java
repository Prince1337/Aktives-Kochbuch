package prince.aktiveskochbuch.adapter.apis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.models.User;
import prince.aktiveskochbuch.domain.usecases.CreateUserUseCase;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
class UserControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_ReturnsCreatedResponse() throws EMailSend {
        // Arrange
        User user = new User(1L, "name", "email", "password", true);
        Mockito.when(createUserUseCase.saveUser(Mockito.any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<HttpResponse> response = userController.createUser(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200");
        assertEquals("User created", Objects.requireNonNull(response.getBody()).getMessage(), "Message should be 'User created'");
        assertEquals(user, response.getBody().getData().get("user"), "User should be the same as expected");
    }

    @Test
    void confirmUserAccount_ReturnsVerifiedResponse() {
        // Arrange
        String token = "someToken";
        Mockito.when(createUserUseCase.verifyToken(token)).thenReturn(true);

        // Act
        ResponseEntity<HttpResponse> response = userController.confirmUserAccount(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200");
        assertEquals("Account Verified", Objects.requireNonNull(response.getBody()).getMessage(), "Message should be 'Account Verified'");
        assertTrue((boolean) response.getBody().getData().get("Success"), "Success should be true");
    }

}