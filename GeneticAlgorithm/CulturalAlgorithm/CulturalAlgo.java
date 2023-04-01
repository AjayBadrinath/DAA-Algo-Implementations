package generics;


import java.util.Random;

import generics.linkedlist.Node;


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
 class Walk {
	private static  String genepool="";
	 static String[] PopulationPool;
	 int normativeKnowledge[];
	 QuickSort s;
	 linkedlist situationalKnowledge=null;
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
		
		return ComputeCost(arr,sequence)+fitness*10;
		
	}
	/*Count occurance of kth char*/
	  int  helperCount(String sequence,char subseq) {
		int c=0;
		for(int i =0;i<sequence.length();i++) {
			if(sequence.charAt(i)==(subseq)) {
				c++;
			}
		}
		return c;
	}
	/*Initialization Functions*/
	  void SetGene(int[][]arr) {
		for(int i=0;i<arr.length;i++) {
			genepool+=((i+1));
		}
	}
	  String  genStr() {
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
	  void init_BeliefSpace(int []normativeRange,linkedlist l) {
		  normativeKnowledge=normativeRange;
		  s=new QuickSort(l);
		  s.qsort(0, l.length(l)+1);
		  l.print(l);
		  System.out.println("========");
		  situationalKnowledge=new linkedlist();
		  //System.out.println(l.goTo(l, 2).chromosome);
		  for(int i=1;i<=2;i++) {
			  situationalKnowledge.insert(situationalKnowledge, l.goTo(l, i).fitness, l.goTo(l, i).chromosome);
		  }
		  situationalKnowledge.print(situationalKnowledge);
		  
		  
		  
	  }
	  void update_BeliefSpace(linkedlist l) {
		  s=new QuickSort(l);
		  s.qsort(0, l.length(l));
		  for(int i=1;i<=2;i++) {
			  if(l.goTo(l, i).fitness<situationalKnowledge.goTo(l, i).fitness) {
			  situationalKnowledge.insert(situationalKnowledge, l.goTo(l, i).fitness, l.goTo(l, i).chromosome);
			  normativeKnowledge[i-1]=l.goTo(l, i).fitness;
		  }
		  }
		  
	  }
	  public  String[] crossover(String parent1,String parent2) {
			int half=parent1.length()/2;
			
			String []ret=new String[2];
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
		void updateBeliefSpace(int [][]arr) {
			s=new QuickSort(situationalKnowledge);
			String []k=crossover(situationalKnowledge.goTo(situationalKnowledge, 1).chromosome,situationalKnowledge.goTo(situationalKnowledge, 2).chromosome);
			for(int i=0;i<2;i++) {
				
			situationalKnowledge.insert(situationalKnowledge,  Fitness(k[0],arr),k[0]);
			}
			String mut=Mutation(situationalKnowledge.head.chromosome,0.5f);
			situationalKnowledge.head.chromosome=mut;
			situationalKnowledge.head.fitness=Fitness(mut,arr);
			s.qsort(0, situationalKnowledge.length(situationalKnowledge));
			
		}
		void replace(Node<?>l,int[][]arr) {
			String []k=crossover(situationalKnowledge.goTo(situationalKnowledge, 1).chromosome,situationalKnowledge.goTo(situationalKnowledge, 2).chromosome);
			String mut=Mutation(k[0],0f);
			l.chromosome=mut;
			l.fitness=Fitness(mut,arr);
		}
		boolean accept(Node<?> l) {
			if(l.fitness>normativeKnowledge[0] && l.fitness<normativeKnowledge[1]) {
				return true;
			}
			return false;
		}
		void RunEpoch(int [][]arr,int n_iteration,int []normativeRange,linkedlist l) {
			init_BeliefSpace(normativeRange,l);
			s=new QuickSort(l);
				for(int k=0;k<n_iteration;k++) {
					for(int i=1;i<l.length(l);i++) {
						if(accept(l.goTo(l, i))==false) {
							replace(l.goTo(l, i),arr);
						}
					}
					
					updateBeliefSpace(arr);
					update_BeliefSpace(l);
					//l.print(l);
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
 public class CulturalAlgo{
		public static void main(String [] args) {
			int [][] graph= {
					{0,3,5,7},
					{3,0,2,6},
					{5,2,0,4},
					{7,6,4,0}
			};
			Walk k=new Walk();

			linkedlist l=k.InitializePopulationPool(10, graph);
			
			int [] arr= {10,20};
			//k.init_BeliefSpace(arr,l);
			k.RunEpoch(graph, 20, arr, l);
			//l.print(l);
			System.out.println("Optimised Hamiltonian Route For TSP : "+l.head.chromosome+"->Cost:"+l.head.fitness);
		}
	}


