package org.kadirov.service.session;

public interface ExpiredSessionsCollector extends Runnable {
    boolean start();
    boolean finish();
}
