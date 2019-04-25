import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Objects;
 
public class Train2 {
	public static class Syllable{
		public String syllable;
		public int tag;
		
		public Syllable(String syllable, int tag){
			this.syllable = syllable;
			this.tag = tag;
		}
		
		@Override
		public boolean equals(Object o) {
	        if(this == o) return true;    
	        if(o == null || getClass() != o.getClass()) return false;
	        
	        Syllable other = (Syllable)o;
	        
	        return other.syllable.equals(syllable) && other.tag == tag;
	    }
		
		@Override
		public int hashCode() {
		    return Objects.hash(syllable, tag);
		}
	}
	
	
    public static void main(String[] args){
    	
    	Vector<Syllable> vec = new Vector<Syllable>();
    	
        try{
            File file = new File("training_data.txt");
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            int beforeTag = 1;
            int befBeforeTag = 0;
            double startTag0 = 0;
            double startTag1 = 0;
            double[] secondTag0 = {0,0};
            double[] secondTag1 = {0,0};
            int linecnt = 0;
            double[][] a = new double[2][2];
            double[][][] a2 = new double[2][2][2];
            HashMap<Syllable, Double> b = new HashMap<Syllable, Double>();
            HashMap<Syllable, Double> b2 = new HashMap<Syllable, Double>();
            long tag0 = 1;
            long tag1 = 1;
            while((line = bufReader.readLine()) != null){
            	int size = line.length();
    			int currentTag=0;

    			for(int i=0;i<size;i++)
    			{
    				if(line.charAt(i)==' ' || line.charAt(i)=='\t' || line.charAt(i)=='\r' || line.charAt(i)=='\n') continue;
    				if(i<(size-1) && (line.charAt(i+1)==' ' || line.charAt(i+1)=='\t' || line.charAt(i+1)=='\r' || line.charAt(i+1)=='\n')) {
    					if(i==0) startTag1++;
    					currentTag=1;
    					tag1++;
    				}
    				else if(i==(size-1)) {
    					currentTag=1;
    					tag1++;
    				}
    				else {
    					if(i==0) startTag0++;
    					currentTag=0;
    					tag0++;
    				}
    				Syllable currentSyllable = new Syllable(line.charAt(i)+"", currentTag);
    				Syllable b2Syllable = new Syllable(line.charAt(i)+"", beforeTag);
    				vec.add(currentSyllable);
    				if(i>0 || linecnt>0) a[beforeTag][currentTag] += 1;
    				if(i>1 || linecnt>0) 
    				{
    					a2[befBeforeTag][beforeTag][currentTag] += 1;
    					//System.out.println("a2["+befBeforeTag+"]["+beforeTag+"]["+currentTag+"]="+a2[befBeforeTag][beforeTag][currentTag]);
    				}
    				if(b.containsKey(currentSyllable)) b.put(currentSyllable, b.get(currentSyllable)+1.0);
    				else b.put(currentSyllable, 1.0);
    				if(b2.containsKey(b2Syllable)) b2.put(b2Syllable, b2.get(b2Syllable)+1.0);
    				else b2.put(b2Syllable, 1.0);
    				befBeforeTag = beforeTag;
    				beforeTag = currentTag;
    			}
    			
    			linecnt++;
            } 
            
            startTag0 = Math.log((startTag0+1) / (double)(tag0+1));
            startTag1 = Math.log((startTag1+1) / (double)(tag1+1));
            
            a[0][0] = Math.log((a[0][0]+1) / (double)(tag0+1));
            a[0][1] = Math.log((a[0][1]+1) / (double)(tag1+1));
            a[1][0] = Math.log((a[1][0]+1) / (double)(tag0+1));
            a[1][1] = Math.log((a[1][1]+1) / (double)(tag1+1));
            
            a2[0][0][0] = Math.log((a2[0][0][0]+1) / (double)(tag0+1));
            a2[0][0][1] = Math.log((a2[0][0][1]+1) / (double)(tag1+1));
            a2[0][1][0] = Math.log((a2[0][1][0]+1) / (double)(tag0+1));
            a2[0][1][1] = Math.log((a2[0][1][1]+1) / (double)(tag1+1));
            a2[1][0][0] = Math.log((a2[1][0][0]+1) / (double)(tag0+1));
            a2[1][0][1] = Math.log((a2[1][0][1]+1) / (double)(tag1+1));
            a2[1][1][0] = Math.log((a2[1][1][0]+1) / (double)(tag0+1));
            a2[1][1][1] = Math.log((a2[1][1][1]+1) / (double)(tag1+1));
			
			for(Syllable key : b.keySet()){
				if(key.tag==0) b.put(key, (double)Math.log((b.get(key)+1) / (double)(tag0+1)));
				else b.put(key, (double)Math.log((b.get(key)+1) /(double)(tag1+1)));
			}
			
			for(Syllable key : b2.keySet()){
				if(key.tag==0) b2.put(key, (double)Math.log((b2.get(key)+1) / (double)(tag0+1)));
				else b2.put(key, (double)Math.log((b2.get(key)+1) /(double)(tag1+1)));
			}
			
			File datFile = new File("HMM2.dat");
			
			FileWriter fw = new FileWriter(datFile);
			//BufferedWriter out = new BufferedWriter(fw);
			fw.write("=====Start Tag======\n");
			fw.write("S_0_" + startTag0+"\n");
			fw.write("S_1_" + startTag1+"\n");
			
			fw.write("=====A1: Transition Probability======\n");
			fw.write("A1_0_0_" + a[0][0]+"\n");
			fw.write("A1_0_1_" + a[0][1]+"\n");
			fw.write("A1_1_0_" + a[1][0]+"\n");
			fw.write("A1_1_1_" + a[1][1]+"\n");
			
			fw.write("=====A2: Transition Probability======\n");
			fw.write("A2_0_0_0_" + a2[0][0][0]+"\n");
			fw.write("A2_0_0_1_" + a2[0][0][1]+"\n");
			fw.write("A2_0_1_0_" + a2[0][1][0]+"\n");
			fw.write("A2_0_1_1_" + a2[0][1][1]+"\n");
			fw.write("A2_1_0_0_" + a2[1][0][0]+"\n");
			fw.write("A2_1_0_1_" + a2[1][0][1]+"\n");
			fw.write("A2_1_1_0_" + a2[1][1][0]+"\n");
			fw.write("A2_1_1_1_" + a2[1][1][1]+"\n");
			
			fw.write("=====B : Output Probability=====\n");
			for(Syllable key : b.keySet()){
				fw.write("B1_"+key.syllable + "_" + key.tag + "_" + b.get(key)+"\n");
			}
			
			fw.write("=====B2 : Output Probability=====\n");
			for(Syllable key : b2.keySet()){
				fw.write("B2_"+key.syllable + "_" + key.tag + "_" + b2.get(key)+"\n");
			}
			
			System.out.println("finish");
			fw.close();
	        
            bufReader.close();
        }catch (FileNotFoundException e) {
        }catch(IOException e){
            System.out.println(e);
        }
     
    }
}
