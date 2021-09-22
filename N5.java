import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Naloga5 {
	
	Node first;
	Node last;
	int mClenov; //st clenov
	int stClenov; //st narejenih clenov
	int sizeClena; //maks velkost clena
	
	public Naloga5() {
		first = new Node();
		last = null;
		mClenov = 0;
		stClenov = 0;
		sizeClena = 0;
	}
	public void init(int m, int n) {
		mClenov = m;
		sizeClena = n;
	}
	public boolean alloc(int size, int id) {
		
		//preveri ce je id ze noter
		Node temp = first.next;
		
		while(temp!=null) {
			if(najdiId(temp, id)!=-1) {
				return false;
			}
			temp = temp.next;
		}
		
		//prevelik size
		if(size<0 || size>sizeClena) return false;
		//prazen seznam
		if(last==null) {
			Node nov = new Node();
			first.next = nov;
			last = first;
			nov.id[0] = id;
			nov.idSize[0] = size;
			nov.idNoter++;
			nov.stZasedenih +=size;
			stClenov++;
			return true;
		}
	
		Node el = first.next;
		Node target = null;
		int razlika = sizeClena;
		while(el!=null) {
			//skozi vse clene iscemo tistega ki ima najblizjega praznega prostora do size
			int stProstih = sizeClena - el.stZasedenih;
	        //takoj ga vstavimo noter
			if(stProstih==size) {
				target = el;
				break;
			}
			//ce ima vec prostega prostora iscemo tistega z najmanjso razliko
			if(stProstih>size) {
				int razlika1 = stProstih - size;
				if(razlika>razlika1) {
					razlika = razlika1;
					target = el;
				}
			}
			el = el.next;
		}
		//ce ne najdemo ustreznega Node, ostvarimo nov, dodamo na koncu
		if(stClenov==mClenov) return false; //ne moremo ostvariti vec Node-ov
		
		if(target == null) {
			target = new Node();
			last.next.next = target;
			last = last.next;
			stClenov++;
		}
		//v target vstavimo size in id
		int index = target.idNoter;
		target.id[index] = id;
		target.idSize[index] = size;
		target.stZasedenih += size;
		target.idNoter++;
		return true;
		
	}
	
	public int najdiId(Node el, int id) {
		int index = -1;
		for(int i=0;i<el.idNoter;i++) {
			if(el.id[i] == id) {
				index = i;
				return index;
			}
		}
		return -1;
	}
	public int free(int id) {
		//iscemo id 
		Node el = first.next;
		int index = -1;
		
		while(el!=null) {
			if(najdiId(el, id)!=-1) {
				index = najdiId(el,id);
				break;
			}
			el = el.next;
		}
		if(index==-1) return 0;
		//preden izbrisemo preverimo ali treba premaknuti na levo
		if(index<el.idNoter-1) {
			//premaknemo v levo
			el.stZasedenih-=el.idSize[index];
			for(int i=index;i<el.idNoter-1;i++) {
				el.id[i] = el.id[i+1];
				el.idSize[i] = el.idSize[i+1];
			}
			el.id[el.idNoter-1]= -1;
			el.idSize[el.idNoter-1]= -1;
			el.idNoter--;
			return 1;
		}
		else {
			el.id[index] = -1;
			el.stZasedenih-=el.idSize[index];
			el.idSize[index] = -1;
			el.idNoter--;
			return 1;
		}
		
	}
	
	public void napisiVDatoteki(BufferedWriter izhod) {
		
		//izracunamo
		int[] zasedeni = new int[sizeClena+1];
		Node el = first.next;
		while(el!=null) {
			zasedeni[el.stZasedenih]++;
			el = el.next;
		}
		zasedeni[0] = zasedeni[0] + mClenov - stClenov;
		
		//izpisemo oz napisemo v datoteki
		for(int i=0;i<=sizeClena;i++) {
			//System.out.println(zasedeni[i]);
			try {
				izhod.write(zasedeni[i]+"");
				izhod.newLine();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	public void izpisiSeznam() {
		Node el = first.next;
		while(el!=null) {
			for(int i=0;i<el.idNoter;i++) {
				System.out.print("id=" + el.id[i] + " size=" + el.idSize[i] + "  ");
			}
			System.out.print(" zasedeni: " + el.stZasedenih + " ");
			System.out.println();
			el = el.next;
		}
		System.out.println();
	}

	
	public class Node{
		int[] id;
		int[] idSize;
		int stZasedenih;
		int idNoter;
		Node next;
		
		public Node() {
			id = new int[sizeClena];
			idSize = new int[sizeClena];
			stZasedenih = 0;
			idNoter = 0;
			next = null;
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
			Naloga5 obj = new Naloga5();
			String korak = br.readLine();
			int stKorakov = Integer.parseInt(korak);
			
			for(int i=0;i<stKorakov;i++) {
			
				vrstica = br.readLine();
				
				String[] niz = vrstica.split(",");
				char ukaz = niz[0].charAt(0);
				String m,n,size, id;
				int m1,n1,size1,id1;
				
				switch(ukaz) {
				case 'i':
				    m = niz[1];
				    m1 = Integer.parseInt(m);
				    n = niz[2];
				    n1 = Integer.parseInt(n);
					obj.init(m1,n1);
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
				} //switch
				
		} //for
			//obj.izpisiSeznam();
			
			FileOutputStream fos = new FileOutputStream(args[1]);
			BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
			
			obj.napisiVDatoteki(izhod);
			izhod.close();
			
		} catch (IOException e) {
			System.out.println(e);
		}
		//long endTime = System.nanoTime();
		//System.out.println("Took "+(endTime - startTime)*0.000000001 + " ns"); 
		
	}

}
