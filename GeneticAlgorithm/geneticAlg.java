package generics;

import  java.util.*;

//import javax.print.attribute.standard.Chromaticity;

//import generics.linkedlist.Node;
class linkedlist{
	Node <?>head;
	static class Node<T extends Number>{
		Integer fitness;
		String chromosome;
		Node <?> next;
		Node(Integer fitness,String chromosome){
			this.fitness=fitness;
			this.chromosome=chromosome;
			next=null;
		}
		
	}
	public <T extends Number >linkedlist insert(linkedlist l,Integer fitness,String chromosome){
		Node<T> newnode=new Node<T>(fitness,chromosome);
		if(l.head==null) {
			l.head=newnode;
			
		}else {
			Node <?>l1=l.head;
			while(l1.next!=null) {
				l1=l1.next;
			}
			l1.next=newnode;
			
		}
		return l;
	}
	public int length(linkedlist l){
		Node<?>tmp=l.head;
		int k=0;
		while(tmp.next!=null) {
			k++;
			tmp=tmp.next;
			
		}
		return k;
	}
	public void delete(linkedlist l,int idx) {
		Node<?> tmp=l.head;
		for(int i=0;i<idx-1;i++) {
			tmp=tmp.next;
		}
		tmp.next=null;
	}
	public linkedlist.Node<?> goTo(linkedlist l,int idx){
		Node<?>l1=l.head;
		int i=0;
		for( i=0;i<idx-1;i++) {l1=l1.next;}
		return l1;
	}
	public void print(linkedlist list) {
		Node<?>l=list.head;
		while(l!=null) {
			System.out.println(l.chromosome+"->"+l.fitness);
			l=l.next;
		}
	}
}
class QuickSort{
	 //F arr[];
	 linkedlist.Node<?> head;
	 linkedlist l;
	public QuickSort(linkedlist l) {
		head=l.head;
		this.l=l;
	}
	public  void swap (linkedlist l,int idx1,int idx2) {
		linkedlist.Node<?> k=l.goTo(l, idx1);
		linkedlist.Node<?> k1=l.goTo(l, idx2);
		
		Integer fit=k.fitness;
		String chromosome=k.chromosome;
		k.chromosome=k1.chromosome;
		k.fitness=k1.fitness;
		k1.chromosome=chromosome;
		k1.fitness=fit;
		
		
		
}
	public   int partition(int low,int h){
		int i=low-1;
		linkedlist.Node<?> pivot=l.goTo(l, h);
		
		for(int j=low;j<=h-1;j++)
		{
			Integer fit=l.goTo(l, j).fitness;
			if(fit.compareTo(pivot.fitness) <0) {
				i++;
				swap(l,i,j);
			}
		}
		swap(l,i+1,h);
	
		
		return i+1;
	}
	public void qsort(int l,int h) {
		if(l<h) {
			int p=partition(l, h);
			qsort(l,p-1);
			qsort(p+1,h);
		}
	}
	
	
}
public class geneticAlg {	
	private final static   String genes="QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm ";
	private static  String secret="ajay";
	static String []PopulationPool;
	
	/*Goal :: Get min Fitness Possible  */
	
	public static int  Fitmin(String chromosome) {
		int fitness=0;
		for (int i=0;i<secret.length();i++) {
			if(chromosome.charAt(i)!=secret.charAt(i)) {
				fitness++;
			}
		}
		return fitness;
	}
	public static String genStr() {
		Random r=new Random();
		String construct="";
		for (int i=0;i<secret.length();i++)
		{
			construct+=genes.charAt(Math.abs(r.nextInt()%genes.length()));
		}
		return construct;
	}
	public static void  GeneratePopulationPool(int nums){
		
		
		String [] constructs=new String[nums];
		
		for (int i=0;i<nums;i++)
		{
			constructs[i]=genStr();
		}
		PopulationPool= constructs;
	}
	public static String[]  crossover(String p1,String p2) {
		/*define crossover function*/
		int half=p1.length()/2;
		String c1="";
		String c2="";
		c1+=(p1.substring(0,half)).concat( p2.substring(0,half));
		c2+=(p1.substring(half,p1.length())).concat( p2.substring(half,p2.length()));
		String [] ret=new String [2];
		ret[0]=c1;
		ret[1]=c2;
		return ret;
		
	}
	/*
	 * Mutation Rate limit 0-1 (float)
	 * */
	public static String Mutation(String chromosome,float Mutation_Rate){
		Random r=new Random();
		int MutateQty=Math.round( Mutation_Rate*chromosome.length());
		for(int i=0;i<MutateQty;i++) {
		char flipbit=genes.charAt(Math.abs( r.nextInt()%genes.length()));
		 chromosome.replace(chromosome.charAt(Math.abs( r.nextInt()%chromosome.length())), flipbit);
	}
		return chromosome;
	}
	/*
	 * selection Rate Range 0-1*/
	public static void selection(float selectionrate,linkedlist l) {
		
	    int selectionQty=Math.round(selectionrate*PopulationPool.length);
		//QuickSort <Integer>=new  QuickSort<Integer>()
	    QuickSort f=new QuickSort(l);
	    f.qsort(0, PopulationPool.length);
	    l.delete(l, selectionQty);
	   
	    
	}
	public static  linkedlist initializePopulation(int Size) {
		GeneratePopulationPool(Size);
		
		linkedlist l=new linkedlist();
		int k=PopulationPool.length;
		for(int i=0;i<k;i++) {
			l.insert(l,Fitmin(PopulationPool[i]), PopulationPool[i]);
		}
		return l;
	}
	public static void runEpochs(linkedlist l,int generations,float crossoverrate,float mutationrate,float selectionrate) {
		Random r=new Random();
		int rs,rs2;
		
		/*for(int i=0;i<generations;i++)*/
			while(l.head.fitness>0){
			
			
			
			/*  Crossover  */
			for(int j=0;j<Math.round(crossoverrate);j++) {
				String[]  cross=crossover(l.goTo(l, Math.abs(r.nextInt() %l.length(l))).chromosome,l.goTo(l, Math.abs(r.nextInt() %l.length(l))).chromosome);
				 rs=r.nextInt()%l.length(l);
				 rs2=r.nextInt()%l.length(l);
				l.goTo(l, Math.abs(rs)).chromosome=cross[0];
				
				l.goTo(l, rs2).chromosome=cross[1];
				l.goTo(l, Math.abs(rs)).fitness=Fitmin(cross[0]);
				l.goTo(l, rs2).fitness=Fitmin(cross[1]);
				
				
			}
			
			
			  // Mutation   
			for(int k=Math.abs(r.nextInt()%l.length(l));k<Math.round(mutationrate);k++) {
				String mutations=Mutation(l.goTo(l, Math.abs(r.nextInt() %l.length(l))).chromosome,mutationrate);
				l.goTo(l, k).chromosome=mutations;
				l.goTo(l, k).fitness=Fitmin(l.goTo(l, k).chromosome);
			}
		
			
			/*selection */
			selection(selectionrate,l);
			//System.out.println(l.goTo(l, 0).chromosome+"->"+l.goTo(l, 0).fitness);
			System.out.println(l.head.chromosome+"->"+l.head.fitness);
			//l.print(l);
			System.out.println("==============");
			//l.goTo(l, l.length(l)).next=initializePopulation(100).head;
		}
		
		
	}
	public static void main(String []args) {
		
		linkedlist initial=initializePopulation(100);
		//initial.print(initial);
		runEpochs(initial, 400, 0.5f, 0.0f, 0.1f);
		//String [] cv=crossover(initial.head.chromosome,initial.head.next.chromosome);
		//for(int i=0;i<2;i++) {
		//	System.out.println(cv[i]);
		//}
	}
}
