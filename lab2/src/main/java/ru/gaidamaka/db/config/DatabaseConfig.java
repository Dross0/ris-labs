package ru.gaidamaka.db.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DatabaseConfig {

    private final String url;

    private final String user;

    private final String password;

}
