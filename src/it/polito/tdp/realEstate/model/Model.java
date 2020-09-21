package it.polito.tdp.realEstate.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import it.polito.tdp.realEstate.db.RealEstateDAO;

public class Model {

	private RealEstateDAO dao;
	private List<String> caratteristiche;
	private Map<Integer, Zona>zoneIdMap;
	
	private Map<String, Immobile> immobiliIdMap;
	private List<Immobile> immobiliList;
	private List<Immobile> bestPortafoglio;
	private float bestCostoPortafoglio; // non conta eventuale immobile voluto
	private float bestPunteggioPortafoglioMedio;
	private Integer maxNumPortafoglio = 10;
	
	// pesi punteggio
	private float pesoMq;
	private float pesoRent;
	private float pesoCrimeRate;
	private float pesoYear;
	private float pesoSchoolRate;
	
	// valoriYear -> più vecchio valore più basso, più recente valore più alto
	Map<Integer, Float> valoriYear; // associo a ogni anno un valore che va da zero a #anni presenti nel db che poi normalizzo a 100
	// valoriMq
	Map<Integer, Float> valoriMq; // dal db frzerò i mq, chiave,  a intero, i centimetri non mi interesano
	// valoriRent
	Map<Integer, Float> valoriRent; 
	// valoriCrime
	Map<Integer, Float> valoriCrime; 
	// valoriSchool
	Map<Integer, Float> valoriSchool; 
				
	// filtri obbligatori
	private float budgetMax=0;
	private float budgetMin=0;
	private Integer minCase;
	private Immobile immobileVoluto;
	

	long startTime = System.nanoTime();
	boolean timeout = false;
	private float massimoBudgetMinimo=0;
	private float massimoSforamento=0;
	
	
	public Model() {
		dao = new RealEstateDAO();
		this.caratteristiche = new ArrayList<String>();
		this.zoneIdMap = new HashMap<Integer, Zona>();
		this.zoneIdMap = dao.getAllZones();
		
		Collections.addAll(this.caratteristiche, "Tasso di criminalità", "Livello scolastico",
				"Valore di mercato medio", "Affitto medio stimato", "Anno di costruzione", "Metratura media");
		
		this.immobiliIdMap = new HashMap<String, Immobile>();
		this.immobiliList = new ArrayList<Immobile>();
		
		this.valoriMq = new HashMap<Integer, Float>();
		this.associaValoriMq();
		
		this.valoriRent = new HashMap<Integer, Float>();
		this.associaValoriRent();
		
		this.valoriYear = new HashMap<Integer, Float>();
		this.associaValoriYear();
		
		this.valoriCrime = new HashMap<Integer, Float>();
		this.associaValoriCrime();
		
		this.valoriSchool = new HashMap<Integer, Float>();
		this.associaValoriSchool();
	}
	
	
	private void associaValoriMq() {
		// chiave mq, value valore 
		this.valoriMq = dao.getAllMq(); //  nel dao sono a zero
		Integer max = dao.getMaxMq();
		Integer min = dao.getMinMq();
		for(Integer mq : valoriMq.keySet()) { // ciclo sulle chiavi: gli anni
			float valore = 100*((float)(max-mq)/(float)(max-min));
			this.valoriMq.put(mq, valore);
		}
	}
	
	private void associaValoriRent() {
		// chiave mq, value valore 
		this.valoriRent = dao.getAllRents(); //  nel dao sono a zero
		Integer max = dao.getMaxRent();
		Integer min = dao.getMinRent();
		for(Integer rent : valoriRent.keySet()) { // ciclo sulle chiavi: gli anni
			float valore = 100*((float)(max-rent)/(float)(max-min));
			this.valoriRent.put(rent, valore);
		}
	}

	private void associaValoriYear() {
		// chiave anno, value valore 
		this.valoriYear = dao.getAllYears(); //  nel dao sono a zero
		Integer max = dao.getMaxYear();
		Integer min = dao.getMinYear();
		for(Integer year : valoriYear.keySet()) { // ciclo sulle chiavi: gli anni
			float valore = 100*((float)(max-year)/(float)(max-min));
//			System.out.println("year:"+year+" - "+"valore:"+valore+" - max:"+max+" - min:"+min+"\n");
			this.valoriYear.put(year, valore);
		}
	}
	
	private void associaValoriCrime() {
		// chiave anno, value valore 
		this.valoriCrime = dao.getAllCrime(); //  nel dao sono a zero
		Integer max = dao.getMaxCrime();
		Integer min = dao.getMinCrime();
		for(Integer crime : valoriCrime.keySet()) { // ciclo sulle chiavi: gli anni
			float valore = 100*((float)(max-crime)/(float)(max-min));
			this.valoriCrime.put(crime, valore);
		}
	}
	
	private void associaValoriSchool() {
		// chiave anno, value valore 
		this.valoriSchool = dao.getAllSchools(); //  nel dao sono a zero
		Integer max = dao.getMaxSchool();
		Integer min = dao.getMinSchool();
		for(Integer school : valoriSchool.keySet()) { // ciclo sulle chiavi: gli anni
			float valore = 100*((float)(max-school)/(float)(max-min));
			this.valoriSchool.put(school, valore);
		}
	}


	public List<String> getCaratteristiche() {
		return caratteristiche;
	}

	public void setCaratteristiche(List<String> caratteristiche) {
		this.caratteristiche = caratteristiche;
	}


	public Map<Integer, Zona> getZoneIdMap() {
		return zoneIdMap;
	}


	public void setZoneIdMap(Map<Integer, Zona> zoneIdMap) {
		this.zoneIdMap = zoneIdMap;
	}	
	
	public List<Zona> getElencoPerCarattristica(String caratteristica, String ordine) {
		return dao.getElencoPerCarattristica(caratteristica, ordine);
	}


	public String getZonaMigliorePerCaratteristica(String caratteristica) {		
		
		Zona zonaBest=null;
		
		for(Zona z : this.zoneIdMap.values()) {
			
			if(zonaBest==null)
				zonaBest=z;
			
			switch(caratteristica) {
			case "Tasso di criminalità":
				// crime rate basso è migliore
				if(z.getAvgCrimeRate() < zonaBest.getAvgCrimeRate())
					zonaBest = z;
				break;
			case "Livello scolastico":
				//school rate alto è migliore
				if(z.getAvgSchoolRate()>zonaBest.getAvgSchoolRate())
					zonaBest = z;
				break;
			case "Valore di mercato medio":
				// valore di mercato alto è migliore perchè è una buona casa
				if(z.getMarketValue()>zonaBest.getMarketValue())
					zonaBest = z;
				break;
			case "Affitto medio stimato":
				// affitto alto è migliore perchè l'investitore deve affittare
				if(z.getAvgRent()>zonaBest.getAvgRent())
					zonaBest = z;
				break;
			case "Anno di costruzione":
				// anno di costruzione alto=recente è migliore
				if(z.getAvgYear()>zonaBest.getAvgYear())
					zonaBest = z;
				break;
			case "Metratura media":
				// casa grande migliore
				if(z.getAvgMq()>zonaBest.getAvgMq())
					zonaBest = z;
				break;		
	    	}
		}
		
		// ho la zona migliore, ritorno la stringa con
		// nome zona e valore della caratteristica richiesta
		switch(caratteristica) {
		case "Tasso di criminalità":
			return zonaBest.getneighborhoodName().toString()+" - "+ zonaBest.getAvgCrimeRate();
		case "Livello scolastico":
			return zonaBest.getneighborhoodName().toString()+" - "+ zonaBest.getAvgSchoolRate();
		case "Valore di mercato medio":
			return zonaBest.getneighborhoodName().toString()+" - "+ zonaBest.getMarketValue();
		case "Affitto medio stimato":
			return zonaBest.getneighborhoodName().toString()+" - "+ zonaBest.getAvgRent();
		case "Anno di costruzione":
			return zonaBest.getneighborhoodName().toString()+" - "+ zonaBest.getAvgYear();
		case "Metratura media":
			return zonaBest.getneighborhoodName().toString()+" - "+ zonaBest.getAvgMq();
    	}
		
		return null;
	}


	public String getZonaPeggiorePerCaratteristica(String caratteristica) {
		
		Zona zonaWorst=null;
		
		for(Zona z : this.zoneIdMap.values()) {
			
			if(zonaWorst==null)
				zonaWorst=z;
			
			switch(caratteristica) {
			case "Tasso di criminalità":
				// crime rate alto è peggiore
				if(z.getAvgCrimeRate() > zonaWorst.getAvgCrimeRate())
					zonaWorst = z;
				break;
			case "Livello scolastico":
				//school rate basso è peggiore
				if(z.getAvgSchoolRate()<zonaWorst.getAvgSchoolRate())
					zonaWorst = z;
				break;
			case "Valore di mercato medio":
				// valore di mercato basso è peggiore perchè è una buona casa
				if(z.getMarketValue()<zonaWorst.getMarketValue())
					zonaWorst = z;
				break;
			case "Affitto medio stimato":
				// affitto basso è peggiore perchè l'investitore deve affittare
				if(z.getAvgRent()<zonaWorst.getAvgRent())
					zonaWorst = z;
				break;
			case "Anno di costruzione":
				// anno di costruzione basso=vecchio è peggiore
				if(z.getAvgYear()<zonaWorst.getAvgYear())
					zonaWorst = z;
				break;
			case "Metratura media":
				// casa piccola peggiore
				if(z.getAvgMq()<zonaWorst.getAvgMq())
					zonaWorst = z;
				break;		
	    	}
		}
		
		// ho la zona peggiore, ritorno la stringa con
		// nome zona e valore della caratteristica richiesta
		switch(caratteristica) {
		case "Tasso di criminalità":
			return zonaWorst.getneighborhoodName().toString()+" - "+ zonaWorst.getAvgCrimeRate();
		case "Livello scolastico":
			return zonaWorst.getneighborhoodName().toString()+" - "+ zonaWorst.getAvgSchoolRate();
		case "Valore di mercato medio":
			return zonaWorst.getneighborhoodName().toString()+" - "+ zonaWorst.getMarketValue();
		case "Affitto medio stimato":
			return zonaWorst.getneighborhoodName().toString()+" - "+ zonaWorst.getAvgRent();
		case "Anno di costruzione":
			return zonaWorst.getneighborhoodName().toString()+" - "+ zonaWorst.getAvgYear();
		case "Metratura media":
			return zonaWorst.getneighborhoodName().toString()+" - "+ zonaWorst.getAvgMq();
    	}
		
		return null;
	}
	
	
	// RICORSIONE
	// CERCO IL PORTAFOGLIO OTTIMO IN BASE A INDICE DI QUALITA'
	
	
	// GESTISCO PRIMA TUTTI I FILTRI
	public List<Immobile> gestioneFiltri(Integer postalCode, String propType, 
			Integer minMq, Integer maxMq, Integer minYear, Integer maxYear,
			float pesoMq, float pesoRent, float pesoCrimeRate, float pesoYear, float pesoSchoolRate){

//		float massimoBudgetMinimo=0;
//		float maxSforamentoBudget=0;//sono già a zero le variabili con this

		
		// volendo posso aggiunger anche il filtro sul numero di bagni e cammere
		// ma non troopo utile
		
		// i primi filtri sono opzionali e servono solo per restringere il campo di ricerca
		// i pesi servono ad assegnare l'importanza della caratteristica nel sistema di
		// punteggio per il calcolo di qualità del portafoglio
		this.pesoMq = pesoMq;
		this.pesoRent = pesoRent;
		this.pesoCrimeRate = pesoCrimeRate;
		this.pesoYear = pesoYear;
		this.pesoSchoolRate = pesoSchoolRate;
		
		
		
		// se filtro troppo restrittivo e non trova immobili o se ne trova
		// pochi (es meno di 10) avvisa l'utente
		
		// scrivere anche prima un messaggio che avvisi l'utente di non usare troppi filtri
		
		// seleziono gli immobili da mettere nell'id map in base ai filtri scelti dall'utente
		// passo tutti i filtri, se null non aggiungo il filtro alla query
		this.immobiliIdMap.clear();
		this.immobiliList.clear();
		this.massimoBudgetMinimo=0;
		this.massimoSforamento=0;
		this.immobiliIdMap.clear();
		this.immobiliList.clear();
		this.immobiliIdMap = dao.getImmobiliFiltrati(postalCode, propType, 
			 minMq, maxMq, minYear, maxYear); 
		
		for(Immobile immobile : this.immobiliIdMap.values()) {
			immobile.setPunteggio(this.calcolaPunteggioImmobile(immobile));
			System.out.println(immobile+" punteggio "+immobile.getPunteggio());
			this.immobiliList.add(immobile);
			this.massimoBudgetMinimo+=immobile.getMarketValue();
		}
		// ORDINO LA LISTA DI IMMOBILI FILTRATI IN BBASE AL PUNTEGGIO
		// ER FACILITARE LA RICORSIONE (DECRESCENTE, prima quelli con punteggio alto)
		Collections.sort(this.immobiliList);
//		for(Immobile immobile : this.immobiliList)
//			System.out.println(immobile+" PUNTEGGIO: "+immobile.getPunteggio()+"PREZZO: "+immobile.getMarketValue());
		

		this.massimoSforamento = (this.massimoBudgetMinimo/this.immobiliList.size())/2; 
		// posso sforare al massimo del costo di mezzo immobile 
		// (calcolato come la metà della media del costo di tutti gli immobili filtrati)
		this.massimoBudgetMinimo=this.massimoBudgetMinimo-1;// sotto di uno al massimo valore di tutti gli immobili filtrati
		
		System.out.println(massimoBudgetMinimo);
		
		return immobiliList;
	
	}
	
	// QUESTO SARA' IL METODO CHE RICHIAMA LA RICORSIONE
	public List<Immobile> getPortafoglioImmobiliare(boolean qualita, 
			float budgetScelto, float maxSforoBudget,
			Immobile immobileVoluto){ 
		

		this.timeout=false;
		this.budgetMin = budgetScelto-maxSforoBudget;
		this.budgetMax = budgetScelto+maxSforoBudget;
		if(this.budgetMin<0)
			this.budgetMin=0;
		System.out.println("Budget min: "+ budgetMin);
		System.out.println("Budget max: "+ budgetMax);
		System.out.println("Best ount: "+ bestPunteggioPortafoglioMedio);
		
		// variabili per la ricorsione
		this.bestPunteggioPortafoglioMedio = - 1; // cos', anche se i pesi sono a zero, nel primo
		// giro della ricorsione entra di sicuro
		this.bestCostoPortafoglio = Float.MAX_VALUE;
		List<Immobile> parziale = new ArrayList<Immobile>();
		this.bestPortafoglio = new ArrayList<Immobile>();
		

		float punteggioPortafoglio = 0;	
		float costoPortafoglio=0;	
		Integer livello=0;
		
		// se c'è inserisco immobile voluto
		if(immobileVoluto!=null) {			
			this.immobileVoluto = immobileVoluto;			
			if(this.immobiliIdMap.containsValue(immobileVoluto))
				this.immobiliIdMap.remove(immobileVoluto);
			// aggiungo nel best
			this.immobileVoluto.setPunteggio(this.calcolaPunteggioImmobile(immobileVoluto));
			this.bestPortafoglio.add(this.immobileVoluto);
			parziale.add(immobileVoluto);
			// non modifico il punteggio del best perchè rischio altrimenti nell'if di non entrare
			// e quindi, finchè non lo modifica la ricorsione, mantengo -1
//				punteggioPortafoglio=this.calcolaPunteggioImmobile(immobileVoluto);
			// comunque modifico il punteggio portafoglio perchè altrimenti
			// avrei una media su un valore in meno
			costoPortafoglio=immobileVoluto.getMarketValue();
			punteggioPortafoglio=immobileVoluto.getPunteggio();
			livello++;
		}
		

		this.startTime = System.nanoTime();
		if(qualita)
			cercaQualita(parziale, livello, punteggioPortafoglio, costoPortafoglio); 
		else
			cercaQuantita(parziale, livello, costoPortafoglio); 
			
		// passo il livello perchè non voglio inserire 		
		// un numero di case troppo grande (altrimenti il programma sarà lento)
		// la ricerca considera solo le case della ricorsione, il nMax imposto non considera
		// l'immobile voluto (considerato però nel ) -> tutto questo scriverlo in un
		// messaggio per l'utente
		System.out.println(this.bestPortafoglio);
		return this.bestPortafoglio;
	}


	// DATO CHE NON VOGLIO FARE VERIFICA SUL PUNTEGGIO TOTALE MA SU
				// QUELLO MEDIO DEL PORTAFOGLIO - ALTRIMENTI IL PUNTEGGIO MIGLIORE
				// SAREBBE SEMPRE QUELLO CHE PRIVILEGIA LA QUANTITA' (PORTAFOGLIO CON MAGGIOR
				// NUMERO DI IMMOBILI)-,
				// VEIRIFICO IL PUNTEGGIO SOLO SE HO GIA' RIEMPITO SUFFICIENZA IL
				// PORTAFOGLIO (HO SFRUTTATO IL budgetMax QUASI A PIENO) - es AL 90 % -
				// E QUINDI POSSO PERMETTERMI DI SCARTARE IN BASE AL PUNTEGGIO
				// SENZA RISCHIO 
				// ALTIMENTI, SENZA VINCOLO DELLA PERCENTUALE SUL budgetMax E FACENDO IL CONFRONTO
				// SOLO SUL PUNTEGGIO MEDIO, RISCHIEREI DI NON AVERE ALTRI INSERIMENTI OLTRE
				// AL PRIMO SE IL PRIMO E' GIA' QUELLO CON PUNTEGGIO MIGLIORE
	
	

	// RICORSIONE CHE PRIVILEGIA LA quaLità DI IMMOBILI NEL PORTAFOGLIO
	private void cercaQualita(List<Immobile> parziale, int livello, float punteggioParziale, 
			float costoParziale) {		

				
		// CONDIZIONE DI TERMINAZIONE
		
		if(costoParziale>this.budgetMin && costoParziale<this.budgetMax) { // termina ricerca parziale e tolgo l'ultimo				
			
//			System.out.println("COSTO PARZIALE"+costoParziale+" "+parziale);			
			
			// verifico con una soglia mminima di budget, a questo punto, se la supera sono entro
			// i limiti che vuole l'utente e sto spendendo abbbastanza budget, quindi
			// scelgo quello con punteggio più alto
			// (faccio questo perchè se confrontassi solo il punteggio totale privilegerei solo qualità
			//  -entra sempre quello con più immobili perchè somma punteggi più alta -, allo stesso modo, 
			// confrontando il punteggio medio rischio di avere uno "zaino" semivuoto - inserendo subito un
			// immobile con punteggio alto, o il più alto, rischio che il nuovo punteggio sia sempre inferiore
			// a quello del best precedente e quindi non potrei più inserire immobile -> Non riuscirei a 
			// riempire il busget, quindi faccio il controllo puunteggio solo a parità di costo 
			// del portafoglio, altrimenti inserisco quello con migliore quantità-
			
//			System.out.println("Costo portafoglio attuale: "+costoParziale);
//			System.out.println("Costo best : "+this.bestCostoPortafoglio);
			
			float punteggioPortafoglioAttualeMedio = (punteggioParziale)/(livello);			
			
//			System.out.println("YASSSSSSSSSSSSSSSSSSSSSSSSSSS - PUNTEGGIO BEST: "+this.bestPunteggioPortafoglioMedio+" PUNTEGGIO PARZIALE: "+punteggioPortafoglioAttualeMedio);
			
			if(punteggioPortafoglioAttualeMedio>this.bestPunteggioPortafoglioMedio) {
//				System.out.println("\n - BEST - \n");
//				System.out.println("Parziale: "+parziale);					
//				System.out.println("COSTO PORTAFOGLIO ATTUALE"+costoParziale);
//				System.out.println("Vecchio BEST "+this.bestPortafoglio);
//				System.out.println("---------------------- PUNTEGGIO VECCHIO BEST: "+this.bestPunteggioPortafoglioMedio+" PUNTEGGIO NUOVO BEST: "+punteggioPortafoglioAttualeMedio);
				
				// svuoto il best
				this.bestPortafoglio.removeAll(bestPortafoglio);
				// inserisco la nuova best
				for(int i=0;i<livello;i++) {
					this.bestPortafoglio.add(parziale.get(i));				
				}
				this.bestCostoPortafoglio = costoParziale; 
				this.bestPunteggioPortafoglioMedio = punteggioPortafoglioAttualeMedio;

//				System.out.println("NUOVO BEST "+this.bestCostoPortafoglio+" "+this.bestPortafoglio);
				
			}
			
			
			return;
		}
		
		// INSERIMENTO
		for(Immobile imm : this.immobiliIdMap.values()) {
			if((System.nanoTime()-this.startTime)/1000000000<60){
			// verifico se il tempo del metodo minore di 60 sec
				// altrimenti chiedo più filtri
				if(!parziale.contains(imm)) { 
					parziale.add(imm);
	//				System.out.println(imm.getPunteggio()+"\n");
					punteggioParziale+=imm.getPunteggio(); // l'ho già inserito nell'immobile nella creazione della lista filtrata
					costoParziale+=imm.getMarketValue();
					
	//				System.out.println("Singolo punteggio: "+imm.toString()+" "+calcolaPunteggioImmobile(imm));
					
					
					cercaQualita(parziale, livello+1, punteggioParziale, costoParziale);
					
					parziale.remove(imm);
					costoParziale = costoParziale - imm.getMarketValue();				
					punteggioParziale = punteggioParziale - imm.getPunteggio();
				}
			} else {
				this.timeout = true;
				return;
			}
		} 
		
	}
	

	// RICORSIONE CHE PRIVILEGIA LA quaNtità DI IMMOBILI NEL PORTAFOGLIO
	// MOLTE CASE, PROBABIIOMENTE PICCOLE/SCCARSA QUALITA' A SECONDA DEI FILTRI, A COSTO INFERIORE
	public void cercaQuantita(List<Immobile> parziale, int livello,	float costoParziale) {
		
		// CONDIZIONE DI TERMINAZIONE		
		//if(livello>=this.maxNumPortafoglio) condizione su limite immobili iseribili, la metto????
		if(costoParziale>this.budgetMin && costoParziale<this.budgetMax) { // termina ricerca parziale e tolgo l'ultimo			
			
			 if(costoParziale<this.bestCostoPortafoglio &&
						(livello==this.bestPortafoglio.size())) { // a parità di size del portafoglio inserisco quello che costa meno
					// altrimenti scelgo quello che costa meno (a parità di size, numero di immobili -> vado su quantità)
					// questo mi permette comunque l'inserimento
					
					// svuoto il best
					this.bestPortafoglio.removeAll(bestPortafoglio);
					// inserisco la nuova best
					for(int i=0;i<livello;i++) { // inserisco tutti tranne l'ultimo perchè fuori budgetMax
						this.bestPortafoglio.add(parziale.get(i));				
					}
					this.bestCostoPortafoglio = costoParziale; 
					
				} else if(livello>this.bestPortafoglio.size()) {
					// se non ho parità di prezzo metto quello che mi permette di avere più riempimento					
					// svuoto il best
					this.bestPortafoglio.removeAll(bestPortafoglio);
					// inserisco la nuova best
					for(int i=0;i<livello;i++) { // inserisco tutti tranne l'ultimo perchè fuori budgetMax
						this.bestPortafoglio.add(parziale.get(i));				
					}
					this.bestCostoPortafoglio = costoParziale; 
					
				}
		}
		
		// INSERIMENTO
		for(Immobile imm : this.immobiliIdMap.values()) {
			if((System.nanoTime()-this.startTime)/1000000000<60){
				if(!parziale.contains(imm)) {
					
					parziale.add(imm);
					
					costoParziale+=imm.getMarketValue();
	
					cercaQuantita(parziale, livello+1, costoParziale);
					
					parziale.remove(imm);
					costoParziale = costoParziale - imm.getMarketValue();
				}
			} else {
				this.timeout = true;
				return;
			}
		}
	}


	private float calcolaPunteggioImmobile(Immobile imm) {
		// calcolo per ogni caratt punti

		float punteggioTotale=0;
		
		float punteggioMq = 0;
		float punteggioRent = 0;
		float punteggioYear = 0;
		float punteggioCrime = 0;
		float punteggioSchool = 0;
				
		//mq
		if(this.pesoMq!=0.0) {
			float valoreMq = this.valoriMq.get(imm.getMq());
			punteggioMq = valoreMq*this.pesoMq;
		}
		
		//rent
		if(this.pesoRent!=0.0) {
			float valoreRent = this.valoriRent.get(imm.getEstimatedRent());
			punteggioRent = valoreRent*this.pesoRent;			
		}
				
		//year
//		System.out.println("STO STAMPANDO GLI ANNI");
//		for(Integer i : this.valoriYear.keySet())
//			System.out.println(i);
		if(this.pesoYear!=0.0) {
			float valoreYear = this.valoriYear.get(imm.getYearBuilt());
			punteggioYear = valoreYear*this.pesoYear;			
		}
		
		
		//crime
		if(this.pesoCrimeRate!=0.0) {
			float valoreCrime = this.valoriCrime.get(imm.getCrimeRate());
			punteggioCrime= valoreCrime*this.pesoCrimeRate;			
		}
		
		//school
		if(this.pesoSchoolRate!=0.0) {
			float valoreSchool= this.valoriSchool.get(imm.getSchoolRate());
			punteggioSchool = valoreSchool*this.pesoSchoolRate;		
		}
		
		// sommo tutti i punteggi
		punteggioTotale = punteggioMq + punteggioRent + punteggioYear + punteggioCrime + punteggioSchool;
//		System.out.println(punteggioTotale);
		return punteggioTotale;
	}


	public float getbestPunteggioPortafoglioMedio() {
		return bestPunteggioPortafoglioMedio;
	}


	public void setbestPunteggioPortafoglioMedio(float bestPunteggioPortafoglioMedio) {
		this.bestPunteggioPortafoglioMedio = bestPunteggioPortafoglioMedio;
	}


	public List<Immobile> getBestPortafoglio() {
		return bestPortafoglio;
	}


	public void setBestPortafoglio(List<Immobile> bestPortafoglio) {
		this.bestPortafoglio = bestPortafoglio;
	}


	public float getBestCostoPortafoglio() {
		if(this.immobileVoluto!=null)
			return this.bestCostoPortafoglio+this.immobileVoluto.getMarketValue();
		else
			return bestCostoPortafoglio;
	}


	public void setBestCostoPortafoglio(float bestCostoPortafoglio) {
		this.bestCostoPortafoglio = bestCostoPortafoglio;
	}


	public boolean isTimeout() {
		return timeout;
	}


	public List<Zona> getAllZoneList() {
		List<Zona> zone = new ArrayList<Zona>();
		for(Zona z : this.zoneIdMap.values())
			zone.add(z);
		Collections.sort(zone);
		return zone;
	}


	public List<String> getAllTipologie() {
		return dao.getAllTipologie();
	}


	public List<Integer> getAllMq() {
		List<Integer> mq = new ArrayList<Integer>();
		// dò gli mq a scaglioni di 5 metri
		for(int i=5;i<=dao.getMaxMq();i+=5)
			mq.add(i);
		return mq;
	}


	public List<Integer> getAllYears() {
		List<Integer> years = new ArrayList<Integer>();
		Integer minYear = dao.getMinYear();
		Integer maxYear = dao.getMaxYear();
		for(int i = minYear;i<=maxYear;i++)
			years.add(i);
		return years;
	}


	public float getMassimoBudgetMinimo() {
		return massimoBudgetMinimo;
	}


	public void setMassimoBudgetMinimo(float massimoBudgetMinimo) {
		this.massimoBudgetMinimo = massimoBudgetMinimo;
	}


	public float getMassimoSforamento() {
		return massimoSforamento;
	}


	public void setMassimoSforamento(float massimoSforamento) {
		this.massimoSforamento = massimoSforamento;
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
