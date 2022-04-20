package ru.gaidamaka.service;

import ru.gaidamaka.model.xml.Osm;

public interface OsmPersistService {

    void persist(Osm osm);

    void persist(Osm osm, long limit);

}
