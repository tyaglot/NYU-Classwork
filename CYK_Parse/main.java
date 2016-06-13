import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


//Tolga Yenisey
public class main
{
	public static void main(String[] args) throws IOException
	{
		// The paths to the lexicon and grammar are the first two arguments, followed by the sentence
		BufferedReader in;
		ArrayList<grammarNode> grammar=new ArrayList<grammarNode>();
		ArrayList<lexNode> lexicon=new ArrayList<lexNode>();
		String temp;
		ArrayList<String> tempList;
		int N=args.length-2;
		String[] sentence=new String[N];
		for(int i=0;i<N;i++)
			sentence[i]=args[i+2].toLowerCase();
		
		
		in=new BufferedReader(new FileReader(args[0]));
		while((temp=in.readLine())!=null)
		{
			tempList=new ArrayList<String>(Arrays.asList(temp.split(" -> | ")));
			grammar.add(new grammarNode(tempList.get(0),tempList.subList(1, tempList.size()-1),tempList.get(tempList.size()-1)));
		}
		in.close();
		
		in=new BufferedReader(new FileReader(args[1]));
		while((temp=in.readLine())!=null)
		{
			tempList=new ArrayList<String>(Arrays.asList(temp.split(" -> | ")));
			lexicon.add(new lexNode(tempList.get(0),tempList.get(1), tempList.get(2)));
		}
		in.close();
		
		Tree[][][] P=new Tree[9][N][N];
		for(nonTerm M: nonTerm.values())
			for(int j=0; j<N; j++)
				for(int k=0;k<N;k++)
					P[M.ordinal()][j][k]=new Tree(M, j, k, null, null, null, 0.0);
		
		for(int i=0;i<N;i++)
		{
			for(lexNode l: lexicon)
			{
				if(l.word.equalsIgnoreCase(sentence[i]))
				{
					P[l.nt.ordinal()][i][i]=new Tree(l.nt, i, i, sentence[i], null, null, l.prob);
					P[l.nt.ordinal()][i][i].prob=l.prob;
					P[l.nt.ordinal()][i][i].word=sentence[i];
				}
			}
		}
		
		for(int length=2;length<=N;length++)
		{
			for(int i=0;i<=N-length;i++)
			{
				int j=i+length-1;
				for(nonTerm M: nonTerm.values())
				{
					for(int k=i;k<j;k++)
					{
						for(grammarNode g: grammar)
						{
							if(g.nt.equals(M))
							{
								double newProb=P[g.subTerms.get(0).ordinal()][i][k].prob*P[g.subTerms.get(1).ordinal()][k+1][j].prob*g.prob;
								if(newProb>P[M.ordinal()][i][j].prob)
								{
									P[M.ordinal()][i][j].prob=newProb;
									P[M.ordinal()][i][j].left=P[g.subTerms.get(0).ordinal()][i][k];
									P[M.ordinal()][i][j].right=P[g.subTerms.get(1).ordinal()][k+1][j];
								}
							}
						}
					}
				}
			}
		}
		if(P[nonTerm.S.ordinal()][0][sentence.length-1].prob==0)
		{
			System.out.println("The sentence cannot be parsed");
			return;
		}
		System.out.println("With probability "+P[nonTerm.S.ordinal()][0][sentence.length-1].prob+", the tree is:");
		printTree(P[nonTerm.S.ordinal()][0][sentence.length-1], 0);
		
	}
	public static void printTree(Tree P, int indent)
	{
		if(P!=null)
		{
			for(int i=0; i<indent; i++)
				System.out.print(" ");
			System.out.print(P.nt+" ");
			if (P.word!=null)
				System.out.print(P.word);
			System.out.print("\n");
			printTree(P.left, indent+3);
			printTree(P.right, indent+3);
		}
		
	}
	
}

