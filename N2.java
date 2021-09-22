
import java.io.*;

public class Naloga2 {

	public List first; 
	public List last;
	public int velikost;
	public int prazno;
	
	Naloga2(){
		first = new List(-1,null,-1,-1);
		last = null;
		velikost = 0;
		prazno = 0;
	}
	
	public void init(int size) {
		velikost = size; 
	}
	
	public boolean alloc(int size, int id) {
		
		//ce je id ze v strukturi 
		if(idJeNoter(id)==false) return false;
		
		//ali je seznam prazen
		if(last==null) {
			return dodajNaZacetku(size, id);
		}
		//seznam ima en element in zacne na 0
		if(last==first && first.next.zacetniIndex==0) {
			return dodajNaKoncu(size,id);
		}
		if(prazno<size) return dodajNaKoncu(size,id); 
		//iscemo prazni prostor
		if(vstaviVPraznem(size,id)) return true;
		//ce ga ni, dodamo na koncu
		else return dodajNaKoncu(size,id);
			
	}
	
	public boolean idJeNoter(int id) {
		List el = first;
		while(el!=null) {
			if(el.id == id) return false;
			el = el.next;
		}
		return true;
	}
	
	public int free(int id) {
		//prazen
		if(last==null) return 0;
		//en element
		int stevilo=0;
		if(last==first) {
			List a = first.next;
			if(a.id==id) {
				stevilo = a.size;
				first.next=null;
				last=null;
			}
			prazno+=stevilo;
			return stevilo;
		}
		
		List el, prev;
		el = first;
		prev = null;
		
		while(true) {
			if(el==null) return 0;
			if(el.next != null && el.next.id==id) break;
			prev = el;
			el = el.next;
			
		}
		if(el.next==null) return 0;
		stevilo = el.next.size;
		
		if (last == el.next) ///predzadnji
			last = el;
		else if (last == el) //zadnji
			last = prev;
			
		el.next = el.next.next;
		
		prazno+=stevilo;
		return stevilo;
		
	}
	
	public void defrag(int n) {
		if(prazno==0) return;
		for(int i=0;i<n;i++) {
			if(klic()==false) return;
		}
	}
	public boolean klic() {
		
		//ce seznam se ne zacne na 0 
		if(first.next.zacetniIndex!=0) {
			first.next.zacetniIndex = 0;
			first.next.koncniIndex = first.next.size-1;
			//ce je en element, nimamo vec praznega prostora
			if(first==last) prazno=0;
			return true;
		}
		
		List prev = first.next;
		List el = first.next.next;
		
		while(el!=null) {
			if(prev.koncniIndex!=el.zacetniIndex-1) {
				
				int razlika = el.zacetniIndex - prev.koncniIndex - 1;
				el.zacetniIndex = el.zacetniIndex - razlika;
				el.koncniIndex = el.koncniIndex - razlika;
				//ce pomeramo zadnega zmanjsujemo praznega prostora
				if(prev==last) prazno = prazno - razlika;
				return true;
			}
			prev=el;
			el=el.next;
	    }
		return false;
	}
	
	public void novdefrag(int n) {
		if(n==0) return;
		
		//ce seznam se ne zacne na index 0
		if(first.next.zacetniIndex!=0) {
			first.next.zacetniIndex = 0;
			first.next.koncniIndex = first.next.size-1;
			if(first==last) prazno=0;
			n--;
		}
		
		List prev, el;
		prev = first.next;;
		el = first.next.next;
		
		while(el!=null && n!=0) {
	
			if(prev.koncniIndex!=el.zacetniIndex-1) {
				int razlika = el.zacetniIndex - prev.koncniIndex - 1;
				el.zacetniIndex-=razlika;
				el.koncniIndex-=razlika;
				n--;
				if(prev==last) prazno = prazno - razlika;
			}
			prev=el;
			el = el.next;
		}
		
	}
	
	public boolean vstaviVPraznem(int size, int id) {
		
		List prev = first.next;
		
		//ce je zacetek prazen
		if(prev.zacetniIndex>=size) {
			dodajNaZacetku(size,id);
			return true;
		}
		
		List el = first.next.next;
		int prostor = 0;
		while(el!=null) {
			if(prev.koncniIndex!=el.zacetniIndex-1) {
			    prostor = el.zacetniIndex - prev.koncniIndex-1;
				if(prostor>=size) {
					prazno = prazno - size;
					dodajVmes(prev,el, size, id);
					return true;
				}
			}
			prev=el;
			el = el.next;
		}
			if(velikost - (last.next.koncniIndex+1)>=size) return dodajNaKoncu(size,id);
		
		return false;
	}
	
	public void dodajVmes(List prev, List after, int size, int id) {
		List nov = new List (id, after, prev.koncniIndex+1, size);
		nov.koncniIndex = nov.zacetniIndex + size-1;
		prev.next = nov;
		
		if(prev==last) {
			last = prev.next;
		}
		
	}
	
	public boolean dodajNaZacetku(int size, int id) {
		if(size>velikost) return false;
		
		List nov = new List (id, null, 0, size);
		nov.next = first.next;
		first.next = nov;
		
		if(last==first) last = nov; //en element
		else if(last==null) last = first; //prazen
		return true;
	}
	
	public boolean dodajNaKoncu(int size, int id) {
		List nov = new List(id, null, -1, size);
		
		//prazen seznam
		if (last == null)
		{
			if(size>velikost) return false;
			
			nov.zacetniIndex = 0;
			nov.koncniIndex = size-1;
			first.next = nov;
			last = first;
			return true;
		}
		
			List zadni = last.next;
			if(velikost-(zadni.koncniIndex+1)<size) return false;
			
			last.next.next = nov;
			last = last.next;
			nov.zacetniIndex = last.koncniIndex+1;
			nov.koncniIndex = nov.zacetniIndex + size-1; 
			return true;

	}
	
	public void napisiVDatoteki(BufferedWriter izhod) {
		List el = first.next;
		
		while(el!=null) {
			
			//System.out.println(el.id + "," + el.zacetniIndex + "," + el.koncniIndex);
			
			try {
			izhod.write(el.id + "," + el.zacetniIndex + "," + el.koncniIndex);
			izhod.newLine();
			}
			catch (IOException e) {
				System.out.println(e);
		    }
			
			el = el.next;
		}
	
	}
	
	public class List 
	{
		int id;
		List next;
		int zacetniIndex;
		int size; 
		int koncniIndex;
		
		List(int element, List nxt, int zacIndex, int velikost)
		{
			id = element;
			next = nxt;
			zacetniIndex = zacIndex;
			size = velikost;
			koncniIndex = zacetniIndex+size-1;
		}
	} //List
	
	
	//****MAIN****
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		BufferedReader br = null;
		String vrstica;
		try {
			br = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException fnfex) {
			System.out.println(fnfex.getMessage() + "The File was not found");
		}
		try {
			Naloga2 obj = new Naloga2();
			String korak = br.readLine();
			int stKorakov = Integer.parseInt(korak);
			
			for(int i=0;i<stKorakov;i++) {
				
				vrstica = br.readLine();
				
				String[] niz = vrstica.split(",");
				char ukaz = niz[0].charAt(0);
				String size, id, n;
				int size1,id1,n1;
				
				switch(ukaz) {
				case 'i':
				    size= niz[1];
				    size1 = Integer.parseInt(size);
					obj.init(size1);
		            break;
				case 'a':
				    size = niz[1];
				    size1 = Integer.parseInt(size);
					id = niz[2];
					id1 = Integer.parseInt(id);
					obj.alloc(size1, id1);
					break;
				case 'f':
					id = niz[1];
					id1 = Integer.parseInt(id);
					obj.free(id1);
					break;
				case 'd':
					n = niz[1];
					n1 =  Integer.parseInt(n);
					obj.novdefrag(n1);
					break;
				} //switch
			//obj.izpisiSeznam();
			//System.out.println();
		} //for
		
		FileOutputStream fos = new FileOutputStream(args[1]);
		BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
		
		obj.napisiVDatoteki(izhod);
		izhod.close();
		
		} catch (IOException e) {
			System.out.println(e);
		}
		
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime)*0.000000001 + " s"); 
		
	}
} //Naloga2
