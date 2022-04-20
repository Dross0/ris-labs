package ru.gaidamaka.userchange;

import java.util.*;
import java.util.stream.Collectors;

public class UsersChangeList {

    private final Map<String, List<UserChange>> changes;

    public UsersChangeList() {
        changes = new HashMap<>();
    }


    public List<UserChange> getChangesByUser(String user) {
        return Collections.unmodifiableList(changes.getOrDefault(user, Collections.emptyList()));
    }

    public void add(UserChange userChange) {
        changes.putIfAbsent(userChange.getUser(), new ArrayList<>());
        changes.get(userChange.getUser()).add(userChange);
    }

    public Map<String, Integer> getChangesNumber(boolean ascending) {
        Comparator<Map.Entry<String, List<UserChange>>> comparator = Comparator.comparingInt(o -> o.getValue().size());
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return changes.entrySet().stream()
                .sorted(comparator)
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                userChanges -> userChanges.getValue().size(),
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        )
                );
    }

    public Map<String, Integer> getChangesNumber() {
        return getChangesNumber(true);
    }
}
