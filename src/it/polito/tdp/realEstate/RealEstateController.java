package it.polito.tdp.realEstate;
import it.polito.tdp.realEstate.model.Immobile;
import it.polito.tdp.realEstate.model.Model;
import it.polito.tdp.realEstate.model.Zona;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RealEstateController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox boxSceltaAnalisi;

    @FXML
    private ComboBox<String> comboBoxCaratteristica;

    @FXML
    private Button btnAbilitaAnalisi;

    @FXML
    private Text txtAbilita;

    @FXML
    private HBox boxAnalisi;

    @FXML
    private Button btnElencoZonaCaratt;

    @FXML
    private ComboBox<String> comboBoxOrdineElenco;

    @FXML
    private TextArea txtElenco;

    @FXML
    private Button btnTrovaZonaCaratt;

    @FXML
    private Text txtZonaAlto;

    @FXML
    private Text txtZonaBasso;

    @FXML
    private Button btnAbilitaNuovaAnalisi;

    @FXML
    private ComboBox<Zona> comboBoxZonaPortaffoglio;

    @FXML
    private ComboBox<String> comboBoxTipoImmobile;

    @FXML
    private ComboBox<Integer> comboBoxMinMq;

    @FXML
    private ComboBox<Integer> comboBoxMaxMq;

    @FXML
    private ComboBox<Integer> comboBoxMinYear;

    @FXML
    private ComboBox<Integer> comboBoxMaxYear;

    @FXML
    private ComboBox<String> comboBoxQualQuan;

    @FXML
    private Button btnInvia;

    @FXML
    private Text mexApp;

    @FXML
    private Slider pesoRent;

    @FXML
    private Slider pesoCrime;

    @FXML
    private Slider pesoSchool;

    @FXML
    private Slider pesoYear;

    @FXML
    private Slider pesoMq;

    @FXML
    private ComboBox<Immobile> comboBoxVoluto;

    @FXML
    private Text sforo;

    @FXML
    private ComboBox<Integer> comboBoxBudget;

    @FXML
    private Button btnCerca;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnInviaQualita;
    
    @FXML
    private TextArea txtRisultatoRicorsione;

    
    
	private Model model;	
	
	private Integer postalCode = null;
	private String propType = null; 
	private Integer minMq=null;
	private Integer maxMq=null;
	private Integer minYear=null;
	private Integer maxYear=null;
	private float pesoMqVar=0;
	private float pesoRentVar=0;
	private float pesoCrimeRateVar=0;
	private float pesoYearVar=0; 
	private float pesoSchoolRateVar=0;
	private Immobile immobileVolutoVar=null;
	private float maxSforamentoBudget=0;
	private float massimoBudgetMin=0;


	// SEZIONE 1 - ANALISI
    @FXML
    void doAbilitaAnalisi(ActionEvent event) {
    	
    	if(this.comboBoxCaratteristica.getValue()!=null) {
    		// ristampo a nero, nel caso in cui fosse diventato rosso
        	this.txtAbilita.setFill(Color.BLACK);
        	// abilito il box in cui faccio le ricerche
        	this.boxAnalisi.setDisable(false);
        	//disabilito la selezione
        	this.boxSceltaAnalisi.setDisable(true);
    	} else
    		this.txtAbilita.setFill(Color.RED);
    }

    @FXML
    void doAbilitaNuovaAnalisi(ActionEvent event) {
    	// disabilito il box in cui faccio le ricerche
    	this.boxAnalisi.setDisable(true);
    	//abilito la selezione
    	this.boxSceltaAnalisi.setDisable(false);
    	// setto l'rdine dell'elelenco a default
		this.comboBoxOrdineElenco.setValue("Decrescente"); //di default
    	
    	// pulisco il resto
    	this.txtElenco.clear();
    	this.txtZonaAlto.setText("~");
    	this.txtZonaBasso.setText("~");
    	this.txtAbilita.setFill(Color.BLACK);
    }

    @FXML
    void doTrovaElencoCaratt(ActionEvent event) {
    	String caratteristica = this.comboBoxCaratteristica.getValue();
    	String car = caratteristica;
    	switch(caratteristica) {
		case "Tasso di criminalità":
			caratteristica = "AVG(p.Violent_Crime_Rate)";
			break;
		case "Livello scolastico":
			caratteristica = "AVG(p.School_Score)";
			break;
		case "Valore di mercato medio":
			caratteristica = "AVG(p.Zillow_Estimate)";
			break;
		case "Affitto medio stimato":
			caratteristica = "AVG(p.Rent_Estimate )";
			break;
		case "Anno di costruzione":
			caratteristica = "AVG(p.yearBuilt)";
			break;
		case "Metratura media":
			caratteristica = "((AVG(p.finished_SqFt))/10.764)";
			break;			
    	}
    	

    	String ordine = this.comboBoxOrdineElenco.getValue(); // crescente o decrescente

    	System.out.println(caratteristica);
    	System.out.println(ordine);
		
			
    	this.txtElenco.clear();
    	List<Zona> elenco = this.model.getElencoPerCarattristica(caratteristica, ordine);
    	for(Zona z : elenco) {
    		this.txtElenco.appendText(z.toString()+" - "+car+
    				": "+z.getValoreDaMetodoCaratteristica(car)+"\n");
    	}
    	
    }

    @FXML
    void doTrovaZonaCaratt(ActionEvent event) {
    	String caratteristica = this.comboBoxCaratteristica.getValue();
       	
//    	this.txtZonaAlto.setText("San Michele".toUpperCase());
//    	this.txtZonaBasso.setText("Pirri".toUpperCase());
    	
    	String foot=""; // da aggiunger a fine riga
    	switch(caratteristica) {
		case "Tasso di criminalità":
			break;
		case "Livello scolastico":
			break;
		case "Valore di mercato medio":
			foot = "$";
			break;
		case "Affitto medio stimato":
			foot = "$";
			break;
		case "Anno di costruzione":
			break;
		case "Metratura media":
			foot = "mq";
			break;		
    	}
    	
    	this.txtZonaAlto.setText(this.model.getZonaMigliorePerCaratteristica(caratteristica)+" "+foot);
    	this.txtZonaBasso.setText(this.model.getZonaPeggiorePerCaratteristica(caratteristica)+" "+foot);
    }	 
    
    
    // SEZIONE 2 - RICORSIONE
    


    @FXML
    void doGestioneFiltri(ActionEvent event) {
    	
    	// i filtri non sono obbligatori, possono anche essere null
    	// solo qualita/quantita è obbligatorio
    	if(this.comboBoxQualQuan.getValue()!=null) {
    		// ristampo a nero, nel caso in cui fosse diventato rosso
        	this.mexApp.setFill(Color.BLACK);
    		this.mexApp.setText("Messaggi inviati dall'applicazione:  ...");
        	// a seconda che sia qual o quan diabilito una o un'altra parte
        	// abilito la selezione
    		
    		// gestisco iil filtro dal model
        	// il metodo gestione filtri
        	// mi ritorna una lista con due interi
        	// il primo è il massimo budget minimo
        	// il secondo è il massimoSforamento dal budget
        	
        	// FILTRI OPZIONALI -> li uso sia per qual che per quan se settati
        	if(this.comboBoxZonaPortaffoglio.getValue()!=null) 
        		this.postalCode=this.comboBoxZonaPortaffoglio.getValue().getPostalCode();
        	if(this.comboBoxTipoImmobile.getValue()!=null)
        		this.propType = this.comboBoxTipoImmobile.getValue(); 
        	if(this.comboBoxMinMq.getValue()!=null)
        		this.minMq = this.comboBoxMinMq.getValue();
        	if(this.comboBoxMaxMq.getValue()!=null)
        		this.maxMq = this.comboBoxMaxMq.getValue();
        	if(this.comboBoxMinYear.getValue()!=null)
        		this.minYear = this.comboBoxMinYear.getValue();
        	if(this.comboBoxMaxYear.getValue()!=null)
        		this.maxYear = this.comboBoxMaxYear.getValue();
        	
        	if(minMq!=null && maxMq!=null) {
        		if(minMq>maxMq) {
            		this.mexApp.setText("Attenzione ! Hai inserito un valore minimo di "+
            				"SUPERFICIE superiore a quello che hai inserito come massimo."+
            				"Non è fisicamente possibile! Modifica per andare avanti!");
            		this.mexApp.setFill(Color.RED);
            		return;
            		// non fa nulla, quindi non devo neppure resettare
        		}
    		}
    		
    		if(minYear!=null && maxYear!=null) {
    			if(minYear>maxYear) {
            		this.mexApp.setText("Attenzione ! Hai inserito un valore minimo di "+
            				"ANNO superiore a quello che hai inserito come massimo."+
            				"Non è fisicamente possibile! Modifica per andare avanti!");
            		this.mexApp.setFill(Color.RED);
            		return;
            		// non fa nulla, quindi non devo neppure resettare
    			}
    		}
        	
        	
        	
    		
    		// QUALITA'
        	if(this.comboBoxQualQuan.getValue()=="Qualità"){
        		// abilito la sezione qualita
        		this.pesoRent.setDisable(false);
        		this.pesoCrime.setDisable(false);
        		this.pesoMq.setDisable(false);
        		this.pesoSchool.setDisable(false);
        		this.pesoYear.setDisable(false);
        		this.btnInviaQualita.setDisable(false);
        		// disabilito filtri e bottoni
        		
        		// disbilito il bottone invia scelta qual/quan
        		// e anche la scelta
        		this.btnInvia.setDisable(true);
        		this.comboBoxQualQuan.setDisable(true);
        		
        		// DATO CHE LA GESTIONE FILTRI LA FACCIO SOLO CON INVIA
        		// DISABILITO TUTTO IL RESTO, SE VUOLE MODIFICARE RESETTA
        		this.comboBoxZonaPortaffoglio.setDisable(true);
        		this.comboBoxTipoImmobile.setDisable(true);
        		this.comboBoxMinMq.setDisable(true);
        		this.comboBoxMaxMq.setDisable(true);
        		this.comboBoxMinYear.setDisable(true);
        		this.comboBoxMaxYear.setDisable(true);
        		
        		
        		// QUANTITA'
        	}else if(this.comboBoxQualQuan.getValue()=="Quantità"){       		
        				           	
            	List<Immobile> immobiliFiltrati = new ArrayList<Immobile>();
            	immobiliFiltrati = this.model.gestioneFiltri(this.postalCode, this.propType, 
            			this.minMq, maxMq, minYear, maxYear, 0, 0, 0, 0, 0); // pesi a zero perchè quantità
            	// sono stati calcolati nel model con la gestione filtri
            	
            	// VERIFICO DI AVERE IMMOBILI FILTRATI, 
            	// ALTRIMENTI CHIEDO DI AMPLIARE IL RANGE DI RICERCA
            	if(!immobiliFiltrati.isEmpty()) {
            		this.maxSforamentoBudget=model.getMassimoSforamento();
                	this.massimoBudgetMin = model.getMassimoBudgetMinimo();
            		this.comboBoxVoluto.getItems().clear();
            		this.comboBoxBudget.getItems().clear();
                	
                	// riempo i campi
                	this.sforo.setText(String.valueOf(this.maxSforamentoBudget));            	
                	for(int i=5000;i<=this.massimoBudgetMin;i+=5000)
                		this.comboBoxBudget.getItems().add(i);            	
                	for(Immobile filtrato:immobiliFiltrati) {
                		this.comboBoxVoluto.getItems().add(filtrato);
                	}
                	
                	// abilito la sezione budget
            		// mentre la qualita rimane disabilitata
            		// mando alla sezione budget   	
                	
                	// abilito quella budget
            		this.comboBoxVoluto.setDisable(false);
            		this.comboBoxBudget.setDisable(false);
            		this.sforo.setDisable(false);
            		this.btnCerca.setDisable(false);  
                	this.btnReset.setDisable(false);  
            		this.txtRisultatoRicorsione.setDisable(false); 
            		
            		// disbilito il bottone invia scelta qual/quan
            		// e anche la scelta
            		this.btnInvia.setDisable(true);
            		this.comboBoxQualQuan.setDisable(true);
            		
            		// DATO CHE LA GESTIONE FILTRI LA FACCIO SOLO CON INVIA
            		// DISABILITO TUTTO IL RESTO, SE VUOLE MODIFICARE RESETTA
            		this.comboBoxZonaPortaffoglio.setDisable(true);
            		this.comboBoxTipoImmobile.setDisable(true);
            		this.comboBoxMinMq.setDisable(true);
            		this.comboBoxMaxMq.setDisable(true);
            		this.comboBoxMinYear.setDisable(true);
            		this.comboBoxMaxYear.setDisable(true);

    	    		this.txtRisultatoRicorsione.clear();
            		this.txtRisultatoRicorsione.appendText("Potrebbe volerci un po' (massimo un minuto)\n");
                	
//            		System.out.println("Voluti: "+this.comboBoxVoluto.getItems().toString());	
            		
            	} else { // il filtro è troppo ristretto, zero immobili filtrati
            		this.doReset(null);
            		this.mexApp.setText("Attenzione ! Il campo di ricerca impostato con i FILTRI "+
            				"è troppo ristretto. Ti consigliamo di inserire meno filtri, o "+
            				"di ampliare il range di quelli desiderati!");
            		this.mexApp.setFill(Color.RED);
            	}
        	}
        	
    	} else {
    		this.mexApp.setText("Attenzione! Non hai selezionato l'orientamento sulla "+
    				"QUALITA' o sulla QUANTITA'. E' obbligatorio selezionarlo per andare avanti!");
    		this.mexApp.setFill(Color.RED);
    		return;
    	}
    	
    
	
    }
    
    @FXML
    void doInviaQual(ActionEvent event) {
    	
    	if(this.comboBoxQualQuan.getValue()=="Qualità") {
    		

        	// ho già preso con il primo invio i filtri
        	// ora prendo i pesi
			this.pesoCrimeRateVar = (float) this.pesoCrime.getValue();
			this.pesoRentVar = (float)  this.pesoRent.getValue();
			this.pesoSchoolRateVar = (float) this.pesoSchool.getValue();
			this.pesoMqVar = (float) this.pesoMq.getValue();    			
			this.pesoYearVar = (float) this.pesoYear.getValue();
			
			System.out.println("Crime"+this.pesoCrimeRateVar+"\n");
			System.out.println("Rente"+this.pesoRentVar+"\n");
			System.out.println("School"+this.pesoSchoolRateVar+"\n");
			System.out.println("Mq"+this.pesoMqVar+"\n");
			System.out.println("Year"+this.pesoYearVar+"\n");
    	     	
        	List<Immobile> immobiliFiltrati = new ArrayList<Immobile>();
        	immobiliFiltrati = this.model.gestioneFiltri(this.postalCode, this.propType, 
        			this.minMq, maxMq, minYear, maxYear, 
        			this.pesoMqVar, this.pesoRentVar, this.pesoCrimeRateVar,
        			this.pesoYearVar, this.pesoSchoolRateVar); // pesi a zero perchè quantità
        	// sono stati calcolati nel model con la gestione filtri
        	
        	// VERIFICO DI AVERE IMMOBILI FILTRATI, 
        	// ALTRIMENTI CHIEDO DI AMPLIARE IL RANGE DI RICERCA      
        	if(!immobiliFiltrati.isEmpty()) {

            	this.maxSforamentoBudget=model.getMassimoSforamento();
            	this.massimoBudgetMin = model.getMassimoBudgetMinimo();
        		this.comboBoxVoluto.getItems().clear();
        		this.comboBoxBudget.getItems().clear();
        		
            	// riempo i campi
            	this.sforo.setText(String.valueOf(this.maxSforamentoBudget));            	
            	for(int i=5000;i<=this.massimoBudgetMin;i+=5000)
            		this.comboBoxBudget.getItems().add(i);            	
            	for(Immobile filtrato:immobiliFiltrati) 
            		this.comboBoxVoluto.getItems().add(filtrato);
    

        		// disabilito la sezione qualita
        		this.pesoRent.setDisable(true);
        		this.pesoCrime.setDisable(true);
        		this.pesoMq.setDisable(true);
        		this.pesoSchool.setDisable(true);
        		this.pesoYear.setDisable(true);
        		this.btnInviaQualita.setDisable(true);
        		
	    		// abilito la sezione budget
	    		// mando alla sezione budget   
	    		this.comboBoxVoluto.setDisable(false);
	    		this.comboBoxBudget.setDisable(false);
	    		this.sforo.setDisable(false);
	    		this.btnCerca.setDisable(false);  
	        	this.btnReset.setDisable(false);   
	    		this.txtRisultatoRicorsione.setDisable(false); 
	    		
	    		this.txtRisultatoRicorsione.clear();
    			this.txtRisultatoRicorsione.appendText("Potrebbe volerci un po' (massimo un minuto)\n");
            	
	        
				
    		} else { // il filtro è troppo ristretto, zero immobili filtrati
        		this.doReset(null);
        		this.mexApp.setText("Attenzione ! Il campo di ricerca impostato con i FILTRI "+
        				"è troppo ristretto. Ti consigliamo di inserire meno filtri, o "+
        				"di ampliare il range di quelli desiderati!");
        		this.mexApp.setFill(Color.RED);
        		// disabilito la sezione qualita
        		this.pesoRent.setDisable(true);
        		this.pesoCrime.setDisable(true);
        		this.pesoMq.setDisable(true);
        		this.pesoSchool.setDisable(true);
        		this.pesoYear.setDisable(true);
        		this.btnInviaQualita.setDisable(true);
        	}
    	}

    }

    
    @FXML
    void doCercaPortafoglio(ActionEvent event) { 
    	// Avvisare se i pesi sono tutti a zero, senza bloccare
    	// "Attenzione: i pesi delle caratteristiche di qualità sono tutti a zero
    	// se non è una scelta per renderli tutti equilivello, allora si
    	// consiglia di settare le quantità e non prendere in considerazione
    	//i portafoglio generato di seguito"    	
    	
    	if(this.comboBoxQualQuan.getValue()!=null) {
    		// ristampo a nero, nel caso in cui fosse diventato rosso
        	this.mexApp.setFill(Color.BLACK);
    		this.mexApp.setText("Messaggi inviati dall'applicazione:  ...");
        	// a seconda che sia qual o quan diabilito una o un'altra parte
        	// abilito la selezione
    		
    		boolean qualita = true;
    		// QUALITA'
        	if(this.comboBoxQualQuan.getValue()=="Qualità"){
        		System.out.println("entra");
        		qualita = true;
        		// verifico se i pesi sono anomali (tutti a zero)
        		// se così avviso l'utente, potrebbe non essere un errore
        		// perchè scelto dall'utente
        		boolean sliderTuttiZero = true;
        		// i pesi li ho già salvati nel metodo invia 
    			
        		if(this.pesoCrimeRateVar!=0)
            		sliderTuttiZero=false;
        		if(this.pesoRentVar!=0)
            		sliderTuttiZero=false;
        		if(this.pesoSchoolRateVar!=0)
            		sliderTuttiZero=false;
        		if(this.pesoMqVar!=0)
            		sliderTuttiZero=false;
        		if(this.pesoYearVar!=0)
            		sliderTuttiZero=false;
        		if(sliderTuttiZero) {
        			this.mexApp.setText("Attenzione: i pesi delle caratteristiche "+
        				"di qualità sono tutti a zero "+
        			    "se non è una scelta per renderli tutti equilivello, allora si "+
        			    "consiglia di settare le quantità e non prendere in considerazione "+
        			    "portafoglio generato di seguito");
        			this.mexApp.setFill(Color.RED);
        		}        		
        		
        	} else
        		qualita = false; // è quantità
        	
        	// tutti i controlli sono stati fatti nei metodi precedenti tranne che su immobile voluto
        	if(this.comboBoxVoluto.getValue()!=null)
        		this.immobileVolutoVar = this.comboBoxVoluto.getValue();
        	// altrimenti resta null
        	
        	if(this.comboBoxBudget.getValue()!=null) {
        		
        		if(this.immobileVolutoVar!=null) {
            		if(this.immobileVolutoVar.getMarketValue()>this.comboBoxBudget.getValue()) {
            			this.txtRisultatoRicorsione.clear();
            			this.txtRisultatoRicorsione.appendText("Attenzione ! Hai selezionato un BUDGET "+
            					this.comboBoxBudget.getValue().toString()+" $ inferiore\n"+
            					"al costo dell'immobile che vuoi necessariamente inserire "+
            					this.immobileVolutoVar.getMarketValue()+" $\n "+
            					"Prova a modificare il budget o a inserire un altro immobile ");                	
            			this.mexApp.setText("ERRORE - INCONGRUENZA tra i costi BUDGET e IMMOBILE SELEZIONATO");
                		this.mexApp.setFill(Color.RED);
                		this.comboBoxBudget.setValue(null);
                		this.comboBoxVoluto.setValue(null);
                		this.immobileVolutoVar = null;                		
            			return;
            		}
            	
        		}
        		List<Immobile> portafoglio = model.getPortafoglioImmobiliare(qualita, 
            			this.comboBoxBudget.getValue(), this.maxSforamentoBudget, this.immobileVolutoVar);
            	Collections.sort(portafoglio);
            	if(portafoglio!=null && !model.isTimeout()) {
            		this.txtRisultatoRicorsione.clear();
            		this.txtRisultatoRicorsione.appendText(" 	-----      ECCO IL TUO PORTAFOGLIO IMMOBILIARE !      -----\n"+
            		"\nPhilly Real Estate ti consiglia il seguente elenco di immobili:\n\n" );
            		float spesaComplessiva=0;
            		for(Immobile imm : portafoglio) {
            			this.txtRisultatoRicorsione.appendText(imm.toString()+" - "+
            					imm.getMarketValue()+" $");
            			if(qualita)
            				this.txtRisultatoRicorsione.appendText(" - "+imm.getPunteggio()+" punti qualità");
            			this.txtRisultatoRicorsione.appendText("\n");
            			spesaComplessiva+=imm.getMarketValue();
            		}
            		this.txtRisultatoRicorsione.appendText("\nIl tuo portafoglio costa: "+spesaComplessiva+" $\n"+
            				"\nGrazie per averci scelto!");
            	}else if(model.isTimeout()) {
            		this.mexApp.setText("Non è stato generato alcun portafoglio immobiliare "+
        					"E' necessario inserire più filtri per evitare un range di immoobili troppo "+
        					"elevato che non permette di ottenere una soluzione ottimale in tempi accettabili ");
            		this.mexApp.setFill(Color.RED);
            		this.txtRisultatoRicorsione.clear();
        			this.txtRisultatoRicorsione.appendText("Non è stato generato alcun portafoglio immobiliare\n"+
        					"E' necessario inserire più filtri per evitare un range \ndi immoobili troppo "+
        					"elevato che non permette di ottenere \n una soluzione ottimale in tempi accettabili ");
            	}
        		
        	} else {
        		this.mexApp.setText("Attenzione: non hai selezionato il BUDGET ! "+
        				"Seleziona un budget per anndare avanti e ottenere il tuo "+
        			    "sportafoglio immobiliare.");
        			this.mexApp.setFill(Color.RED);
        	}
        	
    	}
    }

    @FXML
    void doReset(ActionEvent event) {

    	// resetto tutti i box, i pulsanti e le variabili della ricorsione
//    	System.out.println("entra");
    	// RESET VARIABILI
    	postalCode = null;
    	propType = null; 
    	minMq=null;
    	maxMq=null;
    	minYear=null;
    	maxYear=null;
    	pesoMqVar=0;
    	pesoRentVar=0;
    	pesoCrimeRateVar=0;
    	pesoYearVar=0; 
    	pesoSchoolRateVar=0;
    	immobileVolutoVar=null;
    	maxSforamentoBudget=0;
    	massimoBudgetMin=0;
    	
    	// RESET BOX E PULSANTIthis.mexApp.setFill(Color.BLACK);
		this.mexApp.setText("Messaggi inviati dall'applicazione:  ...");
		this.mexApp.setFill(Color.BLACK);
		this.setSezioneDue();
		
    	
		this.pesoCrime.setValue(0);
		this.pesoMq.setValue(0);
		this.pesoRent.setValue(0);
		this.pesoSchool.setValue(0);
		this.pesoYear.setValue(0); 
		this.comboBoxVoluto.getItems().clear();
//		System.out.println("Voluti: "+this.comboBoxVoluto.getItems().toString());		
		this.comboBoxBudget.getItems().removeAll();
		System.out.println("Budget: "+this.comboBoxBudget.getItems().toString());
		this.sforo.setText("sforo");
		this.txtRisultatoRicorsione.clear();
    	
    
    	
    	// ABILITO/DISABILITO
    	
		this.btnInvia.setDisable(false);
		this.comboBoxQualQuan.setDisable(false);
		
		//abilito
		this.comboBoxZonaPortaffoglio.setDisable(false);
		this.comboBoxTipoImmobile.setDisable(false);
		this.comboBoxMinMq.setDisable(false);
		this.comboBoxMaxMq.setDisable(false);
		this.comboBoxMinYear.setDisable(false);
		this.comboBoxMaxYear.setDisable(false);
		
		// diisabilito
		this.pesoCrime.setDisable(true);
		this.pesoMq.setDisable(true);
		this.pesoRent.setDisable(true);
		this.pesoSchool.setDisable(true);
		this.pesoYear.setDisable(true); 
		this.comboBoxVoluto.setDisable(true);
		this.comboBoxBudget.setDisable(true);
		this.sforo.setDisable(true);
		this.btnCerca.setDisable(true);  
		this.txtRisultatoRicorsione.setDisable(true);
    	this.btnReset.setDisable(true);   
    	this.btnInviaQualita.setDisable(true);
    }
    
    
    // INIZIALIZZAZIONE

    @FXML
    void initialize() {
        assert boxSceltaAnalisi != null : "fx:id=\"boxSceltaAnalisi\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxCaratteristica != null : "fx:id=\"comboBoxCaratteristica\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnAbilitaAnalisi != null : "fx:id=\"btnAbilitaAnalisi\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert txtAbilita != null : "fx:id=\"txtAbilita\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert boxAnalisi != null : "fx:id=\"boxAnalisi\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnElencoZonaCaratt != null : "fx:id=\"btnElencoZonaCaratt\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxOrdineElenco != null : "fx:id=\"comboBoxOrdineElenco\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert txtElenco != null : "fx:id=\"txtElenco\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnTrovaZonaCaratt != null : "fx:id=\"btnTrovaZonaCaratt\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert txtZonaAlto != null : "fx:id=\"txtZonaAlto\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert txtZonaBasso != null : "fx:id=\"txtZonaBasso\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnAbilitaNuovaAnalisi != null : "fx:id=\"btnAbilitaNuovaAnalisi\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxZonaPortaffoglio != null : "fx:id=\"comboBoxZonaPortaffoglio\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxTipoImmobile != null : "fx:id=\"comboBoxTipoImmobile\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxMinMq != null : "fx:id=\"comboBoxMinMq\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxMaxMq != null : "fx:id=\"comboBoxMaxMq\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxMinYear != null : "fx:id=\"comboBoxMinYear\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxMaxYear != null : "fx:id=\"comboBoxMaxYear\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxQualQuan != null : "fx:id=\"comboBoxQualQuan\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnInvia != null : "fx:id=\"btnInvia\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert mexApp != null : "fx:id=\"mexApp\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert pesoRent != null : "fx:id=\"pesoRent\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert pesoCrime != null : "fx:id=\"pesoCrime\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert pesoSchool != null : "fx:id=\"pesoSchool\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert pesoYear != null : "fx:id=\"pesoYear\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert pesoMq != null : "fx:id=\"pesoMq\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnInviaQualita != null : "fx:id=\"btnInviaQualita\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxVoluto != null : "fx:id=\"comboBoxVoluto\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert sforo != null : "fx:id=\"sforo\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert comboBoxBudget != null : "fx:id=\"comboBoxBudget\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnCerca != null : "fx:id=\"btnCerca\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert txtRisultatoRicorsione != null : "fx:id=\"txtRisultatoRicorsione\" was not injected: check your FXML file 'RealEstate.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'RealEstate.fxml'.";

    }

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;			
		this.setSezioneUno();
		this.setSezioneDue();			
	}
	
	
	public void setSezioneUno() {
			
		this.comboBoxOrdineElenco.getItems().clear();
		this.comboBoxOrdineElenco.getItems().add("Crescente");
		this.comboBoxOrdineElenco.getItems().add("Decrescente");
		this.comboBoxOrdineElenco.setValue("Decrescente");

		this.comboBoxCaratteristica.getItems().clear();
		this.comboBoxCaratteristica.getItems().addAll(this.model.getCaratteristiche());
		
	}
	
	public void setSezioneDue() {		

		// settaggio parte A
		this.comboBoxZonaPortaffoglio.getItems().clear();
		this.comboBoxZonaPortaffoglio.setValue(null);
		this.comboBoxZonaPortaffoglio.getItems().addAll(model.getAllZoneList());
		
		this.comboBoxTipoImmobile.getItems().clear();
		this.comboBoxTipoImmobile.setValue(null);
		this.comboBoxTipoImmobile.getItems().addAll(model.getAllTipologie());
		
		this.comboBoxMinMq.getItems().clear();
		this.comboBoxMaxMq.getItems().clear();
		this.comboBoxMinMq.setValue(null);
		this.comboBoxMaxMq.setValue(null);
		this.comboBoxMinMq.getItems().addAll(model.getAllMq());
		this.comboBoxMaxMq.getItems().addAll(model.getAllMq());
		// dopo fare verifica che siano uno minore ell'altro, altrimenti
		// messaggio di errore e non passa alla parte successiva
		
		this.comboBoxMinYear.getItems().clear();
		this.comboBoxMaxYear.getItems().clear();
		this.comboBoxMinYear.setValue(null);
		this.comboBoxMaxYear.setValue(null);
		this.comboBoxMinYear.getItems().addAll(model.getAllYears());
		this.comboBoxMaxYear.getItems().addAll(model.getAllYears());
		// dopo fare verifica che siano uno minore ell'altro, altrimenti
		// messaggio di errore e non passa alla parte successiva
		
		this.comboBoxQualQuan.getItems().clear();
		this.comboBoxQualQuan.setValue(null);
		this.comboBoxQualQuan.getItems().add("Qualità");
		this.comboBoxQualQuan.getItems().add("Quantità");
		
		this.pesoCrime.setValue(0);
		this.pesoMq.setValue(0);
		this.pesoRent.setValue(0);
		this.pesoSchool.setValue(0);
		this.pesoYear.setValue(0); 
		
		// la parte B si può settare solo dopo la gestione dei filtri
		// dopo il pulsante invia

		// inizialmente vuoti
		this.comboBoxVoluto.getItems().clear();
		this.comboBoxBudget.getItems().clear();
		
		
	}
	
	
	
	
	
	
	
}
