package ru.gaidamaka.service.osm;

import ru.gaidamaka.service.osm.tag.Tag;
import ru.gaidamaka.service.osm.tag.TagCounterImpl;
import ru.gaidamaka.userchange.UserChange;
import ru.gaidamaka.userchange.UsersChangeList;
import ru.gaidamaka.userchange.reader.UserChangeReader;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class OSMServiceImpl implements OSMService {

    private final UserChangeReader userChangeReader;
    private final TagCounterImpl tagCounterImpl;
    private final AtomicReference<UsersChangeList> usersChangeList;

    public OSMServiceImpl(UserChangeReader userChangeReader) {
        this.userChangeReader = userChangeReader;
        this.tagCounterImpl = new TagCounterImpl();
        this.usersChangeList = new AtomicReference<>();
    }

    @Override
    public UsersChangeList getUsersChangeList() {
        return usersChangeList.updateAndGet(changeList -> changeList != null ? changeList : readUserChangeList());
    }

    @Override
    public Map<Tag, Integer> getTags() {
        usersChangeList.updateAndGet(changeList -> changeList != null ? changeList : readUserChangeList());
        return tagCounterImpl.getTags();
    }


    private UsersChangeList readUserChangeList() {
        UsersChangeList changeList = new UsersChangeList();
        UserChange userChange = userChangeReader.next();
        while (userChange != null) {
            changeList.add(userChange);
            userChange.getTags().forEach(tagCounterImpl::increment);
            userChange = userChangeReader.next();
        }
        return changeList;
    }


}
