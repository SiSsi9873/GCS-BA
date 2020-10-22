package main;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

public class VAR {
	public static Graph<String, DefaultEdge> U = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	public static Graph<String, DefaultEdge> V = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	public static Graph<String, DefaultEdge> C = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
}
