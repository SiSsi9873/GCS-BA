package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import GraphAlgorithm.Density;
import algorithm.BA;
import param.PS;
import readerWriter.reader;

public class main {

	static long t_start;
	static long t_end;

	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			String parameter = "-P E:/data/spatialSample/others/sample_intro/ -m 2 -r 200 -ALG BA -q (1)";
			run(parameter);
		}
	}

	public static void run(String input) throws IOException {
		run(input.split("\\s+"));
	}

	private static void run(String[] input) throws IOException {
		Pair<Set<String>, Set<String>> result = null;
		param.PS.getInstance().PSLoad(input);

		// read dataset
		reader.readData();


		t_start = System.nanoTime();


		if(PS.algName.equals("BA")) {
			result = BA.run();
		}

		t_end = System.nanoTime();

		System.out.println("algorithm running time = "+ (double)(t_end - t_start) / 1_000_000_000.0);
		System.out.println(result.getFirst());
		System.out.println(result.getSecond());
		System.out.println(Density.getDensity(result.getFirst(), result.getSecond()));
	}

					
}
