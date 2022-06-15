package ru.otus.spring.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.model.mongo.AuthorMongo;
import ru.otus.spring.model.mongo.BookMongo;
import ru.otus.spring.model.mongo.CommentMongo;
import ru.otus.spring.model.mongo.GenreMongo;
import ru.otus.spring.repository.mongo.BookMongoRepository;

import java.util.Collections;
import java.util.Set;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "galeevki", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertBookMongos", author = "galeevki")
    public void innitBooks(BookMongoRepository repository) {
        repository.save(new BookMongo("Сильмариллион", Set.of(new CommentMongo("Комментарий 1 к книге Сльмариллион"), new CommentMongo("Комментарий 2 к книге Сльмариллион")), Set.of(new AuthorMongo("Джон Р.Р. Толкин")), Set.of(new GenreMongo("Фэнтези"))));
        repository.save(new BookMongo("Властелин Колец", Collections.EMPTY_SET, Set.of(new AuthorMongo("Джон Р.Р. Толкин")), Set.of(new GenreMongo("Фэнтези"))));
        repository.save(new BookMongo("Гарри Поттер и Философский камень", Collections.EMPTY_SET, Set.of(new AuthorMongo("Дж. К. Роулинг")), Set.of(new GenreMongo("Фэнтези"))));
        repository.save(new BookMongo("Sapiens. Краткая история человечества", Collections.EMPTY_SET, Set.of(new AuthorMongo("Ю.Н. Харари")), Set.of(new GenreMongo("Нон-фикшн"))));
        repository.save(new BookMongo("Убийство в «Восточном экспрессе»", Collections.EMPTY_SET, Set.of(new AuthorMongo("Агата Кристи")), Set.of(new GenreMongo("Детектив"))));
        repository.save(new BookMongo("Горе от ума", Collections.EMPTY_SET, Set.of(new AuthorMongo("Грибоедов А.С.")), Set.of(new GenreMongo("Комедия"))));
        repository.save(new BookMongo("Война и мир", Collections.EMPTY_SET, Set.of(new AuthorMongo("Толстой Л.Н.")), Set.of(new GenreMongo("Любовный роман"), new GenreMongo("Исторический роман"), new GenreMongo("Военная проза"))));
        repository.save(new BookMongo("Над пропостью во ржи", Collections.EMPTY_SET, Set.of(new AuthorMongo("Д. Сэлинджер")), Set.of(new GenreMongo("Роман воспитания"))));
        repository.save(new BookMongo("Мифы Древней Греции", Collections.EMPTY_SET, Set.of(new AuthorMongo("Н.Кун")), Set.of(new GenreMongo("Мифы и легенды"))));
        repository.save(new BookMongo("Евгений Онегин", Collections.EMPTY_SET, Set.of(new AuthorMongo("Пушкин А.С.")), Set.of(new GenreMongo("Роман в стихах"))));
        repository.save(new BookMongo("1984", Collections.EMPTY_SET, Set.of(new AuthorMongo("Д. Оруэлл")), Set.of(new GenreMongo("Научная фантастика"))));
        repository.save(new BookMongo("Мы", Collections.EMPTY_SET, Set.of(new AuthorMongo("Замятин Е.Н.")), Set.of(new GenreMongo("Антиутопия"), new GenreMongo("Любовный роман"), new GenreMongo("Научная фантастика"))));
        repository.save(new BookMongo("Триумфальная арка", Collections.EMPTY_SET, Set.of(new AuthorMongo("Эрих Мария Ремарк")), Set.of(new GenreMongo("Военная проза"), new GenreMongo("Исторический роман"))));
        repository.save(new BookMongo("Трое в лодке, не считая собаки", Collections.EMPTY_SET, Set.of(new AuthorMongo("Дж. К. Джером")), Set.of(new GenreMongo("Юмористическая повесть"))));
        repository.save(new BookMongo("Тихий Дон", Collections.EMPTY_SET, Set.of(new AuthorMongo("Шолохов М.А.")), Set.of(new GenreMongo("Исторический роман"))));
        repository.save(new BookMongo("Семь смертей Эвелины Хардкасал", Collections.EMPTY_SET, Set.of(new AuthorMongo("С. Тёртон")), Set.of(new GenreMongo("Триллер"))));
        repository.save(new BookMongo("Снеговик", Collections.EMPTY_SET, Set.of(new AuthorMongo("Ю. Нёсбе")), Set.of(new GenreMongo("Детектив"), new GenreMongo("Триллер"))));
        repository.save(new BookMongo("Почти идеальные люди", Collections.EMPTY_SET, Set.of(new AuthorMongo("М. Бут")), Set.of(new GenreMongo("Страноведение"))));
        repository.save(new BookMongo("Краткая история времени", Collections.EMPTY_SET, Set.of(new AuthorMongo("С. Хокинг")), Set.of(new GenreMongo("Научпоп"))));
        repository.save(new BookMongo("Унесенные ветром", Collections.EMPTY_SET, Set.of(new AuthorMongo("М. Митчелл")), Set.of(new GenreMongo("Исторический роман"))));
    }
}
