//Tolga Yenisey
public class Tree
{
	public Tree(nonTerm NonTerm, int i, int i2, String string, Tree r, Tree l, double d)
	{
		startPhrase=i;
		endPhrase=i2;
		nt=NonTerm;
		word=string;
		left=l;
		right=r;
		prob=d;
	}
	int startPhrase; // if startPhrase==endPhrase, then this is a leaf
	int endPhrase;
	nonTerm nt;
	String word;
	Tree left;
	Tree right;
	double prob;
}
