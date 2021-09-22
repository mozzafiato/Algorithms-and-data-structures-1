import java.io.*;

public class Naloga1 {

		int xZac, yZac;
		int stStrank; //M
		int sedezi; //N
		Stranka[] stranke;
		public int[] rezultat;
		int index;
		int trenMinimalnaPot;
	
		
		public Naloga11(int xt, int yt, int N, int M) {
			xZac = xt;
			yZac = yt;
			stStrank = M;
			sedezi = N;
			stranke = new Stranka[2*M];
			index = 0;
			rezultat = new int[2*M];
			trenMinimalnaPot = -1;
		}
		
		public void dodajStranko(int oznaka,int xz,int yz,int xo,int yo) {
			 Stranka str = new Stranka(oznaka, xz, yz, xo, yo);
			stranke[index] = str;
			index++;
		}
		
		public void izpisiStranke() {
			for(int i=0;i<stStrank;i++) {
				System.out.println(stranke[i].oznaka + " " + stranke[i].x1 + " " + stranke[i].y1);
				System.out.println();
			}
		}
		public void dopolniStranke() {
			for(int i=0;i<stStrank;i++) {
				stranke[i+stStrank] = stranke[i];
			}
		}
	
		public int razdalja(int x1, int y1, int x2, int y2) {
			return (Math.abs(x1-x2) + Math.abs(y1-y2));
		}
		
			
		public boolean koncano(int[] stNoter) {
			for(int i=0;i<stNoter.length;i++) {
				if(stNoter[i]==0) return false;
			}
			return true;
		}
		public void izpisi(int[] a) {
			for(int i=0;i<a.length;i++) {
				System.out.print(a[i] + " ");
			}
			System.out.println();
		}
		
		
		public void izpisiRezultat() {
			for(int i=0;i<rezultat.length;i++) {
				System.out.print(rezultat[i] + " ");
			}
			System.out.println();
		}
		public void preslikaj(int[] a, int[] b) {
			for(int i=0;i<a.length;i++) {
				b[i] = a[i];
			}
		}
		
    public void funkcija(Stranka[] stranke, int xt, int yt, int[] stNoter, int prostiSedezi, int pot, int[] rez, int rezi, int stEnk) {
    	
    	if(stEnk==2*stStrank) {
    	     //System.out.println("Najden je pot: "+ pot);
    	     //izpisi(rez);
    	    
    	    if(trenMinimalnaPot==-1 || (trenMinimalnaPot!=-1 && pot<trenMinimalnaPot)) {
    	    	trenMinimalnaPot = pot;
    	    	preslikaj(rez, rezultat);
    	    }   
    	}
    	
	    	//ako imas nesto ostavi
	    	for(int k=0; k<stStrank; k++) {
	    		if(stNoter[k]==1 && stNoter[k+stStrank]==0) {
	    			int tempPot;
	    			if(pot==-1) tempPot = razdalja(xt,yt,stranke[k].x2, stranke[k].y2);
	    			else tempPot = pot+razdalja(xt,yt,stranke[k].x2, stranke[k].y2);
	    			stNoter[k+stStrank]=1;
	    			//System.out.println("Ostavljamo stranko " + (k+1)+ " tren.pot je: " + tempPot);
	    			rez[rezi] = k+1;
	    			
	    			if(trenMinimalnaPot==-1 || (trenMinimalnaPot!=-1 && trenMinimalnaPot>tempPot))
	    			funkcija(stranke, stranke[k].x2, stranke[k].y2, stNoter, prostiSedezi+1, tempPot, rez, rezi+1,stEnk+1);
	    			
	    			stNoter[k+stStrank]=0;
	    			rez[rezi] = 0;
	    		}
	    	}
    	
    	    //vzami pri pogoju da imas dovolj sedezov
	    	for(int m=0;m<stStrank;m++) {
	    		if(stNoter[m]==0 && prostiSedezi!=0) {
	    			int tempPot;
	    			if(pot==-1) tempPot = razdalja(xt,yt,stranke[m].x1, stranke[m].y1);
	    			else tempPot = pot+razdalja(xt,yt,stranke[m].x1, stranke[m].y1);
	    			stNoter[m] = 1;
	    			//System.out.println("Vzeli smo stranko " + (m+1)+ " tren.pot je: " + tempPot);
	    			rez[rezi] = m+1;
	    			
	                if(trenMinimalnaPot==-1 || (trenMinimalnaPot!=-1 && trenMinimalnaPot>tempPot))
	    			funkcija(stranke, stranke[m].x1, stranke[m].y1, stNoter, prostiSedezi-1, tempPot, rez, rezi+1,stEnk+1);
	    			stNoter[m] = 0;
	    			rez[rezi] = 0;
	    		}
	    	}
         
    	
    }//funkcija
    
    public void napisiVDatoteki(BufferedWriter izhod) {
    	
    	try {
	    	for(int i=0;i<rezultat.length;i++) {
	    		if(i<(2*stStrank)-1) izhod.write(rezultat[i]+ ",");
	    		else izhod.write(rezultat[i]+"");
	    	}
    	} catch (IOException e) {
			System.out.println(e);
		}
    }
    
		
	public class Stranka {
		int oznaka;
		int x1, y1, x2, y2;
		
		public Stranka(int o, int X1, int Y1, int X2, int Y2) {
			oznaka = o;
			x1 = X1;
			y1 = Y1;
			x2 = X2;
			y2 = Y2;
		}
	}
	
	public static void main(String[] args) {
	    long startTime = System.nanoTime();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(args[0]));
			
		} catch (FileNotFoundException fnfex) {
			System.out.println(fnfex.getMessage() + "The File was not found");
		}
		try {
			
	        String sedezi = br.readLine();
	        int sedezi1 = Integer.parseInt(sedezi);
	        String koordinati = br.readLine();
	        String[] koordinatiT = koordinati.split(",");
	        int xt = Integer.parseInt(koordinatiT[0]);
	        int yt = Integer.parseInt(koordinatiT[1]);
	        String stStrank = br.readLine();
	        int stStrank1 = Integer.parseInt(stStrank);
	        
	        Naloga11 taxi = new Naloga11(xt,yt, sedezi1, stStrank1);
	        
			for(int i=0;i<stStrank1;i++) {
				String stranka = br.readLine();
				String[] strankaS = stranka.split(",");
				int oznaka = Integer.parseInt(strankaS[0]);
				int xz = Integer.parseInt(strankaS[1]);
				int yz = Integer.parseInt(strankaS[2]);
				int xo = Integer.parseInt(strankaS[3]);
				int yo = Integer.parseInt(strankaS[4]);
				taxi.dodajStranko(oznaka, xz, yz, xo, yo);
			}
		
			int[] rez = new int[2*stStrank1];
			int[] strankeNoter = new int[2*stStrank1];
			
			taxi.dopolniStranke();
			
			for(int id=0;id<stStrank1;id++) {
					
				for(int i=0;i<2*stStrank1;i++) {
					strankeNoter[i]=0;
					rez[i]=0;
				}
					
				strankeNoter[id]=1;
				rez[0]=id+1;
				int razdalja = Math.abs(xt - taxi.stranke[id].x1) + Math.abs(yt - taxi.stranke[id].y1);
				taxi.funkcija(taxi.stranke, taxi.stranke[id].x1, taxi.stranke[id].y1, strankeNoter, taxi.sedezi-1, razdalja, rez, 1,1);
			}
			
			
			
			//System.out.println("Minimalna pot je: "+ taxi.trenMinimalnaPot);
			//taxi.izpisiRezultat();
			
			FileOutputStream fos = new FileOutputStream(args[1]);
			BufferedWriter izhod = new BufferedWriter(new OutputStreamWriter(fos));
			
			taxi.napisiVDatoteki(izhod);
			izhod.close();
			
			
		} 
		 catch (IOException e) {
			System.out.println(e);
		}
		
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime)*0.000000001 + " s"); 
			
	}

}

