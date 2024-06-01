package org.kadirov.service.auth;

import lombok.NonNull;
import org.kadirov.entity.session.Session;

public interface AuthService {
    @NonNull
    Session logIn(@NonNull final String login, @NonNull final String password);
    @NonNull
    Session signUp(@NonNull final String login, @NonNull final String password);
}
