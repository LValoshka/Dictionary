package by.bsu.dictionary.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        Button textButton = new Button(VaadinIcon.TEXT_LABEL.create());
        textButton.addClickListener(e -> UI.getCurrent().navigate("text"));
        Button tableButton = new Button(VaadinIcon.TABLE.create());
        tableButton.addClickListener(e -> UI.getCurrent().navigate("words"));
        Button info = new Button(VaadinIcon.INFO.create());
        info.addClickListener(e -> {
            Dialog dialog = createDialog();
            dialog.setResizable(true);
            dialog.setDraggable(true);
            dialog.open();
        });
        Button statButton = new Button("Tag");
        statButton.addClickListener(e -> UI.getCurrent().navigate("stat"));
        Button nameTagButton = new Button("Name-tag");
        nameTagButton.addClickListener(e -> UI.getCurrent().navigate("nameTag"));
        Button tagsButton = new Button("Tags");
        tagsButton.addClickListener(e -> UI.getCurrent().navigate("tags"));

        HorizontalLayout header = new HorizontalLayout(textButton, tableButton, info,
                statButton, nameTagButton, tagsButton);
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
        dialog.add(new Html("<div><b>CC</b> - Coordinating conjunction</div>"));
        dialog.add(new Html("<div><b>CD</b> - Cardinal number</div>"));
        dialog.add(new Html("<div><b>EX</b> - Existential there</div>"));
        dialog.add(new Html("<div><b>FW</b> - Foreign word</div>"));
        dialog.add(new Html("<div><b>JJR</b> - Adjective, comparative</div>"));
        dialog.add(new Html("<div><b>JJS</b> - Adjective, superlative</div>"));
        dialog.add(new Html("<div><b>LS</b> - List item marker</div>"));
        dialog.add(new Html("<div><b>MD</b> - Modal</div>"));
        dialog.add(new Html("<div><b>NNS</b> - Noun, plural</div>"));
        dialog.add(new Html("<div><b>NNPS</b> - Proper noun, plural</div>"));
        dialog.add(new Html("<div><b>PDT</b> - Predeterminer</div>"));
        dialog.add(new Html("<div><b>POS</b> - Possessive ending</div>"));
        dialog.add(new Html("<div><b>PRP</b> - Personal pronoun</div>"));
        dialog.add(new Html("<div><b>PRP$</b> - Possessive pronoun</div>"));
        dialog.add(new Html("<div><b>RB</b> - Adverb</div>"));
        dialog.add(new Html("<div><b>RBR</b> - Adverb, comparative</div>"));
        dialog.add(new Html("<div><b>RBS</b> - Adverb, superlative</div>"));
        dialog.add(new Html("<div><b>RP</b> - Particle</div>"));
        dialog.add(new Html("<div><b>SYM</b> - Symbol</div>"));
        dialog.add(new Html("<div><b>UH</b> - Interjection</div>"));
        dialog.add(new Html("<div><b>VBG</b> - Verb, gerund or present participle</div>"));
        dialog.add(new Html("<div><b>VBN</b> - Verb, past participle</div>"));
        dialog.add(new Html("<div><b>VBP</b> - Verb, non\u00AD3rd person singular present</div>"));
        dialog.add(new Html("<div><b>WDT</b> - Whdeterminer</div>"));
        dialog.add(new Html("<div><b>WP</b> - Whpronoun</div>"));
        dialog.add(new Html("<div><b>WP$</b> - Possessive whpronoun</div>"));
        dialog.add(new Html("<div><b>WRB</b> - Whadverb</div>"));

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(e -> dialog.close());
        dialog.add(new HorizontalLayout(cancel));
        return dialog;
    }
}
