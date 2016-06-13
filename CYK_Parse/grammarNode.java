import java.util.ArrayList;
import java.util.List;

//Tolga Yenisey
public class grammarNode
{
	nonTerm nt;
	ArrayList<nonTerm> subTerms;
	double prob;
	public grammarNode(String n, List<String> list, String p)
	{
		subTerms=new ArrayList<nonTerm>();
		nt=nonTerm.valueOf(n);
		for(String temp: list)
			subTerms.add(nonTerm.valueOf(temp));
		prob=Float.parseFloat(p.substring(1, p.length()-1));
	}
	public void print()
	{
		System.out.println(nt.toString()+" ");
		for(nonTerm temp: subTerms)
			System.out.print(temp.toString()+" ");
		System.out.println(prob);
	}
}
