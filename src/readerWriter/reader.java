package readerWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;

import RTree.Rtree;
import main.VAR;
import param.PS;

public class reader {

	public static Map<String, Pair<Double,Double>> position = new HashMap<String, Pair<Double, Double>>();
	
	public static void readData() throws IOException {
		read(VAR.U, PS.UName, VAR.C);
		readLocation(VAR.V, PS.VName, VAR.C);
		read(VAR.C, PS.EName, VAR.C);
	}

	private static void readLocation(Graph<String, DefaultEdge> g, String name, Graph<String, DefaultEdge> T) throws IOException {
		BufferedReader reader = null;
		String line;
		String[] token;
		reader = new BufferedReader(new FileReader(name));

		while((line = reader.readLine()) != null){
			token = line.split("\\s+");
			if(token.length != 0) {
				String n1 = token[0];
				g.addVertex(n1);
				T.addVertex(n1);
				position.put(n1, new Pair<Double, Double>(Double.parseDouble(token[1]), Double.parseDouble(token[2])));
			}
		}
		reader.close();
		
		setNeighbors(g);
		
		for(DefaultEdge e : g.edgeSet()){
			System.out.println(e);
		}
		
	}
	
	

	private static void setNeighbors(Graph<String, DefaultEdge> g) throws NumberFormatException, IOException {
		Rtree rt = new Rtree(g, position);

		for(String u : g.vertexSet()) {
			HashSet<String> closeNodes = rt.getLocationWithinRadiusR(u, PS.r);
			//System.out.println(i++);
			if(closeNodes != null) {
				for(String v : closeNodes) {
					g.addEdge(u, v);
				}
			}
		}
	}

	


	private static void read(Graph<String, DefaultEdge> g, String name, Graph<String, DefaultEdge> CC) throws IOException {
		BufferedReader reader = null;
		String line;
		String[] token;
		reader = new BufferedReader(new FileReader(name));

		while((line = reader.readLine()) != null){
			token = line.split("\\s+");
			if(token.length != 0) {
				String n1 = token[0];
				String n2 = token[1];
				g.addVertex(n1);
				g.addVertex(n2);
				g.addEdge(n1, n2);
				if(g != CC) {
					CC.addVertex(n1);
					CC.addVertex(n2);
				}
			}
		}
		reader.close();
	}

}
