package org.kadirov.dao.location;

import org.kadirov.dao.EntityDAO;
import org.kadirov.entity.location.Location;

import java.util.List;

public interface LocationDAO extends EntityDAO<Location, Integer> {
    List<Location> findByUserIdAndOrderById(int userId);

    boolean deleteByLongAndLatAndUserId(double longitude, double latitude, int userId);
}
