package by.bsu.dictionary.ui.form;

import by.bsu.dictionary.core.model.Word;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.Collections;

public class WordCreationForm extends FormLayout {

    private final TextField name = new TextField("Name");
    private final CheckboxGroup<Word.Tags> tags = new CheckboxGroup<>();

    private final Binder<Word> binder = new Binder<>(Word.class);
    private final Button save = new Button("Save");
    private final Button cancel = new Button("Cancel");

    private Word word;

    public WordCreationForm() {
        binder.bindInstanceFields(this);

        tags.setLabel("Tags");
        tags.setItems(Word.Tags.values());
        tags.setValue(Collections.singleton(Word.Tags.DT));
        tags.addThemeVariants(CheckboxGroupVariant.MATERIAL_VERTICAL);
        tags.setRequired(true);
        name.setRequired(true);

        add(name, tags, createButtonsLayout());

        binder.forField(name)
                .asRequired("Field can not be empty!")
                .bind(Word::getName, Word::setName);
//        binder.forField(name)
//                .asRequired("Field can not be empty!")
//                .bind(Word::getName, Word::setName);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(e -> validateAndSave());
        cancel.addClickListener(e -> fireEvent(new WordCreationForm.CancelEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, cancel);
    }

    public void setWord(Word word) {
        this.word = word;
        binder.readBean(word);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(word);
            fireEvent(new WordCreationForm.SaveEvent(this, word));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public abstract static class WordFormEvent extends ComponentEvent<WordCreationForm> {
        private final Word word;

        protected WordFormEvent(WordCreationForm source, Word course) {
            super(source, false);
            this.word = course;
        }

        public Word getWord() {
            return word;
        }
    }

    public static class SaveEvent extends WordCreationForm.WordFormEvent {
        SaveEvent(WordCreationForm source, Word word) {
            super(source, word);
        }
    }

    public static class CancelEvent extends WordCreationForm.WordFormEvent {
        CancelEvent(WordCreationForm source) {
            super(source, null);
        }
    }


    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
