import java.io.*;

public class Naloga3 {
	
	public List first;
	public List last;
	public List zacetek; //po List zacetek se nahaja prazni prostor
	public int zacetekIndex; //da bi vedli od kateri id naprej v tabeli id da se sprehajamo ko iscemo vsoto
	public int stElementov;
	public int[] id;
	public int[] idSize;
	public int[] rezultat;
	
	public Naloga3() {
		first = new List(0, null, -1, -1);
		last = null;
		zacetek = first.next;
		stElementov = 0;
		zacetekIndex = 1;
	}
	public void dodajNaKoncu(int id, int zacIndex, int konIndex) {
		
		List nov = new List (id, null, zacIndex, konIndex);
		if(last==null) {
			last=first;
			first.next = nov;
			stElementov++;
			return;
		}
		last.next.next = nov;
		last = last.next;
		stElementov++;
	}
	public void napolniTabele() {
		id = new int[stElementov];
		idSize = new int[stElementov];
		rezultat = new int[stElementov];
		int i = 0;
		List el = first.next;
		while(el!=null) {
			id[i] = (int)el.id;
			idSize[i] = el.size;
			el = el.next;
			i++;
		}
	}
	
	public void poisciZacetek() {
		List prev, el;
		
		if(first==last || last==null) return;
		prev = first;
		el = prev.next;
		while(el!=null) {
			if(prev.koncniIndex !=(el.zacetniIndex-1)) {
				zacetek = prev;
				return;
			}
			zacetekIndex++;
			prev = el;
			el = el.next;
		}
		
	}
	public void izbrisi(Object id){
		//en element
		if(last==first) {
		List a = first.next;
		if(a.id==id) {
			first.next=null;
			last=null;
			}
			return;
		}
				
		List el, prev;
		el = first;
		prev = null;
		
		while(true) {
			if(el==null) return;
				if(el.next != null && el.next.id==id) break;
					prev = el;
					el = el.next;
					
				}
			if(el.next==null) return;
				if (last == el.next) ///predzadnji
					last = el;
				else if (last == el) //zadnji
					last = prev;
					
				el.next = el.next.next;
				
	}
	
	public List naslednjiZacetek(List el) { //el je trenutni zacetek
		if(el.next==null) return null;
		List a = el;
		List b = el.next;
		while(b!=null) {
			if(a.koncniIndex != b.zacetniIndex-1) {
				zacetek = a;
				return a;
			}
			zacetekIndex++;
			a = b;
			b = b.next;
		}
	  return null;
	}
	
	public void vrniTabelo(int[] rez) {
		rezultat = rez;
		//for(int i=0;i<rezultat.length;i++) {
			//System.out.print(rezultat[i]+ " ");
		//}
		//System.out.println();
	}
	
	public boolean najdiVsoto(int[] tabelaid, int[] tabelasize, int vsota,int i, int[] rez, int rezi) {
		if(vsota==0) {
			vrniTabelo(rez);
			return true;
		}
		if(vsota<0 || i>=stElementov) return false;
		
		if(tabelasize[i]>vsota)  return najdiVsoto(tabelaid, tabelasize, vsota, i+1, rez, rezi);
		else {
			if (najdiVsoto(tabelaid, tabelasize, vsota, i+1, rez, rezi)) return true;
			
			rez[rezi] = tabelaid[i]; //vo rez go socuvuva id-to
			if (najdiVsoto(tabelaid, tabelasize, vsota-tabelasize[i], i+1, rez,rezi+1)) {
				return true;
			}
			rez[rezi] = 0;
			return false;
		}
		
	}
	
	
	public void funkcija(BufferedWriter izhod) { 
		//ko najde prazni prostor mora poiskati nabor blokov katerim je vsota size enaka praznemu prostoru
		
		if(zacetek == null || zacetek.next==null) return;
		
		int prazniProstor = zacetek.next.zacetniIndex - zacetek.koncniIndex - 1;
		//System.out.println("prazni prostor je: "+prazniProstor);
		//System.out.println("zacetekIndex je: " + zacetekIndex + " st.elementov: " + stElementov);
		int[] rez = new int[stElementov];
		String textZaIzhod = "";
		
		//ce ne najdemo vsoto ki je enaka praznemu prostoru
		if(najdiVsoto(id, idSize, prazniProstor, zacetekIndex, rez, 0)==false) { 
			List target = zacetek.next;
			//System.out.println(target.id + "," + (zacetek.koncniIndex+1));
			textZaIzhod = target.id + "," + (zacetek.koncniIndex+1);
			napisiVDatoteki(textZaIzhod, izhod);
			
			target.zacetniIndex = zacetek.koncniIndex + 1;
			target.koncniIndex = target.zacetniIndex + target.size  - 1;
			izbrisiIzTabele((int)target.id);
			//premaknemo brez brisanja
		}
		//najdemo nabor listov ki so enaki praznemu prostoru
		else {
			 //izpisemo in izbrisemo id-je
			List el = first.next;
			int trenutniIndex = zacetek.koncniIndex+1; //index kjer treba da stavimo blok
			//System.out.println("trenutni index je " + trenutniIndex);
			int i=0;
			while(el!=null) {
				if(i==rezultat.length) break;
				 if((int)el.id==rezultat[i]) {
					 //System.out.println(rezultat[i] + "," + trenutniIndex);
					 textZaIzhod = rezultat[i] + "," + trenutniIndex;
					 napisiVDatoteki(textZaIzhod, izhod);
					 trenutniIndex+=el.size;
					 izbrisi(el.id);
					 i++;
				 }
				 else el = el.next;
			 }
			//izbrisemo id-je tud iz tabele id in idSize
			for(int k=0;k<rezultat.length;k++) {
				izbrisiIzTabele(rezultat[k]);
			}
		}
		zacetek = naslednjiZacetek(zacetek.next);
		
		funkcija(izhod);
	}
	
	public void napisiVDatoteki(String textZaIzhod, BufferedWriter izhod) {
		     
		try {
			izhod.write(textZaIzhod);
			izhod.newLine();
			} 
			 catch (IOException e) {
					System.out.println(e);
			}
	}
	
	
	public void izbrisiIzTabele(int element) {
		if(element== 0) return;
		int i=0;
		for(i=0;i<stElementov;i++) {
			if(id[i]==element) break;
			
		}
		if(i==stElementov-1) {
			id[i]=0;
			idSize[i]=0;
			stElementov--;
			return;
		}
		else {
			for(int j=i;j<stElementov-1;j++) {
				id[j] = id[j+1];
				idSize[j] = idSize[j+1];
			}
			id[stElementov-1] = 0;
			idSize[stElementov-1] = 0;
			stElementov--;
		}
		
	}
	
	
	public void izpisiSeznam() {
		List el = first.next;
		
		while(el!=null) {
			System.out.println(el.id + "," + el.zacetniIndex + "," + el.koncniIndex);
			el = el.next;
		}
	
	}
	public void izpisiZacetek() {
		if(zacetek==null) {
			System.out.println("praznega prostora ni");
			return;
		}
		System.out.println(zacetek.id + " " + zacetek.zacetniIndex + " " + zacetek.koncniIndex);
	}
	
	public class List 
	{
		Object id;
		List next;
		int zacetniIndex;
		int size; 
		int koncniIndex;
		
		List(int element, List nxt, int zacIndex, int konIndex)
		{
			id = element;
			next = nxt;
			zacetniIndex = zacIndex;
			koncniIndex = konIndex;
			size = konIndex - zacIndex + 1;
		}
	} //List
	
	public static void main(String[] args) {
		BufferedReader br = null;
		String vrstica;
		try {
			br = new BufferedReader(new FileReader(args[0]));
			
		} catch (FileNotFoundException fnfex) {
			System.out.println(fnfex.getMessage() + "The File was not found");
		}
		try {
			Naloga3 obj = new Naloga3();
	
			while((vrstica = br.readLine()) != null) {
				String[] niz = vrstica.split(",");
				int id, zacIndex, konIndex; 
				id = Integer.parseInt(niz[0]);
				zacIndex = Integer.parseInt(niz[1]);
				konIndex = Integer.parseInt(niz[2]);
				obj.dodajNaKoncu(id, zacIndex, konIndex);
			} //switch
		
			obj.napolniTabele();
			obj.poisciZacetek();
			//obj.izpisiSeznam();
			//obj.izpisiZacetek();
			
			FileOutputStream fos = new FileOutputStream(args[1]);
			BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
				
			obj.funkcija(izhod);
			
			izhod.close();
			
		} //while
		 catch (IOException e) {
			System.out.println(e);
		}
		
		
	}

}
