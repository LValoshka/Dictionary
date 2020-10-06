package by.bsu.dictionary.ui.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ConfirmDialog {
    public static final String EDIT_TEXT = "Are you sure you want to edit the word?";
    public static final String DELETE_TEXT = "Are you sure you want to delete the word?";

    public static Dialog createDialog(Button actionButton, Button cancel, String text) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);
        dialog.setWidth("420px");
        dialog.setHeight("130px");
        dialog.setDraggable(true);
        dialog.add(text);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(e->dialog.close());
        dialog.add(new HorizontalLayout(actionButton, cancel));
        return dialog;
    }
}
