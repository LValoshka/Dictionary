package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.service.text.TextManagementService;
import by.bsu.dictionary.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.concurrent.atomic.AtomicReference;

@Route(value = "text", layout = MainLayout.class)
@PageTitle("Text")
public class TextView extends VerticalLayout {

    private final TextArea txt = new TextArea("Here you can see the text");
    private final transient TextManagementService textManagementService;
    private final MemoryBuffer memoryBuffer = new MemoryBuffer();
    private final Upload upload = new Upload(memoryBuffer);
    private String textInField;

    public TextView(TextManagementService textManagementService) {
        this.textManagementService = textManagementService;
        add(upload, txt);
        uploadFile();
        setTextToTextArea(TextManagementService.globalText);
    }

    private void uploadFile() {
        AtomicReference<String> text = new AtomicReference<>("");
        upload.addSucceededListener(e -> {
            textInField = textManagementService.uploadTextFromHome(memoryBuffer);
            text.set(textInField);
            setTextToTextArea(textInField);
        });
    }

    private void setTextToTextArea(String text) {
        txt.setValue(text);
        txt.setReadOnly(true);
        txt.setSizeFull();
    }
}
