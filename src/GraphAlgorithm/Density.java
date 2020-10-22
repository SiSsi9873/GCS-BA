package GraphAlgorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;

import main.VAR;

public class Density {

	public static double getDensity(Set<String> uCC, Set<String> vCC) {
				
		Set<String> nodes = new HashSet<String>();
		nodes.addAll(uCC);
		nodes.addAll(vCC);
		
		Graph<String, DefaultEdge> g1 = new AsSubgraph<String, DefaultEdge>(VAR.C, nodes);
		
		return (double)g1.edgeSet().size() / nodes.size();
	}
}
