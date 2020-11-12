package by.bsu.dictionary.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
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
        Button info = new Button(VaadinIcon.INFO.create());
        info.addClickListener(e -> {
            Dialog dialog = createDialog();
            dialog.open();
        });

        HorizontalLayout header = new HorizontalLayout(textButton, tableButton, info);
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        addToNavbar(header);
    }

    private Dialog createDialog() {
        Dialog dialog = new Dialog();
        Button cancel = new Button("Cancel");

        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.setWidth("350px");
        dialog.setHeight("350px");

        dialog.add(new Html("<div><b>NN</b> - Noun, singular or mass</div>"));
        dialog.add(new Html("<div><b>DT</b> - Determiner</div>"));
        dialog.add(new Html("<div><b>VB</b> - Verb, base form</div>"));
        dialog.add(new Html("<div><b>VBD</b> - Verb, past tense</div>"));
        dialog.add(new Html("<div><b>VBZ</b> - Verb, third person singular present</div>"));
        dialog.add(new Html("<div><b>IN</b> - Preposition or subordinating conjunction</div>"));
        dialog.add(new Html("<div><b>NNP</b> - Proper noun, singular</div>"));
        dialog.add(new Html("<div><b>TO</b> - to</div>"));
        dialog.add(new Html("<div><b>JJ</b> - Adjective</div>"));
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(e -> dialog.close());
        dialog.add(new HorizontalLayout(cancel));
        return dialog;
    }
}
