package prog4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class NB {

	public static void main(String[] args) throws IOException {
		double eps=.1;
		DecimalFormat df=new DecimalFormat("###.##");
		int rightcount=0;
		int total=0;
		int numTrain=Integer.parseInt(args[0]);
		Scanner scan=new Scanner(new File("stopwords.txt"));
		String temp;
		HashMap<String,Boolean> wordlist=new HashMap<String,Boolean>();
		HashMap<String, HashMap<String,Integer>> catwords=new HashMap<String, HashMap<String,Integer>>();
		HashMap<String,Boolean> stopwords=new HashMap<String, Boolean>();
		HashMap<String,Integer> catcount=new HashMap<String, Integer>();
		HashMap<String, Boolean> catonce=new HashMap<String, Boolean>();
		while(scan.hasNext())
			stopwords.put(scan.next().toLowerCase(), true);
		scan.close();
		BufferedReader in;
		in=new BufferedReader(new FileReader("corpus.txt"));
		temp=in.readLine();
		for(int i=0;i<numTrain;i++)
		{
			catonce.clear();
			while(temp.equalsIgnoreCase(""))
				temp=in.readLine();
			String cat=in.readLine().toLowerCase().replaceAll(" ", "");
			if(!catcount.containsKey(cat))
				catcount.put(cat, 1);
			else
				catcount.put(cat, catcount.get(cat)+1);
			while((temp=in.readLine())!=null&&!temp.equalsIgnoreCase(""))
				for(String s: temp.split(" "))
				{
					s=s.toLowerCase();
					if(s.matches(".*[.,]$")&&!s.matches(".*[.].*[.]"))
					{
						
						s=s.substring(0, s.length()-1);
					}
					
					if(s.length()>2&&!stopwords.containsKey(s))
						if(!catonce.containsKey(s))
						{
							wordlist.put(s,true);
							
							catonce.put(s.toLowerCase(), true);
							int num= catwords.containsKey(cat)?catwords.get(cat).containsKey(s)?catwords.get(cat).get(s):0:0;
							if(!catwords.containsKey(cat))
								catwords.put(cat, new HashMap<String,Integer>());
							catwords.get(cat).put(s, num+1);
						}
				}
					
		}
		
		HashMap<String, Double> logcat=new HashMap<String,Double>();
		HashMap<String,HashMap<String,Double>> logwords=new HashMap<String,HashMap<String,Double>>();
		for(String s:catwords.keySet())
		{
			logcat.put(s,-1*Math.log(              (eps+((double)catcount.get(s)/numTrain))/(1+ (catcount.size()*eps)  )                )/Math.log(2));
			logwords.put(s, new HashMap<String,Double>());
			for(String x: wordlist.keySet())
			{
				double num;
				if(!catwords.get(s).containsKey(x))
					num=0;
				else
					num=catwords.get(s).get(x);
				logwords.get(s).put(x, -1*Math.log(                (eps+num/catcount.get(s))/(1+2*eps)                  )/Math.log(2));
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		while((temp=in.readLine())!=null)
		{
			if(temp.equalsIgnoreCase(""))
				continue;
			total++;
			//System.out.println(temp+" "+total);
			catonce.clear();
			String name=temp;
			String actcat=in.readLine().replaceAll(" ", "");
			
			HashMap<String, Double> sum=new HashMap<String,Double>();
			for(String s: catcount.keySet())
				sum.put(s, logcat.get(s));
			while((temp=in.readLine())!=null&&!temp.equalsIgnoreCase(""))
			{
				//System.out.println(temp);
				for(String s:temp.split(" "))
				{
					s=s.toLowerCase();
					if(s.matches(".*[.,]$")&&!s.matches(".*[.].*[.]"))
					{
						
						s=s.substring(0, s.length()-1);
					}
					if(!wordlist.containsKey(s))
						continue;
					if(!catonce.containsKey(s))
					{
						catonce.put(s, true);
						for(String x: catcount.keySet())
						{
							sum.put(x, sum.get(x)+ logwords.get(x).get(s));
						}
					}
					
				}
				
			}
			String predcat=catcount.keySet().toArray()[0].toString();
			for(String s: catcount.keySet())
			{
				predcat=sum.get(s)<sum.get(predcat)?s:predcat;
			}
			double min=sum.get(predcat);
			double all=0;
			for(String s: catcount.keySet())
			{
				sum.put(s,min-sum.get(s)<7?Math.pow(2, min-sum.get(s) ):0);
				all+=sum.get(s);
			}
			for(String s: catcount.keySet())
			{
				sum.put(s, sum.get(s)/all);
			}
			rightcount+=predcat.equalsIgnoreCase(actcat)?1:0;
			System.out.println(name);
			System.out.println("Prediction: "+predcat+", "+ (predcat.equalsIgnoreCase(actcat)?"RIGHT":"WRONG"));
			
			for(String s: catcount.keySet())
				System.out.print(s+": "+df.format(sum.get(s))+" ");
			System.out.println();
			System.out.println();
		}
		
		System.out.println("Overall Accuracy: "+rightcount+" out of "+total+" = "+df.format((double)rightcount/total));
		
		
		
		
		in.close();	
	}

}
