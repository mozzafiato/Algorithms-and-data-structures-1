import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class Naloga6 {

	Map<Integer, Vozlisce> graf;
	int stMest;
	Vozlisce koren;
	
	public Naloga6(int m) {
		this.graf = new HashMap<>();
		this.stMest = m;
		this.koren = null;
	}
	
	public void dodajVozlisce(int id1, int id2, int visina) {
		
		Vozlisce a,b;
		
		if(graf.containsKey(id1)) {
			a = graf.get(id1);
		}
		else {
			a = new Vozlisce(id1, this.stMest);
			graf.put(id1, a);
		}
		if(graf.containsKey(id2)) {
			b = graf.get(id2);
		}
		else {
			b = new Vozlisce(id2,this.stMest);
			graf.put(id2, b);
		}
		
		a.sosedi[a.stSosedov]=b;
		a.visina[a.stSosedov]=visina;
		a.stSosedov++;
		
		b.sosedi[b.stSosedov]=a;
		b.visina[b.stSosedov]=visina;
		b.stSosedov++;
		
		if(koren==null) this.koren = a;
	}
	
	public void najdiPot(int a, int b, int c, BufferedWriter izhod) {
		Vozlisce start = graf.get(a);
		Vozlisce finish = graf.get(b);
		int rezultat =  najdiPot(start, finish, c);
		
		try {
			izhod.write(rezultat+"");
		}catch (IOException e) {
			System.out.println(e);
	    }
	}
	public int najdiPot(Vozlisce start, Vozlisce finish, int visina) {
		if(start.vrednost == finish.vrednost) return 1;
		
		else {
			start.visited = true;
			int sum=0;
			for(int i=0;i<start.stSosedov;i++) {
				if(start.sosedi[i].visited==false && predora(start.visina[i], visina)) {
					sum+=najdiPot(start.sosedi[i],finish, visina);
					start.sosedi[i].visited = false;
				}
			}
			return sum;
		}
	}
	public boolean predora(int a, int b) {
		if(a>=b || a==-1) return true;
		else return false;
	}
	
	public void izpisiGraf() {
		izpisiGraf(koren);
	}
	public void izpisiGraf(Vozlisce v) {
		
		if(v.visited==false) {
			System.out.println(v.vrednost);
			v.visited=true;
		}
		else return;
		
		for(int i=0;i<v.stSosedov;i++) {
			izpisiGraf(v.sosedi[i]);
		}
	    
	}
	public void izpisiSosede(int a) {
		Vozlisce v = graf.get(a);
		System.out.println("Sosedi vozlisca " + v.vrednost);
		for(int i=0;i<v.stSosedov;i++) {
			System.out.print(v.sosedi[i].vrednost + " ");
		}
		System.out.println();
	}
	
	
	public class Vozlisce{
		int vrednost;
		int stSosedov;
		Vozlisce[] sosedi;
		int[] visina;
		boolean visited;
		
		public Vozlisce(int v, int m) {
			this.vrednost = v;
			this.stSosedov=0;
			this.sosedi = new Vozlisce[m];
			this.visina = new int[m];
			visited = false;
		}
	}
	public static void main(String[] args) {
		
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
			
			Naloga6 obj = new Naloga6(stKorakov);
			
			for(int i=0;i<stKorakov;i++) {
				vrstica = br.readLine();
				String[] niz = vrstica.split(",");
				int id1 = Integer.parseInt(niz[0]);
				int id2 = Integer.parseInt(niz[1]);
				int vis = Integer.parseInt(niz[2]);
				obj.dodajVozlisce(id1, id2, vis);
			}
			
			vrstica = br.readLine();
			String[] niz2 = vrstica.split(",");
			int start = Integer.parseInt(niz2[0]);
			int finish = Integer.parseInt(niz2[1]);
			vrstica = br.readLine();
			int visina = Integer.parseInt(vrstica);
			
			FileOutputStream fos = new FileOutputStream(args[1]);
			BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
			
		    obj.najdiPot(start, finish, visina,izhod);
			izhod.close();
		}catch (IOException e) {
			System.out.println(e);
		}
		
	}

}
