package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.model.TagFrequencyStat;
import by.bsu.dictionary.core.service.statistics.StatisticsService;
import by.bsu.dictionary.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "stat", layout = MainLayout.class)
@PageTitle("Statistics")
public class StatisticsView extends VerticalLayout {
    private final Grid<TagFrequencyStat> statGrid = new Grid<>(TagFrequencyStat.class);

    private final transient StatisticsService statisticsService;

    public StatisticsView(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
        setSizeFull();
        configureGrid();
        add(statGrid);
    }

    private void configureGrid() {
        statisticsService.tagFrequencyStatCount();
        statGrid.setColumns("tag", "frequency");
        statGrid.setItems(statisticsService.findAllTagFrequencyStat());
    }


}
