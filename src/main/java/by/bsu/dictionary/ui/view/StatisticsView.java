package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.service.statistics.StatisticsService;
import by.bsu.dictionary.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "stat", layout = MainLayout.class)
@PageTitle("Statistics")
public class StatisticsView extends VerticalLayout {
    private final Grid<Word> statGrid = new Grid<>(Word.class);

    private final transient StatisticsService statisticsService;

    public StatisticsView(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
        setSizeFull();
        configureGrid();
        fillInTable();
        add(statGrid);
    }

    private void configureGrid() {
        statGrid.setSizeFull();
        statGrid.removeAllColumns();
        statGrid.addColumn(str -> statisticsService.tagFrequencyStat().keySet())
                .setHeader("Tag").setAutoWidth(true).setSortable(true);

        statGrid.addColumn(str -> statisticsService.tagFrequencyStat().values())
                .setHeader("Frequency").setAutoWidth(true).setSortable(true);
    }

    private void fillInTable() {
        statGrid.addColumn(e -> "hi").setHeader("test");
    }

}
