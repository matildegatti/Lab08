package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private Graph<Airport,DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer,Airport> idMap;
	private Map<Integer,Airport> idMapAeroportiCollegati;
	
	public Model() {
		dao=new ExtFlightDelaysDAO();
		idMap=new HashMap<Integer,Airport>();
		idMapAeroportiCollegati=new HashMap<Integer,Airport>();
	}
	
	public void creaGrafo(double distanzaMinima) {
		this.dao.loadAllAirports(idMap);
		
		for(Flight f:this.dao.loadAllFlights()) {
			if(!this.idMapAeroportiCollegati.containsKey(f.getOriginAirportId()))
				this.idMapAeroportiCollegati.put(f.getOriginAirportId(), this.idMap.get(f.getOriginAirportId()));
		}
		
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, idMapAeroportiCollegati.values());
		
		for(Adiacenza a:dao.getAdiacenza(distanzaMinima)) { 
			Graphs.addEdge(this.grafo, idMapAeroportiCollegati.get(a.getId1()), idMapAeroportiCollegati.get(a.getId2()), a.getPeso());
		}
	}
	
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Graph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
}
