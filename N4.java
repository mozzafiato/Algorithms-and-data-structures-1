import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Naloga4 {

		Node first;
		Node last;
		int velikostNode;
		int stNode;
		int stSkupnihElementov;
		
		public Naloga4() {
			first = new Node(0);
			last = null;
			velikostNode = 5;
			stNode=0;
			stSkupnihElementov=0;
		}
		
		public void init(int N) {
			velikostNode = N;
		}
		
		public boolean insert(int v, int p) {
			//neveljavna pozicija
			if(p<0 || p>stSkupnihElementov) return false; 
			//prazen linkedlist
			if(last==null) {
				Node nov = new Node(velikostNode);
				first.next = nov;
				last = first;
				nov.tabela[0] = v;
				nov.stElementov++;
				stNode++;
				stSkupnihElementov++;
				return true;
			}
		
			//iscemo ciljni clen
			int stevec = 0;
			Node el = first.next;
			while(el!=null) {
				stevec+=el.stElementov;
				if(stevec>=p) {
					//posebni primer kadar je el poln da ne bi nov element vstavil v njega, namesto v naslednji node
					if(stevec==p && el.stElementov==velikostNode && el.next!=null) {
						el=el.next;
						stevec+=el.stElementov;
						break;
					}
					else break;
				}
				el = el.next;
			}
			
			//NIMAMO PROSTORA
			if(el.stElementov>=el.velikostTabele) {
				 Node drugi = new Node(velikostNode);
				 drugi.next = el.next;
				 el.next = drugi;
				 stNode++;
				 if(el==last) last = drugi;
				 if(last.next==el) last = el;
				
				 stevec-=el.stElementov; //vrnemo stevca za en Node nazaj
				 
				 //v drugi vstavimo n/2 do stElementov-1
				 //v el tiste izbrisemo
				 for(int i=velikostNode/2;i<el.stElementov;i++) {
					 drugi.tabela[i-velikostNode/2] = el.tabela[i];
					 el.tabela[i]=-1;
				 }
				 drugi.stElementov = el.stElementov - velikostNode/2;
				 el.stElementov = velikostNode/2;
				 
				 //preverjamo kje pripada p (el ali drugi)
				 int temp = el.stElementov;
				 if(stevec + el.stElementov<p) {
					 stevec= stevec + el.stElementov + drugi.stElementov;
					 el = drugi;
				 }
				 else stevec+=temp;
			}
				//VSTAVIMO V PRAZNI PROSTOR
				int indexPozicije = p - (stevec-el.stElementov);
				//System.out.println("index Pozicije za " + p + " je " + indexPozicije);
				//ce ne moramo premaknuti elemente v desno
				if(el.stElementov<=indexPozicije) {
					el.tabela[indexPozicije] = v;
					el.stElementov++;
					stSkupnihElementov++;
					return true;
				}
				//premaknemo elemente v desno
				else {
					 for(int i=el.stElementov;i>indexPozicije;i--) {
						 el.tabela[i] = el.tabela[i-1];
					 }
					 el.tabela[indexPozicije] = v;
					 el.stElementov++;
					 stSkupnihElementov++;
					 return true;
				}
				
		}
		public boolean remove(int p) {
			if(p<0 || p>stSkupnihElementov) return false;
			
			//iscemo ciljni clen
			int stevec = 0;
			Node prev = first;
			Node el = first.next;
			while(el!=null) {
				stevec+=el.stElementov;
				if(stevec>p) break;
				prev = el;
				el = el.next;
			}
			
			int indexPozicije = p - (stevec - el.stElementov);
			
			//*izbrisemo element
			if(indexPozicije<el.stElementov-1) {
				//premaknemo v levo
				for(int i=indexPozicije;i<el.stElementov-1;i++) {
					el.tabela[i] = el.tabela[i+1];
				}
				el.tabela[el.stElementov-1] = -1;
			}
			else el.tabela[indexPozicije] = -1;
			
			el.stElementov --;
			stSkupnihElementov--;
			
			//ce je el zadnji ne moremo od naslednjega dodati ker je null, zato tukaj koncujemo:
			if(el.next==null) return true;
			
			//ce moramo dodati od el.next v el
			if(el.stElementov<velikostNode/2) {
				Node naslednji = el.next;
				
				//ce dodamo samo en clen
				if(naslednji.stElementov-1 >= velikostNode/2) {
					el.tabela[el.stElementov] = naslednji.tabela[0];
					el.stElementov++;
						//premaknemo elemente od el.next v levo
						for(int i=0;i<naslednji.stElementov-1;i++) {
							naslednji.tabela[i] = naslednji.tabela[i+1];
						}
						naslednji.tabela[naslednji.stElementov-1] = -1;
						naslednji.stElementov--;
					    return true;
				} //ce dodamo vse ostale od el.next in ga izbrisemo
				else {
					//premestimo vse elemente iz naslednji v el
					for(int i=0;i<naslednji.stElementov;i++) {
						el.tabela[el.stElementov+i] = naslednji.tabela[i];
					}
					el.stElementov += naslednji.stElementov;
					//pomestimo last
					if(last == naslednji) last = el;
					else if(last==el) last = prev;
					//izbrisemo
				    el.next = el.next.next;
				    stNode--;
				    return true;
				}
			}
			else return true;
			
		}
		public void izpisiSeznam() {
			System.out.println(stNode);
			
			Node el = first.next;
			while(el!=null) {
				for(int i=0;i<velikostNode;i++) {
					
					if(i<el.stElementov)
					{
						System.out.print(el.tabela[i]);
					}
					
					else {
						System.out.print("NULL");
					}
					
					if(i<velikostNode-1) {
						System.out.print(",");
					}
				}
				System.out.println();
				el = el.next;
			}
			System.out.println();
		}
		
		public void napisiVDatoteki(BufferedWriter izhod) {
			
			try {
				//System.out.println(stNode);
				izhod.write(stNode+"");
				izhod.newLine();
				
				Node el = first.next;
				while(el!=null) {
					for(int i=0;i<velikostNode;i++) {
						
						if(i<el.stElementov)
						{
							//System.out.print(el.tabela[i]);
							izhod.write(el.tabela[i]+"");
						}
						
						else {
							//System.out.print("NULL");
							izhod.write("NULL");
						}
						
						if(i<velikostNode-1) {
							//System.out.print(",");
							izhod.write(",");
						}
					}
					//System.out.println();
					izhod.newLine();
					el = el.next;
				}
				System.out.println();
			}
			catch (IOException e) {
				System.out.println(e);
			}
		}
		
	
	public class Node{
		Node next;
		int stElementov;
		int velikostTabele;
		int tabela[];
		
		public Node(int n) {
			next = null;
			stElementov = 0;
			velikostTabele = n;
			tabela = new int[n];
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
			Naloga4 obj = new Naloga4();
			String korak = br.readLine();
			int stKorakov =  Integer.parseInt(korak);
		
			for(int i=0;i<stKorakov;i++) {
			
				vrstica = br.readLine();
				
				String[] niz = vrstica.split(",");
				char ukaz = niz[0].charAt(0);
				String v, p;
				int v1, p1;
				switch(ukaz) {
				case 's':
				    v = niz[1];
				    v1 = Integer.parseInt(v);
					obj.init(v1);
		            break;
				case 'i':
				    v = niz[1];
				    v1 = Integer.parseInt(v);
					p = niz[2];
					p1 = Integer.parseInt(p);
					obj.insert(v1, p1);
					break;
				case 'r':
					p = niz[1];
					p1 = Integer.parseInt(p);
					obj.remove(p1);
					break;
				} //switch
				
				//obj.izpisiSeznam();
				
		} //for
		
		FileOutputStream fos = new FileOutputStream(args[1]);
		BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
		
		obj.napisiVDatoteki(izhod);
		izhod.close();
		
		} catch (IOException e) {
			System.out.println(e);
		}
		//long endTime = System.nanoTime();
		//System.out.println("Took "+(endTime - startTime) + " ns"); 
		
	}//end main
}//end Naloga4
