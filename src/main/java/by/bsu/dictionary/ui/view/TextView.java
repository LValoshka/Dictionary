package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.service.text.TextManagementService;
import by.bsu.dictionary.ui.MainLayout;
import by.bsu.dictionary.ui.dialog.ConfirmDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

    private final TextArea textArea = new TextArea("Here you can see the text");
    private final transient TextManagementService textManagementService;
    private final MemoryBuffer memoryBuffer = new MemoryBuffer();
    private final Upload upload = new Upload(memoryBuffer);
    private final Button saveButton = new Button("Save");
    private final Notification notification = new Notification("Such tag doesn't exist! Please, use tags from info.", 3000);
    private String textInField;
    private String oldValue;

    public TextView(TextManagementService textManagementService) {
        this.textManagementService = textManagementService;
        add(getToolBar(), textArea, saveButton);
        uploadFile();
        setTextToTextArea(TextManagementService.globalText);
        textEdit();
    }

    private void textEdit() {
        textArea.addValueChangeListener(e -> {
            textInField = e.getValue();
            oldValue = e.getOldValue();
        });
        saveButton.addClickListener(e -> saveDialog().open());
    }

    private HorizontalLayout getToolBar() {
        Button tokenize = new Button("Tokenize");
        tokenize.addClickListener(e -> {
            textManagementService.tokenizeSelectedText(textInField.replaceAll("[^a-zA-Z0-9]", " "));
            textInField = TextManagementService.globalText;
            setTextToTextArea(textInField);
        });
        return new HorizontalLayout(upload, tokenize);
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
        textArea.setValue(text);
        textArea.setSizeFull();
    }

    private Dialog saveDialog() {
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel");
        Dialog dialog = ConfirmDialog.createDialog(saveButton, cancelButton, ConfirmDialog.EDIT_TEXT);
        saveButton.addClickListener(e -> {
            TextManagementService.globalText = textInField;
            textManagementService.deleteAll();
            if (!textManagementService.parseText()) {
                System.out.println("in ex");
                notification.open();
            }
//            textManagementService.parseEditedWord(textInField, oldValue);
            dialog.close();
        });
        return dialog;
    }
}
