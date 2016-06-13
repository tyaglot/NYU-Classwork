import java.util.ArrayList;

public class DP {
	
	
	ArrayList<String> atomsRef;
	ArrayList<Boolean> atoms;
	ArrayList<ArrayList<pNode>> props;
	
	void prepare(String[] prop, ArrayList<String> patoms)
	{
		int tint;
		boolean not;
		ArrayList<pNode> tarray;
		props=new ArrayList<ArrayList<pNode>>();
		atoms=new ArrayList<Boolean>();
		if(patoms==null)
			atomsRef=new ArrayList<String>();
		
		for (String m: prop)
		{
			tarray=new ArrayList<pNode>();
			String[] temp=m.split("V");
			for(String l: temp)
			{
				not=false;
				if(l.charAt(0)=='~')
				{
					l=l.substring(1);
					not=true;
				}
				if((tint=atomsRef.indexOf(l))==-1)
				{
					atomsRef.add(l);
					atoms.add(null);
				}
				else
					tint=atomsRef.indexOf(l);
				tarray.add(new pNode(not, tint));
			}
			props.add(tarray);
		}
	}
	
	public void dp(String[] prop, ArrayList<String> atomList)
	{
		prepare(prop,atomList);
		dp1(props, atoms);
		
	}
	
	ArrayList<Boolean> dp1(ArrayList<ArrayList<pNode>> S, ArrayList<Boolean> V)
	{
		boolean tmp;
		boolean exit;
		boolean[][] tmpar=new boolean[V.size()][3];
		int tmpint;
		for(;;)
		{
			exit=true;
			tmp=false;
			if(S.isEmpty())
			{
				for(Boolean v :V)
					V.set(V.indexOf(v), v==null?true:v);
				return V;
			}
			for(ArrayList<pNode> s: S)
				if(s.isEmpty())
					tmp=true;
			if(tmp)
				return null;
			
			tmp=false;
			for(int i=0; i<tmpar.length; i++)
				for(int j=0; j<tmpar[0].length; j++)
					tmpar[i][j]=false;
			
			for(ArrayList<pNode> s: S)
			{
				
				for(pNode p:s)
				{
					if(!tmpar[p.atom][0])
					{
						tmpar[p.atom][0]=true;
						tmpar[p.atom][1]=p.not;
						tmpar[p.atom][2]=true;
					}
					else if(tmpar[p.atom][1]!=p.not)
						tmpar[p.atom][2]=false;
				}
			}
			tmpint=-1;
			for(int j=0; j<tmpar.length; j++)
				if(tmpar[j][0]&&tmpar[j][2])
					tmpint=j;
				
			if(tmpint!=-1)
			{
				exit=false;
				V.set(tmpint, tmpar[tmpint][1]);
				for(ArrayList<pNode> s: S)
				{
					for(pNode p:s)
					{
						if(p.atom==tmpint)
						{
							S.remove(s);
							break;
						}
					}
				}
			}
			
			for(ArrayList<pNode> s:S)
			{
				if(s.size()==1)
				{
					exit=false;
					V.set(s.get(0).atom,!s.get(0).not);
					for(ArrayList<pNode> c:S)
					{
						for(pNode a: c)
						{
							if(a.atom==s.get(0).atom && (a.not!=s.get(0).not))
							{
								S.remove(c);
								break;
							}
							else if (a.atom==s.get(0).atom && (a.not==s.get(0).not))
								c.remove(a);
						}
					}
				}
			}
			if(exit)
				break;
			
			
		}
		
		ArrayList<ArrayList<pNode>> S1;
		for(int i=0; i< V.size(); i++)
		{
			
			if(V.get(i)==null)
			{
				V.set(i, true);
				
				S1=new ArrayList<ArrayList<pNode>>();
				
				for(ArrayList<pNode> s:S)
				{
					S1.add(new ArrayList<pNode>());
					for(pNode p:s)
					{
						S1.get(S1.size()-1).add(new pNode(p.not,p.atom));
					}
				}
			
				for(ArrayList<pNode> c:S1)
				{
					for(pNode a:c)
					{
						if(a.atom==i && (a.not!=V.get(i)))
						{
							S1.remove(c);
							break;
						}
						else if (a.atom==i && (a.not==V.get(i)))
							c.remove(a);
					}
				}
				ArrayList<Boolean> VNEW=dp1(S1,V);
				if(VNEW!=null)
					return VNEW;
				
				V.set(i, false);
				
				S1=new ArrayList<ArrayList<pNode>>();
				
				for(ArrayList<pNode> s:S)
				{
					S1.add(new ArrayList<pNode>());
					for(pNode p:s)
					{
						S1.get(S1.size()-1).add(new pNode(p.not,p.atom));
					}
				}
			
				for(ArrayList<pNode> c:S1)
				{
					for(pNode a:c)
					{
						if(a.atom==i && (a.not!=V.get(i)))
						{
							S1.remove(c);
							break;
						}
						else if (a.atom==i && (a.not==V.get(i)))
							c.remove(a);
					}
				}
				
				return dp1(S1,V);
				
				
			}
		}
		return null;
		
	}
	
	
	
	class pNode
	{
		boolean not;
		int atom;
		public pNode(boolean n, int a)
		{
			not =n;
			atom=a;
		}
	}
	
	
	
	
}