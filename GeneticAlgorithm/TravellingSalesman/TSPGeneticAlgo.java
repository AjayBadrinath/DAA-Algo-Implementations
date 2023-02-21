package generics;

import java.util.Random;
/*
 * Travelling Salesman Problem Genetic Algorithm Implementation 
 * @author :Ajay Badrinath
 * @since  :2023-02-20
 * */


//Assume A complete Graph where there exist H Path Goal: Minimise the cost
/*
 * Linked List To Store The nodes which has the Path and Its Corresponding Cost
 * 
 * Class Consist of helper functions to create node ,delete node ,insert node , lengthof list
 * 
 * */
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
/*
 * Sorting Function based off weights/ fitness ,
 * Sorts the Linked List  using quicksort algo (O(nlogn))
 *
 * 
 * */
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
			if(fit.compareTo(pivot.fitness) <=0) {
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

/*
 * Class defining the Genetic Algorithm implementation 
 * Consist of Member Functions to 
 * Compute Cost,Fitness ,Count Substring
 * Init Func like genStr ,Set Gene generatePopulationPool,InitializePopulationPool
 * Main Func like Mutation ,crossover ,selections and Run Epochs
 * 
 * */
public class TSPGeneticAlgo {
	private static  String genepool="";
	 static String[] PopulationPool;
	 /*
	  * Function to compute cost  based off  Adjacency Matrix Provided   
	  * */
	  int ComputeCost(int [][] arr,String sequence) {
		//System.out.println('k');
		int cost=0;
		for(int i=0;i<sequence.length()-1;i++) {
			cost+=arr[sequence.charAt(i)-'0'-1][sequence.charAt(i+1)-'0'-1];
		}
		return cost;
		
	}
	  /*
	   * Fitness Function Extends the Cost Func 
	   * Check For Repeated Occurance of Substring k in s[1:len-1] and Penalise the cost accordingly
	   * 
	   * */
	  int Fitness(String sequence,int[][]arr) {
		int fitness=0;
		
		for(int i=1;i<sequence.length()-1;i++) {
			if((helperCount(sequence.substring(i+1,sequence.length()-1),sequence.charAt(i))>0) || (sequence.charAt(0)==sequence.charAt(i))) {
				fitness++;
			}
		}
		
		return ComputeCost(arr,sequence)+fitness*100;
		
	}
	/*Count occurance of kth char*/
	public  int  helperCount(String sequence,char subseq) {
		int c=0;
		for(int i =0;i<sequence.length();i++) {
			if(sequence.charAt(i)==(subseq)) {
				c++;
			}
		}
		return c;
	}
	/*Initialization Functions*/
	public  void SetGene(int[][]arr) {
		for(int i=0;i<arr.length;i++) {
			genepool+=((i+1));
		}
	}
	public  String  genStr() {
		Random r=new Random();
		String chromosome ="";
		char start=genepool.charAt(Math.abs(r.nextInt()%genepool.length()));
		chromosome+=start;
		
		//r.setSeed(System.currentTimeMillis());
		for(int i=1;i<=genepool.length()-1;i++) {
			
			chromosome+=genepool.charAt(Math.abs(r.nextInt()%genepool.length()));
		}
		chromosome+=start;
		return chromosome;
	}
	public  void generatePopulationPool(int nums) {
		String[] construct=new String[nums];
		
		for(int i=0;i<nums;i++) {
			construct[i]="";
			construct[i]+=(genStr());
		}
		PopulationPool=construct;
	}
	/*
	 * Encapsulate All the Above init Functions under one function
	 * */
	public  linkedlist InitializePopulationPool(int PopulationSize,int [][]graph) {
		SetGene(graph);
		generatePopulationPool(PopulationSize);
		linkedlist l=new linkedlist();
		for(int i=0;i<PopulationPool.length;i++) {
			//l.insert(l,ComputeCost(graph,PopulationPool[i]) , PopulationPool[i]);
			l.insert(l, Fitness(PopulationPool[i],graph), PopulationPool[i]);
		}
		
		return l;
		
	}
	/*Cross over func*/
	public  String[] crossover(String parent1,String parent2) {
		int half=parent1.length()/2;
		
		String []ret=new String[2];
		//ret[0]=(parent1.substring(0,half)).concat(parent2.substring(half,parent2.length()));
		//ret[1]=(parent2.substring(0,half)).concat(parent1.substring(half,parent1.length()));
		ret[0]=parent1.replace(parent1.charAt(2), parent2.charAt(3));
		ret[1]=parent2.replace(parent1.charAt(3), parent2.charAt(2));
		return ret;
	}
	/*Swap kth random char with another*/
	public  String Mutation(String chromosome,float MutationRate) {
		Random r=new Random();
		int MutateQty=Math.round( MutationRate*chromosome.length());
		for(int i=0;i<MutateQty;i++) {
			char flipbit=genepool.charAt(Math.abs( r.nextInt()%genepool.length()));
			 chromosome.replace(chromosome.charAt(Math.abs( r.nextInt()%chromosome.length())), flipbit);
		}
			return chromosome;
	}
	/*Sort and select based off fitness*/
	public   void selection(float selectionrate,linkedlist l) {
		
	    int selectionQty=Math.round(selectionrate*l.length(l));
		//QuickSort <Integer>=new  QuickSort<Integer>()
	    QuickSort f=new QuickSort(l);
	    f.qsort(0, l.length(l)-1);
	    l.delete(l, selectionQty);
	   
	    
	}
	/*Run the simulation*/
	public   void runEpochs(linkedlist l,int [][]arr,int generations,float crossoverrate,float mutationrate,float selectionrate) {
		Random r=new Random();
		int rs,rs2;
		int m=0;
		while(true) {
			//int k=0;
			
			for(int j=0;j<Math.round(crossoverrate);j++) {
				String[]  cross=crossover(l.goTo(l, Math.abs(r.nextInt() %l.length(l))).chromosome,l.goTo(l, Math.abs(r.nextInt() %l.length(l))).chromosome);
				/*
				rs=r.nextInt()%l.length(l);
				 rs2=r.nextInt()%l.length(l);
				l.goTo(l, Math.abs(rs)).chromosome=cross[0];
				
				l.goTo(l, rs2).chromosome=cross[1];
				l.goTo(l, Math.abs(rs)).fitness=Fitness(cross[0],arr);
				l.goTo(l, rs2).fitness=Fitness(cross[1],arr);
				*/
				l.insert(l, Fitness(cross[0],arr), cross[0]);
				l.insert(l, Fitness(cross[1],arr), cross[1]);
				
			}
			  // Mutation   
			for(int k=Math.abs(r.nextInt()%l.length(l));k<Math.round(mutationrate);k++) {
				String mutations=Mutation(l.goTo(l, Math.abs(r.nextInt() %l.length(l))).chromosome,mutationrate);
				l.goTo(l, k).chromosome=mutations;
				l.goTo(l, k).fitness=Fitness(l.goTo(l, k).chromosome,arr);
			}
		
			
			/*selection */
			selection(selectionrate,l);
			//System.out.println(l.goTo(l, 0).chromosome+"->"+l.goTo(l, 0).fitness);
			System.out.println(l.head.chromosome+"->"+l.head.fitness);
			//l.print(l);
			System.out.println("==============");
			m++;
		}
	 }
	public static void main(String []args) {
		/*
		int[][] graph={
				{1000,10,15,20},
				{10,1000,35,25},
				{15,35,1000,30},
				{20,25,30,1000}};
				*/
		int [][] graph= {
				{0,10,15,20},
				{5,0,9,10},
				{6,13,0,12},
				{8,8,9,0}
		};
		TSPGeneticAlgo s=new TSPGeneticAlgo();
		System.out.println(s.ComputeCost(graph,"24434"));
		//SetGene(graph);
		//System.out.println(genStr());
		//generatePopulationPool(100);
		//for(String i:PopulationPool) {
		//	System.out.println(i);
		//}
		linkedlist l=s.InitializePopulationPool(1000,graph);
		//l.print(l);
		//selection(0.34f,l);
		//System.out.println(l.length(l));
		//QuickSort s=new QuickSort(l);
		//s.qsort(0, l.length(l)-1);
		//l.print(l);
		//System.out.println(Fitness("12131",graph));
		s.runEpochs(l,graph,400,0.6f,0.6f,0.9f);
		//int k=100%0;
	}
}
