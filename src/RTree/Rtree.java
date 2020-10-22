package RTree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.decomposition.DulmageMendelsohnDecomposition;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.github.davidmoten.grumpy.core.Position;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import rx.Observable;
import rx.functions.Func1;
import param.PS;

public class Rtree {
	RTree<String, Point> tree;
	HashMap<String, Point> container;

	public Rtree(Graph<String, DefaultEdge> u2, 
			Map<String, Pair<Double, Double>> position) throws NumberFormatException, IOException {
		
		tree =  RTree.create();
		container = new HashMap<String, Point>();

		for(String u : u2.vertexSet()) {
			double lat = position.get(u).getFirst();
			double lng = position.get(u).getSecond();
			
			Point pointGPS = Geometries.pointGeographic(lng, lat);
			tree = tree.add(u, pointGPS);
			this.container.put(u, pointGPS);
		}
	}
	
	
	public HashSet<String> getLocationWithinRadiusR(String u, double maxDist){
		HashSet<String> ret = new HashSet<String>();
		List<Entry<String, Point>> list = null;
		double realDistanceThreshold = PS.r;
		try {
			list =  search(tree, this.container.get(u), realDistanceThreshold).toList().toBlocking().single();
		}
		catch (Exception e) {}
		
		if(list != null) {
			for(Entry<String, Point> element : list) {
				String v = element.value();
				if(!u.equals(v)) {
					ret.add(v);				
				}
			}
			return ret;
		}
		else {
			return null;
		}

	}


	public static <T> Observable<Entry<T, Point>> search(RTree<T, Point> tree, Point lonLat,
			final double distanceKm) {
		// First we need to calculate an enclosing lat long rectangle for this
		// distance then we refine on the exact distance
		final Position from = Position.create(lonLat.y(), lonLat.x());
		Rectangle bounds = createBounds(from, distanceKm);

		return tree
				// do the first search using the bounds
				.search(bounds)
				// refine using the exact distance
				.filter(new Func1<Entry<T, Point>, Boolean>() {
					public Boolean call(Entry<T, Point> entry) {
						Point p = entry.geometry();
						Position position = Position.create(p.y(), p.x());
						return from.getDistanceToKm(position) < distanceKm;
					}
				});
	}

	private static Rectangle createBounds(final Position from, final double distanceKm) {
		// this calculates a pretty accurate bounding box. Depending on the
		// performance you require you wouldn't have to be this accurate because
		// accuracy is enforced later
		Position north = from.predict(distanceKm, 0);
		Position south = from.predict(distanceKm, 180);
		Position east = from.predict(distanceKm, 90);
		Position west = from.predict(distanceKm, 270);

		return Geometries.rectangle(west.getLon(), south.getLat(), east.getLon(), north.getLat());
	}
}






