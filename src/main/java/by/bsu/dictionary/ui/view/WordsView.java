package by.bsu.dictionary.ui.view;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.service.text.TextManagementService;
import by.bsu.dictionary.core.service.word.WordFinder;
import by.bsu.dictionary.core.service.word.WordManagementService;
import by.bsu.dictionary.ui.MainLayout;
import by.bsu.dictionary.ui.dialog.ConfirmDialog;
import by.bsu.dictionary.ui.dialog.CreationDialog;
import by.bsu.dictionary.ui.form.WordCreationForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.stream.Collectors;


@Route(value = "words", layout = MainLayout.class)
@PageTitle("Words")
public class WordsView extends VerticalLayout {

    private final Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());
    private final Grid<Word> wordGrid = new Grid<>(Word.class);
    private final TextField nameField = new TextField();
    private final TextField lemmaField = new TextField();
    private final TextField filterText = new TextField();
    private Grid.Column<Word> editorColumn;

    private final Editor<Word> editor = wordGrid.getEditor();
    private transient String editingWordName;
    private transient String editingWordLemma;

    private WordCreationForm wordForm;
    private Dialog creationDialog;

    private final transient WordFinder wordFinder;
    private final transient WordManagementService wordManagementService;
    private final transient TextManagementService textManagementService;

    public WordsView(WordFinder wordFinder,
                     WordManagementService wordManagementService,
                     TextManagementService textManagementService) {
        this.wordFinder = wordFinder;
        this.wordManagementService = wordManagementService;
        this.textManagementService = textManagementService;

        setSizeFull();

        add(getToolBar(), wordGrid);

        configureGrid();
        updateList();
        editNameField();
    }

    private WordCreationForm createForm() {
        wordForm = new WordCreationForm();
        wordForm.addListener(WordCreationForm.SaveEvent.class, this::saveWord);
        wordForm.addListener(WordCreationForm.CancelEvent.class, e -> closeCustomEditor());
        return wordForm;
    }

    private void closeCustomEditor() {
        wordForm.setVisible(false);
        wordForm.setWord(null);
        creationDialog.close();
    }

    private void saveWord(WordCreationForm.SaveEvent saveEvent) {
        Word word = new Word();
        Map<String, List<String>> map = new HashMap<>();
        word.setName(saveEvent.getWord().getName());
        word.setFrequency(0L);
        word.setTags(saveEvent.getWord().getTags());
        word.setLemma(saveEvent.getWord().getLemma());

        wordManagementService.save(word);
        updateList();
        closeCustomEditor();
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.addThemeName("bordered");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        Button update = new Button(VaadinIcon.REFRESH.create());
        update.addClickListener(e -> {
            textManagementService.deleteAll();
            textManagementService.parseText();
            updateList();
        });

        Button addWordButton = new Button("Add word", e -> {
            creationDialog = CreationDialog.createDialog(createForm());
            creationDialog.open();
            addWord();
        });

        HorizontalLayout toolBar = new HorizontalLayout(filterText, addWordButton, update);
        toolBar.expand(addWordButton);
        return toolBar;
    }

    private void addWord() {
        wordGrid.asSingleSelect().clear();
        editWord(new Word());
    }

    private void editWord(Word word) {
        if (word == null) {
            closeCustomEditor();
        } else {
            wordForm.setWord(word);
            wordForm.setVisible(true);
        }
    }

    private void configureGrid() {
        wordGrid.setSizeFull();
        wordGrid.setColumns("name", "frequency", "lemma");

        wordGrid.addColumn(word -> {
            List<String> tags = word.getTags().stream().map(Enum::name).collect(Collectors.toList());
            return tags.isEmpty() ? " " : String.join(",", tags);
        }).setHeader("Tag").setAutoWidth(true).setSortable(true);

        editorColumn = wordGrid.addComponentColumn(word -> {
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addClickListener(e -> {
                editingWordName = word.getName();
                editingWordLemma = word.getLemma();
                editor.editItem(word);
                nameField.focus();
            });
            editButton.setEnabled(!editor.isOpen());
            editButtons.add(editButton);
            return editButton;
        });

        wordGrid.addComponentColumn(word -> {
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteDialog(word).open());
            return deleteButton;
        });
    }

    private void updateList() {
        wordGrid.setItems(wordFinder.findAllByFilter(filterText.getValue()));
    }


    private void editNameField() {
        Binder<Word> binder = new Binder<>(Word.class);
        editor.setBinder(binder).setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        binder.forField(nameField)
                .withValidator(new StringLengthValidator("Name can not be empty.", 1, 50))
                .withStatusLabel(validationStatus).bind("name");
        wordGrid.getColumnByKey("name").setEditorComponent(nameField);
        binder.forField(lemmaField)
                .withValidator(new StringLengthValidator("Lemma can not be empty.", 1, 50))
                .withStatusLabel(validationStatus).bind("lemma");
        wordGrid.getColumnByKey("lemma").setEditorComponent(lemmaField);


        editor.addOpenListener(e -> editButtons.forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save");
        save.addClickListener(e -> editor.save());
        Button cancel = new Button("Cancel");
        cancel.addClickListener(e -> editor.cancel());

        wordGrid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);

        editor.addSaveListener(e -> {
            textManagementService.replaceWordFromTextWithNewOne(editingWordName, e.getItem().getName()); //
            wordManagementService.saveWordWithNewLemma(e.getItem().getName(), e.getItem().getLemma());
            editor.cancel();
        });
        add(validationStatus, wordGrid);
    }

    private Dialog deleteDialog(Word word) {
        Button deleteButton = new Button("delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("cancel");
        Dialog dialog = ConfirmDialog.createDialog(deleteButton, cancelButton, ConfirmDialog.DELETE_TEXT);
        deleteButton.addClickListener(e -> {
            wordManagementService.delete(word);
            dialog.close();
            updateList();
            textManagementService.deleteWordFromText(word.getName());
        });
        return dialog;
    }

}
