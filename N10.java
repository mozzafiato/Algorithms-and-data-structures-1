import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class Naloga10 {
	
	Map<Integer, Vozlisce> mesta;
	int stPovezav;
	int stVozlisc;
	int zac;
	int cilj;
	int[] sekvenca;
	int[] prev;
	int iprev;
	boolean konec;
	boolean spremembaH;
	
	public Naloga10(int M) {
		stPovezav=M;
		stVozlisc=0;
		mesta = new HashMap<Integer, Vozlisce>();
		prev = new int[10000];
		iprev=-1;
		konec=false;
		
		for(int i=0;i<10000;i++) {
			prev[i]=0;
		}
	}
	
	
	public void dodajPovezavo(int v1, int v2, int c) {
		
		Vozlisce a, b;
		
		if(mesta.containsKey(v1)==false) {
			 a = new Vozlisce(v1);
			 mesta.put(v1, a);
			 stVozlisc++;
		}
		else a = mesta.get(v1);
		
		if(mesta.containsKey(v2)==false) {
			b = new Vozlisce(v2);
			mesta.put(v2, b);
			stVozlisc++;
		}
		else b = mesta.get(v2);
		
		a.sosedi[a.stSosedov] = b;
		a.cena[a.stSosedov] = c;
		a.stSosedov++;
		
	}
	
	public void iteracija(BufferedWriter izhod) {
		
		int stObiskanihVozlisc = 0;
		int[] sekvenca = new int[stVozlisc];
		initVozlisca();
		Vozlisce zacetek = mesta.get(zac);
		sekvenca[0] = zacetek.id;
		int index=1;
		int ocenaMin = Integer.MAX_VALUE;
		int id = stVozlisc+1;
		Vozlisce naslednjo=null;
		Vozlisce v=null;
		spremembaH=false;
		boolean update=false;
		
		while(stObiskanihVozlisc<stVozlisc) {
			 stObiskanihVozlisc++;
			 
			 //System.out.println("***Zacnemo od vozlisce: "+zacetek.id);
			 id = stVozlisc+1;
			 ocenaMin = Integer.MAX_VALUE;
			 update=false;
			 
			 for(int i=0;i<zacetek.stSosedov;i++) {
				 
				 v = zacetek.sosedi[i];
				 
				 if(v.id!=cilj && v.stSosedov==0) {
					 v.v=Integer.MAX_VALUE;
					 v.visited=true;
					 sekvenca[index]=v.id;
					 index++;
					 izpisi(sekvenca,index,izhod);
					 init(sekvenca,prev,index);
					 iteracija(izhod);
					 return;
				 }
				 
				 //System.out.print("Sosed " + v.id + ":  ");
				 
				 if(v.visited==false) {
					 v.v = zacetek.cena[i] + v.h;
					 update=true;
					 if(v.v<ocenaMin) {
						 ocenaMin = v.v;
						 id=v.id;
						 naslednjo=v;
					 }
					 else if(v.v==ocenaMin) {
						 if(v.id < id) {
							 id = v.id;
							 naslednjo = v;
						 }
					 }
	
					 //System.out.println("ocenaMax: " + ocenaMax + " id: " + id);
				 }
			 }
			 
			 if(update==false) {
				 v.visited=true;
				 izpisi(sekvenca,index,izhod);
				 init(sekvenca,prev,index);
				 iteracija(izhod);
				 return;
			 }
			
			 if(zacetek.h<ocenaMin) {
				 zacetek.h = ocenaMin;
				 spremembaH=true;
			  }
			 
			 zacetek.visited = true;
			 //System.out.println("h("+zacetek.id+")=" + ocenaMin);
			 zacetek = naslednjo;
			 sekvenca[index] = naslednjo.id;
			 index++;
			 if(zacetek.id==cilj) break;
		}
		
		izpisi(sekvenca,index,izhod);
		init(sekvenca,prev,index);
		if(konec==false) {
			iteracija(izhod);
		}
	}
	
	public void dodaj(int z, int c) {
		zac=z;
		cilj=c;
	}
	
	public void init (int[] sekvenca, int[] prev, int n) {
		
	    if(n==iprev && spremembaH==false) {
	    	konec = enakost(sekvenca, prev,n);
	    }
		
		iprev=n;
		
		for(int i=0;i<n;i++) {
			prev[i] = sekvenca[i];
			sekvenca[i] = -1;
		}
		
	}
	
	public boolean enakost(int[] sekvenca, int[] prev, int n) {
		for(int i=0;i<n;i++) {
			if(sekvenca[i]!=prev[i]) return false;
		}
		return true;
	}
	
	public void initVozlisca() {
		for(int i=1;i<=stVozlisc;i++) {
			Vozlisce v = mesta.get(i);
			v.visited=false;
		}
	}
	
	public void izpisi(int[] sekvenca, int n, BufferedWriter izhod) {
		
		try {
			for(int i=0;i<n;i++) {
				
				if(i==n-1) izhod.write(sekvenca[i]+"");
				else izhod.write(sekvenca[i] + ",");
				//(i==n-1) System.out.print(sekvenca[i]);
				//else System.out.print(sekvenca[i] + ",");
			}
			izhod.newLine();
			System.out.println();
		} catch (IOException e) {
			System.out.println(e);
	    }
	}
	
	
	public class Vozlisce{
		int id;
		boolean visited;
		Vozlisce[] sosedi;
		int[] cena;
		int stSosedov;
		int v;
		int h;
		
		public Vozlisce(int id) {
			this.id = id;
			this.visited = false;
			this.sosedi = new Vozlisce[10000];
			this.cena = new int[10000];
			this.stSosedov=0;
			this.h=0;
			this.v=Integer.MAX_VALUE;
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
			
			Naloga10 obj = new Naloga10(stKorakov);
			
			FileOutputStream fos = new FileOutputStream(args[1]);
			BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(int i=0;i<stKorakov;i++) {
				vrstica = br.readLine();
				String[] niz = vrstica.split(",");
				int v1 = Integer.parseInt(niz[0]);
				int v2 = Integer.parseInt(niz[1]);
				int c = Integer.parseInt(niz[2]);
				obj.dodajPovezavo(v1, v2, c);
			}
			
			vrstica = br.readLine();
			String[] niz1 = vrstica.split(",");
			int zacetek = Integer.parseInt(niz1[0]);
			int cilj = Integer.parseInt(niz1[1]);
			obj.dodaj(zacetek, cilj);
			obj.iteracija(izhod);

			izhod.close();
			
		}catch (IOException e) {
			System.out.println(e);
		}

	}
}
