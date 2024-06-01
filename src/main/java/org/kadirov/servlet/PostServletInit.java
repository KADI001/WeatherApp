package org.kadirov.servlet;

import jakarta.servlet.ServletConfig;

public interface PostServletInit {
    default void onInit(ServletConfig config) {
    }
}
