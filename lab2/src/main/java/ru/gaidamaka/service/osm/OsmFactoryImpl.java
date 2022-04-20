package ru.gaidamaka.service.osm;

import lombok.RequiredArgsConstructor;
import ru.gaidamaka.model.xml.Osm;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

@RequiredArgsConstructor
public class OsmFactoryImpl implements OsmFactory {

    private final InputStream is;

    @Override
    public Osm createOsm() {
        try {
            JAXBContext context = JAXBContext.newInstance(Osm.class);
            return (Osm) context.createUnmarshaller()
                    .unmarshal(is);
        } catch (JAXBException e) {
            throw new IllegalStateException("Ошибка демаршалинга osm", e);
        }
    }
}
