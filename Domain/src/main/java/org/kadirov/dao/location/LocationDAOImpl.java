package org.kadirov.dao.location;

import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.kadirov.dao.BaseEntityDAO;
import org.kadirov.entity.location.Location;

import java.util.List;
import java.util.function.Function;

public class LocationDAOImpl extends BaseEntityDAO<Location, Integer> implements LocationDAO {

    public LocationDAOImpl(@NonNull SessionFactory sessionFactory) {
        super(Location.class, sessionFactory);
    }

    @Override
    public List<Location> findByUserIdAndOrderById(int userId) {
        return executeWithTransaction((Function<Session, List<Location>>) session -> session
                .createQuery("FROM Location WHERE user.id=:userId order by id desc", Location.class)
                .setParameter("userId", userId)
                .getResultList()
        );
    }

    @Override
    public boolean deleteByLongAndLatAndUserId(double longitude, double latitude, int userId) {
        return executeWithTransaction(session -> {
            String query = "DELETE FROM Location l WHERE l.longitude=:longitude AND l.latitude=:latitude AND l.user.id=:userId";
            int executedUpdate = session.createMutationQuery(query).setParameter("longitude", longitude).setParameter("latitude", latitude).setParameter("userId", userId).executeUpdate();

            return executedUpdate != 0;
        });
    }
}
