package by.bsu.dictionary.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("Logo=)");
        logo.addClassName("logo");

        Button textButton = new Button(VaadinIcon.TEXT_LABEL.create());
        textButton.addClickListener(e -> UI.getCurrent().navigate("text"));
        Button tableButton = new Button(VaadinIcon.TABLE.create());
        tableButton.addClickListener(e -> UI.getCurrent().navigate("words"));

        HorizontalLayout header = new HorizontalLayout(textButton, tableButton);
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        addToNavbar(header);

    }
}
