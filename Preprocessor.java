import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Objects;
import java.util.Scanner;

public class Preprocessor {
	public static class Syllable2{
		public Double probability0 = null;
		public Double probability1 = null;
		
		public Syllable2(Double probability0, Double probability1){
			this.probability0 = probability0;
			this.probability1 = probability1;
		}
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
    	while(true){
    	Scanner scan = new Scanner(System.in);
    	System.out.println("문자열을 입력하세요.");
    	String input = scan.nextLine();
    	
    	boolean[] definite = new boolean[input.length()];
    	boolean[] definiteBond = new boolean[input.length()];
    	int definiteCount = 0;
    	
    	for(int i=0;i<input.length();i++)
		{
			if(input.charAt(i)==' ' || input.charAt(i)=='\t' || input.charAt(i)=='\r' || input.charAt(i)=='\n') continue;
			if(i<input.length()-1 && (input.charAt(i+1)==' ' || input.charAt(i+1)=='\t' || input.charAt(i)=='\r' || input.charAt(i)=='\n')) 
			{
				definite[i-definiteCount] = true;
				definiteCount++;
			}
			if(i<(input.length()-1) && input.charAt(i+1)=='는') definiteBond[i-definiteCount] = true;
		}
    	String inputWithoutBlank = input.replaceAll("\\p{Z}", "");

    	try{
            File file = new File("HMM2.dat");
            Scanner sc = new Scanner(file);
            private double startTag0 = 0;
        	private double startTag1 = 0;
        	double[][] a = new double[2][2];
            double[][][] a2 = new double[2][2][2];
        	private HashMap<Character, Syllable2> b = new HashMap<Character, Syllable2>();
        	private HashMap<Character, Syllable2> b2 = new HashMap<Character, Syllable2>();
            while(sc.hasNextLine()){
            	String line = sc.nextLine();
            	String[] splittedLine = line.split("_");
	        	if(splittedLine[0].equals("S"))
	        	{
	        		//System.out.println("S");
	        		if(splittedLine[1].equals("0")) startTag0 = Double.parseDouble(splittedLine[2]);
	        		else startTag1 = Double.parseDouble(splittedLine[2]);
	        	}
	        	else if(splittedLine[0].equals("A1"))
	        	{
	        		//System.out.println("A1");
	        		int first = Integer.parseInt(splittedLine[1]);
	        		int second = Integer.parseInt(splittedLine[2]);
	        		a[first][second] = Double.parseDouble(splittedLine[3]);
	        	}
	        	else if(splittedLine[0].equals("A2"))
	        	{
	        		//System.out.println("A2");
	        		int first = Integer.parseInt(splittedLine[1]);
	        		int second = Integer.parseInt(splittedLine[2]);
	        		int third = Integer.parseInt(splittedLine[3]);
	        		a2[first][second][third] = Double.parseDouble(splittedLine[4]);
	        	}
	        	else if(splittedLine[0].equals("B1"))
	        	{
	        		//System.out.println("B1");
	        		Character syllable = splittedLine[1].charAt(0);
	        		int tag = Integer.parseInt(splittedLine[2]);
	        		Syllable2 newSyllable = b.get(syllable);
	        		if(newSyllable == null)
	        		{
	        			if(tag==0) newSyllable = new Syllable2(Double.parseDouble(splittedLine[3]), null);
	        			else newSyllable = new Syllable2(null, Double.parseDouble(splittedLine[3]));
		        		b.put(syllable, newSyllable);
	        		}
	        		else
	        		{
	        			if(tag==0) newSyllable.probability0 = Double.parseDouble(splittedLine[3]);
	        			else newSyllable.probability1 = Double.parseDouble(splittedLine[3]);
	        		}
	        	}
	        	else if(splittedLine[0].equals("B2"))
	        	{
	        		//System.out.println("B2");
	        		Character syllable = splittedLine[1].charAt(0);
	        		int tag = Integer.parseInt(splittedLine[2]);
	        		Syllable2 newSyllable = b2.get(syllable);
	        		if(newSyllable == null)
	        		{
	        			if(tag==0) newSyllable = new Syllable2(Double.parseDouble(splittedLine[3]), null);
	        			else newSyllable = new Syllable2(null, Double.parseDouble(splittedLine[3]));
		        		b2.put(syllable, newSyllable);
	        		}
	        		else
	        		{
	        			if(tag==0) newSyllable.probability0 = Double.parseDouble(splittedLine[3]);
	        			else newSyllable.probability1 = Double.parseDouble(splittedLine[3]);
	        		}
	        	}
	        }

            double v0=0, v1=0, v00=0, v01=0, v10=0, v11=0, v000=0, v001=0, v010=0, v011=0, v100=0, v101=0, v110=0, v111=0;
            Syllable2 syllable0 = b.get(inputWithoutBlank.charAt(0));
            System.out.println("0: is in word? : " + isInWord(inputWithoutBlank, 0));
            if(definite[0])
            {
            	System.out.println("definite0");
            	v0 = -10;
            	v1 = 0;
            }
            else if(definiteBond[0] || isInWord(inputWithoutBlank, 0))
        	{
            	v0 = 0;
            	v1 = -10;
        	}
            else if(syllable0 == null)
            {
            	System.out.println("A0");
            	v0 = startTag0;
            	v1 = startTag1;
            }
            else if(syllable0.probability0 == null && syllable0.probability1 != null)
            {
            	System.out.println("B0");
            	v0 = startTag0-10;
            	v1 = startTag1;
            }
            else if(syllable0.probability0 != null && syllable0.probability1==null)
            {
            	System.out.println("C0");
            	v0 = startTag0;
            	v1 = startTag1-10;
            }
            else
            {
            	System.out.println("D0");
            	System.out.println(syllable0.probability0);
            	System.out.println(syllable0.probability1);
            	v0 = startTag0 + syllable0.probability0;
                v1 = startTag1 + syllable0.probability1;
            } 

            if(inputWithoutBlank.length()>1)
            {
            	Syllable2 syllable1 = b.get(inputWithoutBlank.charAt(1));
            	System.out.println("1: is in word? : " + isInWord(inputWithoutBlank, 1));
	            if(definite[1])
	            {
	            	System.out.println("definite1");
	            	v00 = -10;
	        		v01 = 0;
	        		v10 = -10;
	        		v11 = 0;
	            }
	            else if(definiteBond[1] || isInWord(inputWithoutBlank, 1))
            	{
	            	v00 = 0;
	        		v01 = -10;
	        		v10 = 0;
	        		v11 = -10;
            	}
	            else if(syllable1 == null)
	            {
	            	System.out.println("A1");
	            	v00 =v0 + a[0][0];
	        		v01 =v0 + a[0][1];
	        		v10 =v1 + a[1][0];
	        		v11 =v1 + a[1][1];
	            }
	            else if(syllable1.probability0 == null && syllable1.probability1!=null)
	            {
	            	System.out.println("B1");
	        		v00 =v0 + a[0][0]-10;
	        		v01 =v0 + a[0][1];
	        		v10 =v1 + a[1][0]-10;
	        		v11 =v1 + a[1][1];
	            }
	            else if(syllable1.probability0 != null && syllable1.probability1==null)
	            {
	            	System.out.println("C1");
	            	v00 =v0 + a[0][0];
	        		v01 =v0 + a[0][1]-10;
	        		v10 =v1 + a[1][0];
	        		v11 =v1 + a[1][1]-10;
	            }
	            else
	            {
	            	System.out.println("D1");
	            	Syllable2 syllable2 = null;
	            	if(inputWithoutBlank.length()>2) syllable2 = b2.get(inputWithoutBlank.charAt(2));
	            	if(syllable2 == null)
	            	{
	            		v00 =v0 + a[0][0] + syllable1.probability0;
		        		v01 =v0 + a[0][1] + syllable1.probability1;
		        		v10 =v1 + a[1][0] + syllable1.probability0;
		        		v11 =v1 + a[1][1] + syllable1.probability1;
	            	}
	            	else if(syllable2.probability0 == null && syllable2.probability1 != null)
	            	{
	            		v00 =v0 + a[0][0] + syllable1.probability0*2;
		        		v01 =v0 + a[0][1] + syllable1.probability1;
		        		v10 =v1 + a[1][0] + syllable1.probability0*2;
		        		v11 =v1 + a[1][1] + syllable1.probability1;
	            	}
	            	else if(syllable2.probability0 != null && syllable2.probability1 == null)
	            	{
	            		v00 =v0 + a[0][0] + syllable1.probability0;
		        		v01 =v0 + a[0][1] + syllable1.probability1*2;
		        		v10 =v1 + a[1][0] + syllable1.probability0;
		        		v11 =v1 + a[1][1] + syllable1.probability1*2;
	            	}
	            	else
	            	{
	            		if(syllable2.probability0 > syllable2.probability1)
	    				{
		            		v00 =v0 + a[0][0] + syllable1.probability0;
		            		v01 =v0 + a[0][1] + syllable1.probability1*2;
		            		v10 =v1 + a[1][0] + syllable1.probability0;
		            		v11 =v1 + a[1][1] + syllable1.probability1*2;
	    				}
	            		else
	            		{
	            			v00 =v0 + a[0][0] + syllable1.probability0;
	    	        		v01 =v0 + a[0][1] + syllable1.probability1;
	    	        		v10 =v1 + a[1][0] + syllable1.probability0;
	    	        		v11 =v1 + a[1][1] + syllable1.probability1;
	            		}
	            	}
	            } 
            }
            
            PathProb[][] pp = new PathProb[inputWithoutBlank.length()][(int) Math.pow(2, inputWithoutBlank.length())];
            pp[0][0] = new PathProb("0,", 0, v0);
            pp[0][1] = new PathProb("1,", 1, v1);
            if(inputWithoutBlank.length()>1)
            {
	            pp[1][0] = new PathProb("0,0,", 0, v0 + v00);
	            pp[1][1] = new PathProb("0,1,", 1, v0 + v01);
	            pp[1][2] = new PathProb("1,0,", 0, v1 + v10);
	            pp[1][3] = new PathProb("1,1,", 1, v1 + v11);
            }
            
            //System.out.println(v[0][0]);
            //System.out.println(v[0][1]);
            //System.out.println();
            
            for(int i=2; i<inputWithoutBlank.length(); i++)
            {
            	Syllable2 syllableI = b.get(inputWithoutBlank.charAt(i));
            	System.out.println(i + ": is in word? : " + isInWord(inputWithoutBlank, i));
            	if(definite[i])
            	{
            		System.out.println("definite2");
            		v000 = -10;
            		v001 = 0;
            		v010 = -10;
            		v011 = 0;
            		v100 = -10;
            		v101 = 0;
            		v110 = -10;
            		v111 = 0;
            	}
            	else if(definiteBond[i] || isInWord(inputWithoutBlank, i))
            	{
            		v000 = 0;
            		v001 = -10;
            		v010 = 0;
            		v011 = -10;
            		v100 = 0;
            		v101 = -10;
            		v110 = 0;
            		v111 = -10;
            	}
            	else if(syllableI == null)
            	{
            		System.out.println("A");
            		//v00 = v[i-1][0] + a[0][0];
            		v000 =a2[0][0][0];
            		v001 =a2[0][0][1];
            		v010 =a2[0][1][0];
            		v011 =a2[0][1][1];
            		v100 =a2[1][0][0];
            		v101 =a2[1][0][1];
            		v110 =a2[1][1][0];
            		v111 =a2[1][1][1];
            	}
            	else if(syllableI.probability0 == null && syllableI.probability1 != null)
            	{
            		System.out.println("B");
            		v000 =a2[0][0][0]-10;
            		v001 =a2[0][0][1];
            		v010 =a2[0][1][0]-10;
            		v011 =a2[0][1][1];
            		v100 =a2[1][0][0]-10;
            		v101 =a2[1][0][1];
            		v110 =a2[1][1][0]-10;
            		v111 =a2[1][1][1];
            	}
            	else if(syllableI.probability0 != null && syllableI.probability1 == null)
            	{
            		System.out.println("C");
            		v000 =a2[0][0][0];
            		v001 =a2[0][0][1]-10;
            		v010 =a2[0][1][0];
            		v011 =a2[0][1][1]-10;
            		v100 =a2[1][0][0];
            		v101 =a2[1][0][1]-10;
            		v110 =a2[1][1][0];
            		v111 =a2[1][1][1]-10;
            	}
            	else
            	{
            		System.out.println("D");
            		Syllable2 syllableIplus1 = null;
	            	if(inputWithoutBlank.length()>(i+1)) syllableIplus1 = b2.get(inputWithoutBlank.charAt(i+1));
        			if(syllableIplus1 == null)
        			{
		            	v000 =a2[0][0][0] + syllableI.probability0;
		            	v001 =a2[0][0][1] + syllableI.probability1;
		            	v010 =a2[0][1][0] + syllableI.probability0;
		            	v011 =a2[0][1][1] + syllableI.probability1;
		            	v100 =a2[1][0][0] + syllableI.probability0;
		            	v101 =a2[1][0][1] + syllableI.probability1;
		            	v110 =a2[1][1][0] + syllableI.probability0;
		            	v111 =a2[1][1][1] + syllableI.probability1;
        			}
        			else if(syllableIplus1.probability0 == null && syllableIplus1.probability1 != null)
        			{
		            	v000 =a2[0][0][0] + syllableI.probability0*2;
		            	v001 =a2[0][0][1] + syllableI.probability1;
		            	v010 =a2[0][1][0] + syllableI.probability0*2;
		            	v011 =a2[0][1][1] + syllableI.probability1;
		            	v100 =a2[1][0][0] + syllableI.probability0*2;
		            	v101 =a2[1][0][1] + syllableI.probability1;
		            	v110 =a2[1][1][0] + syllableI.probability0*2;
		            	v111 =a2[1][1][1] + syllableI.probability1;
        			}
        			else if(syllableIplus1.probability0 != null && syllableIplus1.probability1 == null)
        			{
		            	v000 =a2[0][0][0] + syllableI.probability0;
		            	v001 =a2[0][0][1] + syllableI.probability1;
		            	v010 =a2[0][1][0] + syllableI.probability0;
		            	v011 =a2[0][1][1] + syllableI.probability1;
		            	v100 =a2[1][0][0] + syllableI.probability0;
		            	v101 =a2[1][0][1] + syllableI.probability1;
		            	v110 =a2[1][1][0] + syllableI.probability0;
		            	v111 =a2[1][1][1] + syllableI.probability1;
        			}
        			else
        			{
        				if(syllableIplus1.probability0 > syllableIplus1.probability1)
        				{
			            	v000 =a2[0][0][0] + syllableI.probability0;
			            	v001 =a2[0][0][1] + syllableI.probability1*2;
			            	v010 =a2[0][1][0] + syllableI.probability0;
			            	v011 =a2[0][1][1] + syllableI.probability1*2;
			            	v100 =a2[1][0][0] + syllableI.probability0;
			            	v101 =a2[1][0][1] + syllableI.probability1*2;
			            	v110 =a2[1][1][0] + syllableI.probability0;
			            	v111 =a2[1][1][1] + syllableI.probability1*2;
        				}
        				else
        				{
        					v000 =a2[0][0][0] + syllableI.probability0;
			            	v001 =a2[0][0][1] + syllableI.probability1;
			            	v010 =a2[0][1][0] + syllableI.probability0;
			            	v011 =a2[0][1][1] + syllableI.probability1;
			            	v100 =a2[1][0][0] + syllableI.probability0;
			            	v101 =a2[1][0][1] + syllableI.probability1;
			            	v110 =a2[1][1][0] + syllableI.probability0;
			            	v111 =a2[1][1][1] + syllableI.probability1;
        				}
        			}
            	}
            	
            	for(int j=0; j<(int) Math.pow(2, i+1); j+=2)
            	{
            		if(pp[i-2][j/4].lastTag==0 && pp[i-1][j/2].lastTag==0)
            		{
            			pp[i][j] = new PathProb(pp[i-1][j/2].path+"0,", 0, pp[i-1][j/2].probability + v000);
            			pp[i][j+1] = new PathProb(pp[i-1][j/2].path+"1,", 1, pp[i-1][j/2].probability + v001);
            		}
            		else if(pp[i-2][j/4].lastTag==0 && pp[i-1][j/2].lastTag==1)
            		{
            			pp[i][j] = new PathProb(pp[i-1][j/2].path+"0,", 0, pp[i-1][j/2].probability + v010);
            			pp[i][j+1] = new PathProb(pp[i-1][j/2].path+"1,", 1, pp[i-1][j/2].probability + v011);
            		}
            		else if(pp[i-2][j/4].lastTag==1 && pp[i-1][j/2].lastTag==0)
            		{
            			pp[i][j] = new PathProb(pp[i-1][j/2].path+"0,", 0, pp[i-1][j/2].probability + v100);
            			pp[i][j+1] = new PathProb(pp[i-1][j/2].path+"1,", 1, pp[i-1][j/2].probability + v101);
            		}
            		else if(pp[i-2][j/4].lastTag==1 && pp[i-1][j/2].lastTag==1)
            		{
            			pp[i][j] = new PathProb(pp[i-1][j/2].path+"0,", 0, pp[i-1][j/2].probability + v110);
            			pp[i][j+1] = new PathProb(pp[i-1][j/2].path+"1,", 1, pp[i-1][j/2].probability + v111);
            		}
            	}
            	
            	//v[i][0] = v00>v10 ? v00 : v10;
            	//v[i][1] = v01>v11 ? v01 : v11;
            	//p[i] = v[i][0]>v[i][1] ? 0 : 1;

            }
            
            PathProb theOptimalPath = pp[inputWithoutBlank.length()-1][0];
            double theBiggestProb = pp[inputWithoutBlank.length()-1][0].probability;
            for(int i=0; i<(int) Math.pow(2, inputWithoutBlank.length()); i++)
            {
            	//System.out.println("path: " + pp[inputWithoutBlank.length()-1][i].path + "probability: " + pp[inputWithoutBlank.length()-1][i].probability);
            	if(theBiggestProb<pp[inputWithoutBlank.length()-1][i].probability)
            	{
            		theBiggestProb = pp[inputWithoutBlank.length()-1][i].probability;
            		theOptimalPath = pp[inputWithoutBlank.length()-1][i];
            	}
            }
            
            System.out.println("theOptimalPath : " + theOptimalPath.path);
            
            String[] p = theOptimalPath.path.split(",");
                      
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<inputWithoutBlank.length();i++)
            {
            	//System.out.println("inputWithoutBlank.charAt(i)"+ inputWithoutBlank.charAt(i));
            	sb.append(inputWithoutBlank.charAt(i)+"");
            	if(p[i].equals("1") || definite[i]) sb.append(" ");
            }
            output = sb.toString();
            
            System.out.println("자동 띄어쓰기 결과: " + output);
             
    	}catch (FileNotFoundException e) {
        }
    	}
    }
}