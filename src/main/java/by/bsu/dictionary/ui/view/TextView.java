package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.service.text.TextManagementService;
import by.bsu.dictionary.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "text", layout = MainLayout.class)
@PageTitle("Text")
public class TextView extends VerticalLayout {

    private final TextArea txt = new TextArea("Here you can see the text");
    private final transient TextManagementService textManagementService;

    public TextView(TextManagementService textManagementService) {
        this.textManagementService = textManagementService;
        add(txt);

        setTextToTextArea();
    }


    private void setTextToTextArea() {
        String text = String.valueOf(textManagementService.uploadText());

        txt.setValue(text);
        txt.setReadOnly(true);
        txt.setSizeFull();
    }
}
