import java.util.*;
import java.io.*; 

public class Naloga7 {
	
	Map<Integer, Vozlisce> drevo;
	Vozlisce koren;
	Vozlisce temp;
	static int globalniX;
	
	public Naloga7() {
		drevo = new HashMap<>();
		koren = null;
		temp = null;
		globalniX=0;
	}
	
	public void dodajVozlisce(int ID, int v, int ID_l, int ID_d) {
		
		Vozlisce novo;
		//ostvarimo novo vozlisce
		if(drevo.containsKey(ID)==false) {
			novo = new Vozlisce();
			//dodamo v mapi
			drevo.put(ID, novo);
		} else {
			novo = drevo.get(ID);
		}
		
		novo.vrednost = v;
		Vozlisce levo = null;
		Vozlisce desno = null;
		
		//ali obstaja levi sin
		if(drevo.containsKey(ID_l)==false) {
			levo = new Vozlisce();
			drevo.put(ID_l, levo);
		} else {
			levo = drevo.get(ID_l);
		}
		
		//povezemo levega sina
		novo.levo = levo;
		levo.oce = novo;
		
		//ali obstaja desni sin
		if(drevo.containsKey(ID_d)==false) {
			desno = new Vozlisce();
			drevo.put(ID_d, desno);
		} else {
			desno = drevo.get(ID_d);
		}
		
		//povezemo desnega sina
		novo.desno = desno;
		desno.oce = novo;
		
		//update temp
		temp = novo;
	}
	
	public void poisciKoren() {
		poisciKoren(temp);
	}
	public void poisciKoren(Vozlisce temp) {
		if(temp.oce==null) koren = temp;
		else poisciKoren(temp.oce);
	}
	
	public void izpisSinov() {
		izpisSinov(koren);
	}
	
	public void izpisSinov(Vozlisce v) {
		if(v==null || v.vrednost==-1) return;
		System.out.println(v.vrednost+ " ima sinovi " + v.levo.vrednost + " i " + v.desno.vrednost);
		izpisSinov(v.levo);
		izpisSinov(v.desno);
	}
	public void izpisPoVrsti(BufferedWriter izhod) {
		izpisPoVrsti(koren, izhod);
	}
	
	public void izpisPoVrsti(Vozlisce v, BufferedWriter izhod) {
		if(v==null) return;
		
		Queue<Vozlisce> vozlisca = new LinkedList<Vozlisce>();
		vozlisca.add(v);
		
		int visina=0;
		
		while(true) {
			
			int stVozlisc = vozlisca.size();
			if(stVozlisc==0) break;
			
			
			while(stVozlisc > 0) 
	            { 
	                Vozlisce temp = vozlisca.peek(); 
	                //System.out.println(temp.vrednost + "," + temp.x + "," + visina);
	                try {
	                izhod.write(temp.vrednost + "," + temp.x + "," + visina);
	                izhod.newLine();
	                }
	                catch (IOException e) {
	    				System.out.println(e);
	    		    }
	    			
	                vozlisca.remove(); 
	                if(temp.levo.vrednost != -1) 
	                    vozlisca.add(temp.levo); 
	                if(temp.desno.vrednost != -1) 
	                    vozlisca.add(temp.desno); 
	                stVozlisc-=1; 
	            } 
	            visina++;
		}
		
	}
	public void izpisDrevesa() {
		izpisDrevesa(koren);
	}
	public void izpisDrevesa(Vozlisce v) {
		if(v==null) return;
		
		Queue<Vozlisce> vozlisca = new LinkedList<Vozlisce>();
		vozlisca.add(v);
		
		while(true) {
			
			int stVozlisc = vozlisca.size();
			if(stVozlisc==0) break;
			
			
			while(stVozlisc > 0) 
	            { 
	                Vozlisce temp = vozlisca.peek(); 
	                System.out.print(temp.vrednost + " "); 
	                vozlisca.remove(); 
	                if(temp.levo.vrednost != -1) 
	                    vozlisca.add(temp.levo); 
	                if(temp.desno.vrednost != -1) 
	                    vozlisca.add(temp.desno); 
	                stVozlisc-=1; 
	            } 
	       System.out.println();
		}
		
	}
	
	public void izpisiKoren() {
		System.out.println(koren.vrednost);
	}
	
	public void dolociXKoordinate() {
		dolociXKoordinate(koren);
	}
	
	public void dolociXKoordinate(Vozlisce v) {
		if(v==null) return;
		if(v.levo.vrednost!=-1)
		dolociXKoordinate(v.levo);
		v.x=globalniX;
		globalniX++;
		if(v.desno.vrednost!=-1)
		dolociXKoordinate(v.desno);
	}
	
	
	
	public void povecajXVseh(Vozlisce v) {
			if(v==null) return;
			if(jeLeviSin(v)) {
				v.x++;
				povecajXVseh(v.oce);
			}
			else {
				v.x++;
				globalniX=v.x+1;
				return;
			}
	}
	
	public boolean jeLeviSin(Vozlisce v) {
		if(v==koren) return false;
		Vozlisce oce = v.oce;
		if(oce.levo.vrednost == v.vrednost) return true;
		else return false;
	}
	
    class Vozlisce{
		int vrednost;
		Vozlisce levo;
		Vozlisce desno;
		Vozlisce oce;
		int x;
		
		public Vozlisce() {
			this.vrednost = -1;
			this.levo = null;
			this.desno = null;
			this.oce=null;
			this.x=-1;
		}
		public Vozlisce(int v, int l, int d) {
			this.vrednost = v;
			this.levo = new Vozlisce();
			this.levo.vrednost = l;
			this.desno = new Vozlisce();
			this.desno.vrednost = d;
			this.oce = null;
			this.x=-1;
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
			Naloga7 obj = new Naloga7();
			String korak = br.readLine();
			int stKorakov = Integer.parseInt(korak);
			
			for(int i=0;i<stKorakov;i++) {
				vrstica = br.readLine();
				String[] niz = vrstica.split(",");
				int ID = Integer.parseInt(niz[0]);
				int V = Integer.parseInt(niz[1]);
				int IDL = Integer.parseInt(niz[2]);
				int IDD = Integer.parseInt(niz[3]);
				obj.dodajVozlisce(ID,V,IDL,IDD);
			}
		obj.poisciKoren();
		//obj.izpisSinov();
		//obj.izpisDrevesa();
		obj.dolociXKoordinate();
		
		
		FileOutputStream fos = new FileOutputStream(args[1]);
		BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
		
		obj.izpisPoVrsti(izhod);
		izhod.close();
		
		
		} catch (IOException e) {
			System.out.println(e);
		}
	}//main

	
	
}//Naloga7
