package by.bsu.dictionary.ui;

import by.bsu.dictionary.ui.view.*;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./style/style.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createDrawer() {
        RouterLink textLink = new RouterLink("Text", TextView.class);
        textLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink wordsLink = new RouterLink("Words", WordsView.class);
        RouterLink tagFrequencyStatLink = new RouterLink("Tag-Frequency statistics", StatisticsView.class);
        RouterLink nameTagFrequencyStatLink = new RouterLink("Word_Tag-Frequency statistics", NameTagView.class);
        RouterLink tagTagFrequencyStatLink = new RouterLink("Tag_Tag-Frequency statistics", TagsFrequencyView.class);

        addToDrawer(new VerticalLayout(textLink, wordsLink, tagFrequencyStatLink, nameTagFrequencyStatLink, tagTagFrequencyStatLink));
    }

    private void createHeader() {
        H1 logo = new H1("Dictionary");
        logo.addClassName("logo");

        Button info = new Button(VaadinIcon.INFO.create());
        info.addClickListener(e -> {
            Dialog dialog = createDialog();
            dialog.setResizable(true);
            dialog.setDraggable(true);
            dialog.open();
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), info, logo);
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
