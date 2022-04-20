package ru.gaidamaka.userchange.reader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.service.osm.tag.Tag;
import ru.gaidamaka.userchange.UserChange;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XMLUserChangeReader implements UserChangeReader {
    private static final Logger log = LoggerFactory.getLogger(XMLUserChangeReader.class);

    private static final QName USER_ATTRIBUTE_NAME = new QName("user");
    private static final QName CHANGE_ATTRIBUTE_NAME = new QName("changeset");
    private static final QName TAG_KEY_ATTRIBUTE_NAME = new QName("k");
    private static final String TAG_START_ELEMENT_NAME = "tag";


    private final InputStream inputStream;
    private final XMLEventReader xmlEventReader;

    public XMLUserChangeReader(InputStream inputStream) {
        this.inputStream = inputStream;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            this.xmlEventReader = factory.createXMLEventReader(inputStream);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Ошибка создания xml event reader", e);
        }
    }

    @Override
    public UserChange next() {
        List<Tag> tags = new ArrayList<>();
        while (xmlEventReader.hasNext()) {
            XMLEvent event = nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                Optional<String> tagKey = readTag(startElement);
                if (tagKey.isPresent()) {
                    tags.add(new Tag(tagKey.get()));
                } else {
                    Optional<String> user = extractAttributeValue(startElement, USER_ATTRIBUTE_NAME);
                    Optional<String> change = extractAttributeValue(startElement, CHANGE_ATTRIBUTE_NAME);
                    if (user.isPresent() && change.isPresent()) {
                        UserChange userChange = new UserChange(user.get(), change.get());
                        userChange.addAllTags(tags);
                        tags.clear();
                        return userChange;
                    } else {
                        tags.clear();
                    }
                }

            }
        }
        return null;
    }

    private Optional<String> readTag(StartElement startElement) {
        if (TAG_START_ELEMENT_NAME.equals(startElement.getName().getLocalPart())) {
            return extractAttributeValue(startElement, TAG_KEY_ATTRIBUTE_NAME);
        }
        return Optional.empty();
    }


    @Override
    public void close() {
        try {
            inputStream.close();
            xmlEventReader.close();
        } catch (IOException | XMLStreamException e) {
            log.error("Ошибка при закрытии", e);
        }
    }

    private XMLEvent nextEvent() {
        try {
            return xmlEventReader.nextEvent();
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Ошибка при получении следующего события", e);
        }
    }

    private Optional<String> extractAttributeValue(StartElement startElement, QName attributeName) {
        Attribute attribute = startElement.getAttributeByName(attributeName);
        if (attribute == null) {
            return Optional.empty();
        }
        return Optional.of(attribute.getValue());
    }
}
