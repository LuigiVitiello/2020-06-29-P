/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	this.txtResult.clear();
    	if (!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, crea prima il grafo");
    		return;
    	}
    	List<Adiacenza> conn= new ArrayList<>();
    	conn=model.ConessioneMax();
    	if (conn==null) {
    		this.txtResult.setText("errore");
    		return;
    	}
    	for (Adiacenza a : conn) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String minString= this.txtMinuti.getText();
    	int min;
    	try {
    		min= Integer.parseInt(minString);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Errore, inseire un numero di minuti");
    		return;
    	}
    	Integer mese= this.cmbMese.getValue();
    	if (mese==null) {
    		this.txtResult.setText("Errore, selezionare un mese");
    		return;
    	}
    	model.creaGrafo(min, mese);
    	this.txtResult.setText("GRAFO CREATO\n");
    	this.txtResult.appendText("# vertici: "+model.getNumVertici());
    	this.txtResult.appendText("\n# archi: "+model.getNumArchi());
    	
    	this.cmbM1.getItems().addAll(this.model.getVertici());
    	this.cmbM2.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    	Match m1 = this.cmbM1.getValue();
    	Match m2 = this.cmbM2.getValue();
    	if(m1== null)
    	{
    		this.txtResult.setText("Scegliere un match di partenza");
    		return ;
    	}
    	if(m2== null)
    	{
    		this.txtResult.setText("Scegliere un match di destinazione");
    		return ;
    	}
    	if(m1.equals(m2))
    	{
    		this.txtResult.setText("Scegliere due match diversi");
    		return ;
    	}
    	
    	if(!this.model.isGrafoCreato()) {
    		this.txtResult.setText("Prima creare il grafo");
    		return ;
    	}
    	
    	this.txtResult.appendText("\nPercorso:\n");
    	for(Match m: this.model.doCollegamento(m1, m2)) {
    		this.txtResult.appendText(m.toString()+"\n");
    	}
    	this.txtResult.appendText("Peso: "+this.model.getPeso());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for (int i=1; i<=12; i++) {
    		this.cmbMese.getItems().add(i);
    	}
    }
    
    
}
