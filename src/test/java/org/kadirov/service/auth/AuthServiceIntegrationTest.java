package org.kadirov.service.auth;

import org.junit.jupiter.api.Test;
import org.kadirov.TestContainersTest;
import org.kadirov.Utils;
import org.kadirov.config.Environment;
import org.kadirov.service.auth.exception.LoginNotExistException;
import org.kadirov.service.auth.exception.WrongPasswordException;
import org.kadirov.dao.session.SessionDAO;
import org.kadirov.dao.session.SessionDAOImpl;
import org.kadirov.entity.session.Session;
import org.kadirov.dao.user.UserDAO;
import org.kadirov.dao.user.UserDAOImpl;
import org.kadirov.entity.user.User;
import org.kadirov.dao.user.exception.LoginAlreadyExistException;
import org.kadirov.dao.user.exception.LoginTooLongException;
import org.mindrot.jbcrypt.BCrypt;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AuthServiceIntegrationTest extends TestContainersTest {

    private static final SessionDAO sessionDAO = new SessionDAOImpl(sessionFactory);
    private static final UserDAO userDAO = new UserDAOImpl(sessionFactory);

    private static final AuthService authService = new AuthServiceImpl(
            sessionDAO,
            userDAO,
            Integer.parseInt(Environment.getProperty("session.ttl.duration")),
            ChronoUnit.valueOf(Environment.getProperty("session.ttl.unit"))
    );

    @Test
    void GivenExistentLogin_WhenLogIn_ThenSuccess() {
        //GIVEN
        String login = Utils.genUniqueLogin(15);
        String password = "password";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        userDAO.save(new User(login, hashedPassword));

        //WHEN
        //THEN
        Session session = assertDoesNotThrow(() -> authService.logIn(login, password));
        assertThat(sessionDAO.findById(session.getId())).isPresent();
    }

    @Test
    void GivenNonExistentLogin_WhenLogIn_ThenThrowLoginNotExistException() {
        //GIVEN
        String login = Utils.genUniqueLogin(15);
        String password = "password";

        //WHEN
        //THEN
        assertThatThrownBy(() -> authService.logIn(login, password))
                .isInstanceOf(LoginNotExistException.class)
                .hasMessage(String.format("Login '%s' doesn't exist", login));
    }

    @Test
    void GivenWrongPassword_WhenLogIn_ThenThrowLoginNotExistException() {
        //GIVEN
        String login = Utils.genUniqueLogin(15);
        String password = "password";
        String wrongPassword = "123345";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        userDAO.save(new User(login, hashedPassword));

        //WHEN
        //THEN
        assertThatThrownBy(() -> authService.logIn(login, wrongPassword))
                .isInstanceOf(WrongPasswordException.class)
                .hasMessage(String.format("Password for user with login '%s' doesn't equal", login));
    }

    @Test
    void GivenExistentLogin_WhenSignUp_ThenThrowLoginAlreadyExistException() {
        //GIVEN
        String login = Utils.genUniqueLogin(15);
        String password = "password";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        userDAO.save(new User(login, hashedPassword));

        //WHEN
        //THEN
        assertThatThrownBy(() -> authService.signUp(login, password))
                .isInstanceOf(LoginAlreadyExistException.class)
                .hasMessage(String.format("Login '%s' already exists", login));
    }

    @Test
    void GivenTooLongLogin_WhenSignUp_ThenThrowLoginTooLongException() {
        //GIVEN
        String login = Utils.genUniqueLogin();
        String password = "password";

        //WHEN
        //THEN
        assertThatThrownBy(() -> authService.signUp(login, password))
                .isInstanceOf(LoginTooLongException.class)
                .hasMessage(String.format("Login '%s' too long", login));
    }

    @Test
    void GivenNonExistentLogin_WhenSignUp_ThenSuccess() {
        //GIVEN
        String login = Utils.genUniqueLogin(15);
        String password = "password";

        //WHEN
        //THEN
        Session session = assertDoesNotThrow(() -> authService.signUp(login, password));
        assertAll(
                () -> assertThat(userDAO.findByLogin(login)).isPresent(),
                () -> assertThat(sessionDAO.findById(session.getId())).isPresent()
        );
    }
}
