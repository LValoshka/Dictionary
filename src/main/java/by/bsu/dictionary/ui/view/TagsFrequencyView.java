package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.model.TagsFrequency;
import by.bsu.dictionary.core.service.statistics.StatisticsService;
import by.bsu.dictionary.ui.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "tags", layout = MainLayout.class)
@PageTitle("Statistics")
@CssImport("./style/style.css")
public class TagsFrequencyView extends VerticalLayout {
    private final Grid<TagsFrequency> statGrid = new Grid<>(TagsFrequency.class);

    private final transient StatisticsService statisticsService;

    public TagsFrequencyView(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
        statGrid.addClassName("content");
        setSizeFull();
        configureGrid();
        add(statGrid);
    }

    private void configureGrid() {
        statisticsService.tagsFrequencyStatCount();
        statGrid.setColumns("tags", "frequency");
        statGrid.setItems(statisticsService.findAllTagsFrequency());
    }

}
