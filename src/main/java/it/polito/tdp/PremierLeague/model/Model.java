package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
}
