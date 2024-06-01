package org.kadirov.service.session;

import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kadirov.TestContainersTest;
import org.kadirov.Utils;
import org.kadirov.dao.session.SessionDAO;
import org.kadirov.dao.session.SessionDAOImpl;
import org.kadirov.entity.session.Session;
import org.kadirov.dao.user.UserDAO;
import org.kadirov.dao.user.UserDAOImpl;
import org.kadirov.entity.user.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiredSessionsCollectorImplIntegrationTest extends TestContainersTest {

    private static final TimeUnit unit = TimeUnit.HOURS;
    private static final int clearInterval = 2;
    private static final SessionDAO sessionDAO = new SessionDAOImpl(sessionFactory);
    private static final UserDAO userDAO = new UserDAOImpl(sessionFactory);

    private static final ExpiredSessionsCollector underTest = new ExpiredSessionsCollectorImpl(sessionDAO, clearInterval, unit);

    @Test
    @DisplayName("Clean up expired sessions in db table Sessions")
    void GivenExpiredAndFreshSessions_WhenRun_ThenOnlyFreshLeft() {
        //GIVEN
        List<User> users = List.of(
                new User(Utils.genUniqueLogin(15), "password1"),
                new User(Utils.genUniqueLogin(15), "password2"),
                new User(Utils.genUniqueLogin(15), "password3")
        );

        users.forEach(userDAO::save);

        List<Session> sessions = List.of(
                new Session(users.get(0), Timestamp.from(DateUtil.yesterday().toInstant())),
                new Session(users.get(1), Timestamp.from(DateUtil.yesterday().toInstant())),
                new Session(users.get(2), Timestamp.from(DateUtil.tomorrow().toInstant()))
        );

        sessions.forEach(sessionDAO::save);

        //WHEN
        underTest.run();

        //THEN
        List<Session> allSessions = sessionDAO.findAll();
        Timestamp now = Timestamp.from(Instant.now());
        assertThat(allSessions).allMatch(session -> session.getExpiresAt().after(now));
    }
}
