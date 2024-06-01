package org.kadirov.service.session;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kadirov.dao.session.SessionDAO;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class ExpiredSessionsCollectorImpl implements ExpiredSessionsCollector {

    private final SessionDAO sessionDAOImpl;
    private final int cleanInterval;
    private final TimeUnit unit;

    private final ScheduledExecutorService scheduledExecutorService;

    private ScheduledFuture<?> task;

    public ExpiredSessionsCollectorImpl(@NonNull SessionDAO sessionDAOImpl, int cleanInterval, @NonNull TimeUnit unit) {
        this.sessionDAOImpl = sessionDAOImpl;
        this.cleanInterval = cleanInterval;
        this.unit = unit;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public boolean start() {
        if (task != null && !task.isDone())
            return false;

        task = scheduledExecutorService.scheduleAtFixedRate(this, cleanInterval, cleanInterval, unit);
        return false;
    }

    @Override
    public boolean finish() {
        if (task == null || task.isDone())
            return false;

        task.cancel(false);
        return true;
    }

    @Override
    public void run() {
        try {
            sessionDAOImpl.deleteByExpiresAtLessThanNow();
        } catch (Exception e) {
            log.error("Failed to collect expired sessions", e);
        }
    }
}
