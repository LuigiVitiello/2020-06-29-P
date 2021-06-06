package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleWeightedGraph<Match, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer,Match> idMap;
	private List<Adiacenza> adiacenze;
	private boolean grafoCreato;
	private List<Match> percorso;
	private int peso;
	private int max;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap= new HashMap<>();
		this.dao.listAllMatches(idMap);
		this.adiacenze= new ArrayList<>();
		this.grafoCreato=false;
	}
	
	public void creaGrafo(int min, int mese) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(idMap,mese));
		this.adiacenze= dao.getAdiacenze(idMap, min, mese);
		
		for (Adiacenza a : this.adiacenze) {
			if (grafo.vertexSet().contains(a.getM1()) && grafo.vertexSet().contains(a.getM2())) {
				Graphs.addEdgeWithVertices(grafo, a.getM1(), a.getM2(), (double)a.getPeso());
			}
		}
		this.grafoCreato=true;
	}
	
	public boolean isGrafoCreato() {
		return grafoCreato;
	}

	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Adiacenza> ConessioneMax() {
		int max=0;
		List<Adiacenza> best= new ArrayList<>();
		for (Adiacenza a : this.adiacenze) {
			if (a.getPeso()>max) {
				max=a.getPeso();
			}
		}
		for (Adiacenza a : this.adiacenze) {
			if (a.getPeso()==max)
				best.add(a);
		}
		if (best.isEmpty()) {
			return null;
		}
		return best;
	}
	
	public Set<Match> getVertici(){
		return this.grafo.vertexSet();
	}
	
	/*
	 * creo lista adiacenze
	 *  richiamo ricorsione (lista di match)
	 *  
	 *  
	 *  condizione ultimo di parziale = m2
	 *     se peso > max salvo il cammino
	 *     
	 *  else
	 *    per tutti i successori di m1
	 *       if adiacenze !contiene partita tra m1 e m succ
	 *          aggiungo adiacenza ad adiacenze usate
	 *          aggiungo succ a parziale
	 *          ricorsione
	 *          remove
	 */
	
	public List<Match> doCollegamento(Match partenza,Match destinazione) {
		List<Match> parziale = new ArrayList<Match>();
		parziale.add(partenza);
	    max =0;
		List<Adiacenza> adiacenzeUsate = new ArrayList<Adiacenza>();
		peso=0;
		percorso = new ArrayList<Match>();
		cerca(partenza,destinazione,parziale,adiacenzeUsate);
		
		return percorso;
	}

	private void cerca(Match partenza, Match destinazione, List<Match> parziale, List<Adiacenza> adiacenzeUsate) {
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(peso>max) {
				max = peso;
				percorso = new ArrayList<Match>(parziale);
			} 
		} else {
			
			for(Match succ: Graphs.neighborListOf(this.grafo, partenza)) {
				Adiacenza a = new Adiacenza(partenza,succ,(int)this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, succ)));
				Adiacenza a2 = new Adiacenza(succ,partenza,(int)this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, succ)));
		        if(!adiacenzeUsate.contains(a) && !adiacenzeUsate.contains(a2) && !parziale.contains(succ)) {
		        	adiacenzeUsate.add(a);
		        	adiacenzeUsate.add(a2);
		        	peso+= a.getPeso();
		        	parziale.add(succ);
		        	cerca(succ,destinazione,parziale,adiacenzeUsate);
		        	
		        	peso-=a.getPeso();
		        	parziale.remove(succ);
		        	
		        }
			}
		}
		
	}
	
	public int getPeso() {
		return max;
	}
}
