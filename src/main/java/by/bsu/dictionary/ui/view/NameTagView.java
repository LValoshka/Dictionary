package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.model.WordTagFrequency;
import by.bsu.dictionary.core.service.statistics.StatisticsService;
import by.bsu.dictionary.ui.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "nameTag", layout = MainLayout.class)
@PageTitle("Statistics")
@CssImport("./style/style.css")
public class NameTagView extends VerticalLayout {
    private final Grid<WordTagFrequency> statGrid = new Grid<>(WordTagFrequency.class);

    private final transient StatisticsService statisticsService;

    public NameTagView(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
        statGrid.addClassName("content");
        setSizeFull();
        configureGrid();
        add(statGrid);
    }

    private void configureGrid() {
        statisticsService.wordTagFrequencyStatCount();
        statGrid.setColumns("nameTag", "frequency");
        statGrid.setItems(statisticsService.findAllWordTagFrequency());
    }

}
