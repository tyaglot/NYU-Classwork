//Tolga Yenisey
public class lexNode
{
	nonTerm nt;
	String word;
	double prob;
	public lexNode(String n, String w, String p)
	{
		prob=Float.parseFloat(p.substring(1, p.length()-1));
		word=w.toLowerCase();
		nt=nonTerm.valueOf(n);
	}
	public void print()
	{
		System.out.print(nt.toString()+" ");
		System.out.print(word+" ");
		System.out.println(prob);
	}
}
