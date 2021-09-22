import java.util.*;
import java.io.*; 
 
public class Naloga9 {
	
	Map<Integer, Set<Integer>> mapa;
	List<Set<Integer>> mnozica; 
	
	public Naloga9() {
		mapa = new HashMap<Integer, Set<Integer>>();
		mnozica = new ArrayList<Set<Integer>>();
	}
	
	public void dodajPrijatelja(int p1, int p2, BufferedWriter izhod) {
	
		if(mapa.containsKey(p1)==false && mapa.containsKey(p2)==false) {
			Set<Integer> set = new HashSet<Integer>();
			set.add(p1);
			set.add(p2); 
			mapa.put(p1, set);
			mapa.put(p2, set);
			//System.out.println("Novo drustvo: " + p1 + " i " + p2);
		}
		else if(mapa.containsKey(p1) && mapa.containsKey(p2)==false) {
			
			Set<Integer> s = new HashSet<Integer>();
			s = mapa.get(p1);
			s.add(p2);
			mapa.put(p2, s);
			//System.out.println(p2 + " ide u drustvo kude " + p1);
			
		}
		else if(mapa.containsKey(p1)==false && mapa.containsKey(p2)) {
		
			Set<Integer> s = new HashSet<Integer>();
			s = mapa.get(p2);
			s.add(p1);
			mapa.put(p1, s);
			//System.out.println(p1 + " ide u drustvo kude " +p2);
			
		}
		else{
			//ista mnozica ->isprintaj 
			if(mapa.get(p1).equals(mapa.get(p2))) {
				//System.out.println(p1 + "," + p2);
				
				try {
					izhod.write(p1 + "," + p2);
					izhod.newLine();	
				} catch (IOException e) {
    				System.out.println(e);
    		    }
			}
			else {
				//razlicna mnozica, napravi uniju, ne printaj!
				unija(mapa.get(p1), mapa.get(p2));
				//System.out.println("Spojuemo drustvo na " + p1 +" i " + p2);
			}
		}
	}
	
	public void unija(Set<Integer> s1, Set<Integer> s2) {
		//v s1 stavimo sve od s2
		//elemnti od s2 u mapu da se smeni mnozica
		Iterator<Integer> i = s2.iterator();
		
		while(i.hasNext()) {
			int element = i.next();
			mapa.remove(element);
			s1.add(element);
			mapa.put(element, s1);
		}
		mnozica.remove(s2);
	}
	public void test(int p1) {
		if(mapa.containsKey(p1)) System.out.println(p1 + " DA");
		else System.out.println(p1 + " NE");
	}
	
	public static void main(String[] args) {
		
		BufferedReader br = null;
		String vrstica;
		try {
			br = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException fnfex) {
			System.out.println(fnfex.getMessage() + "The File was not found");
		} try {
			Naloga9 obj = new Naloga9();
			String korak = br.readLine();
			int stKorakov = Integer.parseInt(korak);
			
			FileOutputStream fos = new FileOutputStream(args[1]);
			BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(int i=0;i<stKorakov;i++) {
				vrstica = br.readLine();
				String[] niz = vrstica.split(",");
				int p1 = Integer.parseInt(niz[0]);
				int p2 = Integer.parseInt(niz[1]);
				obj.dodajPrijatelja(p1,p2,izhod);
			}
			izhod.close();
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}
