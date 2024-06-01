package org.kadirov.dao.session;

import org.kadirov.dao.EntityDAO;
import org.kadirov.entity.session.Session;

public interface SessionDAO extends EntityDAO<Session, String> {
    void deleteByExpiresAtLessThanNow();
}
