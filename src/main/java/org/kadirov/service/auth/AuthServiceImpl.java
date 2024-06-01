package org.kadirov.service.auth;

import lombok.NonNull;
import org.kadirov.service.auth.exception.LoginNotExistException;
import org.kadirov.service.auth.exception.WrongPasswordException;
import org.kadirov.dao.session.SessionDAO;
import org.kadirov.entity.session.Session;
import org.kadirov.dao.user.UserDAO;
import org.kadirov.entity.user.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    private final int sessionTTL;
    private final ChronoUnit sessionTTLUnit;

    public AuthServiceImpl(@NonNull SessionDAO sessionDAO,
                           @NonNull UserDAO userDAO,
                           int sessionTTL,
                           @NonNull ChronoUnit sessionTTLUnit) {
        this.sessionTTL = sessionTTL;
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
        this.sessionTTLUnit = sessionTTLUnit;
    }

    @Override
    @NonNull
    public Session logIn(@NonNull final String login, @NonNull final String password) {
        Optional<User> optionalUser = userDAO.findByLogin(login);

        if (optionalUser.isEmpty())
            throw new LoginNotExistException(String.format("Login '%s' doesn't exist", login));

        User user = optionalUser.get();

        if (!BCrypt.checkpw(password, user.getPassword()))
            throw new WrongPasswordException(String.format("Password for user with login '%s' doesn't equal", login));

        return sessionDAO.save(new Session(user, getSessionTTL()));
    }

    @Override
    @NonNull
    public Session signUp(@NonNull final String login, @NonNull final String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(login, hashedPassword);

        userDAO.save(newUser);

        return sessionDAO.save(new Session(newUser, getSessionTTL()));
    }

    private Timestamp getSessionTTL() {
        return Timestamp.from(Instant.now().plus(sessionTTL, sessionTTLUnit));
    }
}
