package org.kadirov.dao.user;

import lombok.NonNull;
import org.kadirov.dao.EntityDAO;
import org.kadirov.entity.user.User;

import java.util.Optional;

public interface UserDAO extends EntityDAO<User, Integer> {
    Optional<User> findByLogin(@NonNull final String login);
}
