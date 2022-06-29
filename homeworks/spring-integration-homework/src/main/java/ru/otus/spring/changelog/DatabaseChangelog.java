package ru.otus.spring.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.domain.Caterpillar;
import ru.otus.spring.repository.CaterpillarRepository;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "galeevki", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertCaterpillars", author = "galeevki")
    public void initCaterpillars(CaterpillarRepository repository) {
        for (int i = 0; i < 10; i++) {
            repository.save(Caterpillar.newRandomCaterpillar());
        }
    }
}
