package param;

import java.util.HashSet;
import java.util.Set;

public class PS {

	public static String algName;

	public static String UName;
	public static String VName;
	public static String EName;
	public static String outputFileName;

	public static int m;
	public static double r;

	public static Set<String> uQueryNodes = new HashSet<String>();
	public static Set<String> vQueryNodes = new HashSet<String>();

	private static PS ps = new PS() ;
	private PS() {	}
	public static PS getInstance() {
		return ps;
	}

	public  void PSLoad(String[] args) {


		for(int i = 0; i < args.length; i++) {
			String current = args[i];
			if(current.equals("-m") || current.equals("-m")) {
				m = Integer.parseInt(args[i+1]);
				break;
			}
		}
		
		for(int i = 0; i < args.length; i++) {
			String current = args[i];
			if(current.equals("-r") || current.equals("-r")) {
				r = Integer.parseInt(args[i+1]);
				break;
			}
		}


		for(int i = 0; i < args.length; i++) {
			String current = args[i];
			if(current.equals("-ALG") || current.equals("-alg")) {
				algName = args[i+1];
				break;
			}
		}
		
		for(int i = 0; i < args.length; i++) {
			String current = args[i];
			if(current.equals("-q") || current.equals("-Q")) {
				String q = args[i+1];
				q = q.replace("(", "");
				q = q.replace(")", "");
				String[] tokens = q.split(",");
				for(String token : tokens) {
					token = token.trim();
					if(token.contains("l")) {
						vQueryNodes.add(token);
					}
					else {
						uQueryNodes.add(token);
					}
				}
				break;
			}
		}
		
		for(int i = 0; i < args.length; i++) {
			String current = args[i];
			if(current.equals("-P") || current.equals("-p") ) {
				UName = args[i+1]+"/network.dat";
				VName = args[i+1]+"/location.dat";
				EName = args[i+1]+"/checkin.dat";

				outputFileName = args[i+1]+"/"+algName+"_m_"+m+"_r_"+r;
				outputFileName += ".dat";

				break;
			}
		}




		System.out.println("======================================");
		System.out.println("=============Parameter list================");
		System.out.println("======================================");
		System.out.println("U entity file name \t\t= "+UName);
		System.out.println("V entity file name \t\t= "+VName);
		System.out.println("E entity file name \t\t= "+EName);
		System.out.println("m \t\t= "+m);
		System.out.println("r \t\t= "+r);
		System.out.println("======================================");
	}

}
