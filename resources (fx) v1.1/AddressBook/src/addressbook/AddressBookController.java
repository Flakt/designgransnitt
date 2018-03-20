
package addressbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.chalmers.cse.dat215.lab1.*;

public class AddressBookController implements Initializable {
    
    @FXML private MenuBar menuBar;
    @FXML private Button newButton;
    @FXML private Button deleteButton;
    @FXML private SplitPane splitPane;
    @FXML private ListView listView;  //    Ska enlig labb vara contactsList

    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField mailTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField postCodeTextField;

    private Presenter presenter;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        presenter = new Presenter(listView,firstNameTextField,lastNameTextField,phoneTextField,mailTextField,addressTextField,cityTextField,postCodeTextField);
        presenter.init();
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

            }
        });
        firstNameTextField.focusedProperty().addListener(new TextFieldListener(firstNameTextField));
        lastNameTextField.focusedProperty().addListener(new TextFieldListener(lastNameTextField));
        phoneTextField.focusedProperty().addListener(new TextFieldListener(phoneTextField));
        mailTextField.focusedProperty().addListener(new TextFieldListener(mailTextField));
        addressTextField.focusedProperty().addListener(new TextFieldListener(addressTextField));
        cityTextField.focusedProperty().addListener(new TextFieldListener(cityTextField));
        postCodeTextField.focusedProperty().addListener(new TextFieldListener(postCodeTextField));
    }
    
    @FXML 
    protected void openAboutActionPerformed(ActionEvent event) throws IOException{
    
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("addressbook/resources/AddressBook");
        Parent root = FXMLLoader.load(getClass().getResource("address_book_about.fxml"), bundle);
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root));
        aboutStage.setTitle(bundle.getString("about.title.text"));
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }
    
    @FXML 
    protected void closeApplicationActionPerformed(ActionEvent event) throws IOException{
        
        Stage addressBookStage = (Stage) menuBar.getScene().getWindow();
        addressBookStage.hide();
    }

    @FXML
    protected void newButtonActionPerformed (ActionEvent event) {
        presenter.newContact();
    }

    @FXML
    protected void deleteButtonActionPerformed (ActionEvent event) {
        presenter.removeCurrentContact();
    }

    @FXML
    protected void textFieldActionPerformed(ActionEvent event) {
        presenter.textFieldActionPerformed(event);
    }

    private class TextFieldListener implements ChangeListener<Boolean> {
        private TextField textField;

        public TextFieldListener(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue) {
                presenter.textFieldFocusGained(textField);
            }
            else {
                presenter.textFieldFocusLost(textField);
            }
        }
    }
}
