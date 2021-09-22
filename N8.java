import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class Naloga8 {
	
	Map<Integer, Povezava> povezave;
	Map<Integer, Vozlisce> mesta;
	int stMest;
	int stPovezav;
	int stOznacenihPovezav;
	int doseg;
	Povezava first;
	Povezava last;
	Map<Integer, Set<Integer>> vsebovani;
	List<Set<Integer>> mnozica; 
	int stMnozic;
	boolean rezultat = false;
	
	public Naloga8(int m) {
		this.povezave = new HashMap<Integer, Povezava>();
		this.mesta = new HashMap<Integer, Vozlisce>();
		this.stMest=0;
		this.stPovezav=m;
		this.first = null;
		this.last = null;
		this.vsebovani = new HashMap<Integer, Set<Integer>>();
		this.mnozica = new ArrayList<Set<Integer>>();
		this.stMnozic = 0;
		this.stOznacenihPovezav=0;
	}
	
	public void dodajPovezavo(int id, int m1, int m2, int razdalja) {
		
		Vozlisce a, b;
		
		//stejemo mesta
		if(mesta.containsKey(m1)==false) {
		    a = new Vozlisce(m1);
			mesta.put(m1, a);
			//System.out.println("Dodali smo mesto: " + m1);
			this.stMest++;
		}
		else a = mesta.get(m1);
		
		if(mesta.containsKey(m2)==false) {
			b = new Vozlisce(m2);
			mesta.put(m2, b);
			//System.out.println("Dodali smo mesto: " + m2);
			this.stMest++;
		}
		else b = mesta.get(m2);
		
		//dodamo povezavo v map
		if(povezave.containsKey(id)==false) {
			Povezava p = new Povezava(id,a,b,razdalja);
			povezave.put(id, p);
			
			//ustrezno povezavo dodamo v urejen seznam
			dodajPovezavoVSeznam(p);
		}
		
	}
	
	public void dodajPovezavoVSeznam(Povezava p) {
		//prazen
		if(first==null) {
			first = p;
			last = p;
		}
		//dodamo na zacetku
		else if(p.km <= first.km) {
			
			p.naslednja = first; 
			first = p;
			if(last==null) last = first.naslednja;
		} 
		//dodamo na koncu
		else if(p.km>=last.km){
			last.naslednja = p;
			last = p;
		}
		else {
		    //dodamo vmes
			Povezava e = first, prev = first;
			while(e!=null) {
				if(e.km>p.km) break;
				prev = e;
				e = e.naslednja;
			}
			prev.naslednja = p;
			p.naslednja = e;
		}
	}
	
	public void oznaciPovezave() {
		Povezava e = first;
		int stOznacenihMest = 0;
		//System.out.println("stOznacenihMest "+ stOznacenihMest);
		while(e!=null) {
			//vstavitev
			if(stOznacenihMest==stMest && stMnozic==1) {
				rezultat = true;
				break;
			}
			if(e.km<=doseg) {
				//ce je vsaj edno vozlisce false, oznacimo povezavo
				if(e.v1.visited==false && e.v2.visited) {
					e.v1.visited=true;
					e.oznaka = true;
					stOznacenihPovezav++;
					stOznacenihMest++;
					
					Set<Integer> s = vsebovani.get(e.v2.idVozlisca);
					vsebovani.put(e.v1.idVozlisca,s);
					
					//System.out.println("Dodamo " + e.v1.idVozlisca);
				}
				else if(e.v1.visited && e.v2.visited==false) {
					e.v2.visited=true;
					e.oznaka = true;
					stOznacenihMest++;
					stOznacenihPovezav++;
					Set<Integer> s = vsebovani.get(e.v1.idVozlisca);
					vsebovani.put(e.v2.idVozlisca,s);
					
					//System.out.println("Dodamo " + e.v2.idVozlisca);
				}
				//nova povezava ->nov graf
				else if(e.v1.visited==false && e.v2.visited==false) {
					e.v1.visited=true;
					e.v2.visited=true;
					e.oznaka=true;
					stOznacenihMest+=2;
					stOznacenihPovezav++;
					stMnozic++;
					Set<Integer> s = new HashSet<Integer>();
					s.add(e.v1.idVozlisca);
					s.add(e.v2.idVozlisca);
					vsebovani.put(e.v1.idVozlisca, s);
					vsebovani.put(e.v2.idVozlisca, s);
					mnozica.add(s);
					
					//System.out.println("Dodamo " + e.v1.idVozlisca + " " + e.v2.idVozlisca);
				} 
				else if(e.v1.visited && e.v2.visited && stMnozic>1) {
					//System.out.println("Stevilo mnozic je: " + stMnozic);
					 //ce ne se nahajata u isti mnozici, naredimo unijo
					if(vsebovani.get(e.v1.idVozlisca).equals(vsebovani.get(e.v2.idVozlisca))==false){
					    stMnozic--;
					    stOznacenihPovezav++;
						e.oznaka = true;
						Set<Integer> s1 = vsebovani.get(e.v1.idVozlisca);
						Set<Integer> s2 = vsebovani.get(e.v2.idVozlisca);
						unija(s1,s2);
						
						}
					//System.out.println("Povezemo s " + e.id);
				}
			  }
			e = e.naslednja;
		} //while
	}
	
	public void unija(Set<Integer> s1, Set<Integer> s2) {
	
		Iterator<Integer> i = s2.iterator();
		
		while(i.hasNext()) {
			int element = i.next();
			vsebovani.remove(element);
			s1.add(element);
			vsebovani.put(element, s1);
		}
		mnozica.remove(s2);
	}
	
	
	
	public void izpisiSeznam() {
		Povezava e = first;
		while(e!=null) {
			System.out.println(e.id + " " + e.km);
			e = e.naslednja;
		}
	}
	public void izpisiRez(BufferedWriter izhod) {
		
		if(rezultat==false) {
			System.out.println(-1);
			return;
		}
		try {
			
			int j=0;
			for(int i=1;i<=stPovezav;i++) {
				Povezava e = povezave.get(i);
				if(e.oznaka) {
					j++;
					if(j==stOznacenihPovezav) {
						izhod.write(e.id + "");
						//System.out.print(e.id);
					}
					else {
						izhod.write(e.id + ",");
						//System.out.print(e.id + ",");
					}	
				}
			}
	
		} catch (IOException e) {
			System.out.println(e);
	    }
		
		System.out.println();
	}
	
	public void stMest() {
		System.out.println("Stevilo mest: "+this.stMest);
	}
	public void dodajDoseg(int d) {
		this.doseg = d;
	}
	
	public class Povezava{
		int id;
		Vozlisce v1;
		Vozlisce v2;
		int km;
		boolean oznaka;
		Povezava naslednja;
		
		public Povezava(int id, Vozlisce v1, Vozlisce v2, int km) {
			this.id = id;
			this.v1 = v1;
			this.v2 = v2;
			this.km = km;
			this.oznaka = false;
			this.naslednja = null;
		}
		
	}
	public class Vozlisce{
		int idVozlisca;
		boolean visited;
		
		public Vozlisce(int id) {
			this.idVozlisca = id;
			this.visited = false;
		}
	}
	
	
	public static void main(String[] args) {

		//long startTime = System.nanoTime();
		
		BufferedReader br = null;
		String vrstica;
		try {
			br = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException fnfex) {
			System.out.println(fnfex.getMessage() + "The File was not found");
		}
		try {
			String korak = br.readLine();
			int stKorakov = Integer.parseInt(korak);
			
			Naloga8 obj = new Naloga8(stKorakov);
			
			for(int i=0;i<stKorakov;i++) {
				vrstica = br.readLine();
				String[] niz = vrstica.split(",");
				int id = Integer.parseInt(niz[0]);
				int m1 = Integer.parseInt(niz[1]);
				int m2 = Integer.parseInt(niz[2]);
				int km = Integer.parseInt(niz[3]);
				obj.dodajPovezavo(id, m1, m2, km);
			}
			vrstica = br.readLine();
			int doseg = Integer.parseInt(vrstica);
			obj.dodajDoseg(doseg);
			obj.oznaciPovezave();
			//obj.izpisiSeznam();
			
			FileOutputStream fos = new FileOutputStream(args[1]);
			BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
			
			obj.izpisiRez(izhod);
			
			izhod.close();
			
			}catch (IOException e) {
				System.out.println(e);
			}
		
		
		//long endTime = System.nanoTime();
		//System.out.println("Took "+(endTime - startTime)*0.000000001 + " s"); 
	}
}
