package org.kadirov.servlet;

import jakarta.servlet.http.*;

public interface PreService {
    default void onPreService(HttpServletRequest req, HttpServletResponse resp){}
}
