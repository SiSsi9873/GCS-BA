package algorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.scoring.Coreness;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import GraphAlgorithm.Density;
import main.VAR;
import param.PS;

public class BA {
	
	public static Pair<Set<String>, Set<String>> run() {
		
		computeCoreIndex();
		
		Set<Set<String>> UCCs = getConnectedAllComponents(VAR.U, VAR.C);
		Set<Set<String>> VCCs = getConnectedAllComponents(VAR.V, VAR.C);
		
		// preprocessing query nodes
		preprocessing(UCCs, PS.uQueryNodes);
		preprocessing(VCCs, PS.vQueryNodes);
		
		double maxDensity = -1;
		Pair<Set<String>, Set<String>> best = null;
		
		for(Set<String> UCC : UCCs) {
			for(Set <String> VCC : VCCs) {
				
				double density = Density.getDensity(UCC, VCC);
				if(density > maxDensity) {
					maxDensity = density;
					best = new Pair<Set<String>, Set<String>>(UCC, VCC);
				}
			}
		}
		return best;
	}

	private static void preprocessing(Set<Set<String>> uCCs, Set<String> Q) {

		Set<Set<String>> removeList = new HashSet<Set<String>>();
		
		if(Q.size() == 0) {
			return;
		}
		
		for(Set<String> cc : uCCs) {
			if(cc.containsAll(Q)) {
				// pass
			}
			else {
				removeList.add(cc);
			}
		}
		
		for(Set<String> cc : removeList) {
			uCCs.remove(cc);
		}
		
		
	}

	private static Set<Set<String>> getConnectedAllComponents(
			Graph<String, DefaultEdge> g, 
			Graph<String, DefaultEdge> T) {

		HashSet<String> keep = new HashSet<String>();
		HashSet<String> delete = new HashSet<String>();

		Coreness<String, DefaultEdge> cu = new Coreness<String, DefaultEdge>(g);
		Map<String, Integer> coreIndex = cu.getScores();

		for(Entry<String, Integer> entry : coreIndex.entrySet()) {
			if(entry.getValue() >= PS.m) {
				keep.add(entry.getKey());
			}
			else {
				delete.add(entry.getKey());
			}
		}

		for(String x : delete) {
			T.removeVertex(x);
			g.removeVertex(x);
		}

		Set<Set<String>> ret = new HashSet<Set<String>>();
		
		int ks = getKS(coreIndex);
		
		for(int i = PS.m; i <= ks; i++) {
			Set<String> nodesSet = getNodes(coreIndex, i);
			Graph<String, DefaultEdge> g1 = 
					new AsSubgraph<String, DefaultEdge>(g, nodesSet);
	        ConnectivityInspector<String, DefaultEdge> ci = new ConnectivityInspector<String, DefaultEdge>(g1);
	        for(Set<String> each : ci.connectedSets()) {
	        	ret.add(each);
	        }
		}
		
		return ret;
		
		
		
		
		
	}

	private static Set<String> getNodes(Map<String, Integer> coreIndex, int i) {
		Set<String> ret = new HashSet<String>();
		for(Entry<String, Integer> entry : coreIndex.entrySet()) {
			if(entry.getValue() >= i) {
				ret.add(entry.getKey());
			}
		}
		return ret;
	}

	private static int getKS(Map<String, Integer> coreIndex) {
		int max = 0;
		for(int x : coreIndex.values()) {
			if(x > max) {
				max = x;
			}
		}
		return max;
	}

	private static void computeCoreIndex() {
		
	}
}
