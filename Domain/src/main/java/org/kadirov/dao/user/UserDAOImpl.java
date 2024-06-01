package org.kadirov.dao.user;

import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.kadirov.dao.BaseEntityDAO;
import org.kadirov.entity.user.User;
import org.kadirov.dao.user.exception.LoginAlreadyExistException;
import org.kadirov.dao.user.exception.LoginTooLongException;

import java.util.Optional;
import java.util.function.Function;

public class UserDAOImpl extends BaseEntityDAO<User, Integer> implements UserDAO {

    public UserDAOImpl(@NonNull SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    @Override
    public @NonNull User save(@NonNull User entity) throws LoginAlreadyExistException {
        try {
            return super.save(entity);
        } catch (ConstraintViolationException cve) {
            throw new LoginAlreadyExistException(String.format("Login '%s' already exists", entity.getLogin()), cve);
        } catch (DataException de){
            throw new LoginTooLongException(String.format("Login '%s' too long", entity.getLogin()), de);
        }
    }

    @Override
    public Optional<User> findByLogin(@NonNull final String login) {
        return executeWithTransaction((Function<Session, Optional<User>>) session -> Optional.ofNullable(session
                .createQuery("from User u where u.login = :login", User.class)
                .setParameter("login", login)
                .uniqueResult()));
    }
}
