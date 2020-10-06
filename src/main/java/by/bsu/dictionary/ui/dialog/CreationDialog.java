package by.bsu.dictionary.ui.dialog;


import by.bsu.dictionary.ui.form.WordCreationForm;
import com.vaadin.flow.component.dialog.Dialog;

public class CreationDialog {
    public static Dialog createDialog(WordCreationForm wordCreationForm) {
        Dialog dialog = new Dialog();
        dialog.add(wordCreationForm);
        return dialog;
    }
}
