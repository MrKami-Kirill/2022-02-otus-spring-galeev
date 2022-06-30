package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.service.ButterflyService;
import ru.otus.spring.service.CaterpillarService;
import ru.otus.spring.service.TransformationService;

import static java.lang.System.out;

@RequiredArgsConstructor
@ShellComponent
public class ShellCommands {

    private final CaterpillarService caterpillarService;
    private final ButterflyService butterflyService;
    private final TransformationService transformationService;

    @ShellMethod(value = "Get caterpillar by id", key = "cbi")
    private void getCaterpillarById(@ShellOption String id) throws Exception {
        out.println(caterpillarService.findById(id));
    }

    @ShellMethod(value = "Get all caterpillars", key = "c")
    private void getAllCaterpillars() {
        out.println(caterpillarService.findAll());
    }

    @ShellMethod(value = "Get all butterflies", key = "cc")
    private void getCaterpillarsCount() {
        out.println("Всего гусениц: " + caterpillarService.findAll().size());
    }

    @ShellMethod(value = "Get butterfly by id", key = "bbi")
    private void getButterflyById(@ShellOption String id) throws Exception {
        out.println(butterflyService.findById(id));
    }

    @ShellMethod(value = "Get all butterflies", key = "b")
    private void getAllButterflies() {
        out.println(butterflyService.findAll());
    }

    @ShellMethod(value = "Get all butterflies", key = "bc")
    private void getButterfliesCount() {
        out.println("Всего бабочек: " + butterflyService.findAll().size());
    }

    @ShellMethod(value = "Add random new valid caterpillar", key = "arnvc")
    private void addRandomNewValidCaterpillar() {
        out.println(caterpillarService.addValidNew());
    }

    @ShellMethod(value = "Add random new caterpillar", key = "arnc")
    private void addRandomNewCaterpillar() {
        out.println(caterpillarService.addNew());
    }

    @ShellMethod(value = "Add new caterpillar by name", key = "ancbn")
    private void addNewCaterpillarByName(@ShellOption String name) {
        out.println(caterpillarService.addNewByName(name));
    }

    @ShellMethod(value = "Transformation all caterpillars to butterflies", key = "t")
    private void transformationAllCaterpillarsToButterflies() throws Exception {
        transformationService.startTransformation();
    }
}
