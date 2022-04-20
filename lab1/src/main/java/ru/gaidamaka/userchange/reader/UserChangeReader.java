package ru.gaidamaka.userchange.reader;

import ru.gaidamaka.userchange.UserChange;

public interface UserChangeReader extends AutoCloseable {
    UserChange next();
}
