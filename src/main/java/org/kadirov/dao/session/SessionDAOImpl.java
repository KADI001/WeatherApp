package org.kadirov.dao.session;

import lombok.NonNull;
import org.hibernate.SessionFactory;
import org.kadirov.dao.BaseEntityDAO;
import org.kadirov.entity.session.Session;

import java.util.function.Consumer;

public class SessionDAOImpl extends BaseEntityDAO<Session, String> implements SessionDAO {

    public SessionDAOImpl(@NonNull SessionFactory sessionFactory) {
        super(Session.class, sessionFactory);
    }

    @Override
    public void deleteByExpiresAtLessThanNow() {
        executeWithTransaction((Consumer<org.hibernate.Session>) session ->
                session.createMutationQuery("delete from Session s where s.expiresAt < current_timestamp()").executeUpdate());
    }
}
