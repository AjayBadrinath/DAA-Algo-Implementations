package generics;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
class RandomWalk{
	int n_ants,src,dst;
	int [][]arr;
	String genepool="";
	double [][]vapmat;
	double[][]antprob;
	RandomWalk(int n_ants,int [][] arr,int src,int dst){
		this.n_ants=n_ants;
		this.arr=arr;
		this.src=src;
		this.dst=dst;
		vapmat=new double[arr.length][arr.length];
		antprob=new double[arr.length][arr.length];
		
	}
	
	public  void SetGene(int[][]arr) {
		for(int i=0;i<arr.length;i++) {
			genepool+=((i+1));
		}
	}
	boolean checkVis(String chromosome,char s) {
		for(int i=0;i<chromosome.length();i++) {
			if(chromosome.charAt(i)==s) {
				return true;
			}
		}
		return false;
	}
	public  String  genStr() {
		Random r=new Random();
		String chromosome ="";
		//char start=genepool.charAt(Math.abs(r.nextInt()%genepool.length()));
		char start='1';
		chromosome+=start;
		char tmp;
		
		
		while(chromosome.length()<arr.length) {
			tmp=genepool.charAt(Math.abs(r.nextInt()%genepool.length()));
			if (checkVis(chromosome,tmp)==false) {
				chromosome+=tmp;
			}
		}
		chromosome+=start;
		return chromosome;
	}
	double ComputeCost(int [][] arr,String sequence) {
		//System.out.println('k');
		int cost=0;
		for(int i=0;i<sequence.length()-1;i++) {
			cost+=arr[sequence.charAt(i)-'0'-1][sequence.charAt(i+1)-'0'-1];
		}
		Double k= ((double)1/(double)cost);
		return BigDecimal.valueOf(k).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	double[][] overlayMatrix(String path,double cost,double[][]arr1){
		int x,x1;
		double [][] arr=arr1;
		for(int i=0;i<path.length()-1;i++) {
			 x=path.charAt(i)-'0';
			 //System.out.println(x+1);
			 x1=path.charAt(i+1)-'0';
			 arr[x-1][x1-1]+=cost;
			 arr[x1-1][x-1]+=cost;
		}
		return arr;
	}
	double[][] init(){
		
		RandomWalk r=new RandomWalk(10,arr,0,2);
		r.SetGene(arr);
		
		double[][]x;
		String t="";
		
		for(int i=0;i<1;i++) {
		t=r.genStr();
		System.out.println(t);
		System.out.println(r.ComputeCost(arr, t));
		System.out.println("------");
		}
		double [][] arr1=new double[t.length()-1][t.length()-1];
		
		x=r.overlayMatrix(t,r.ComputeCost(arr, t),arr1);
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x.length;j++) {
				System.out.print(x[i][j]+"\t");
			}
			System.out.println();
		}
		for(int i=0;i<1;i++) {
			t=r.genStr();
			System.out.println(t);
			x=r.overlayMatrix(t,r.ComputeCost(arr, t),x);
			System.out.println(r.ComputeCost(arr, t));
			System.out.println("------");
			}
		
		
		for(int i=0;i<x.length;i++) {
			for(int j=0;j<x.length;j++) {
				System.out.print(x[i][j]+"\t");
			}
			System.out.println();
		}
		//System.out.println(r.genStr());
		return x;
	}
	double CalcPheromoneDensity(double vaporisationConst,double init_tau,double sum_tau) {
		return (((1-vaporisationConst)*init_tau)+sum_tau);
	}
	
	void CalcAntProb(double[][]overlaymat,double alpha,double beta) {
		double sum=0.0;
	
		for(int i=0;i<overlaymat.length;i++) {
			
			for(int k=0;k<overlaymat.length;k++) {
				if(arr[i][k]==0) {continue;}
				//System.out.println(1/(double)arr[i][k]);
				sum+=Math.pow(vapmat[i][k],alpha)*Math.pow(1/(double)arr[i][k], beta);
			}
			for(int j=0;j<overlaymat.length;j++) {
				if(arr[i][j]==0) {continue;}
				antprob[i][j]=BigDecimal.valueOf((Math.pow(vapmat[i][j],alpha)*Math.pow(1/(double)arr[i][j],beta))/sum).setScale(3, RoundingMode.HALF_UP).doubleValue();
			}
			sum=0.0;
		}
		System.out.println("======ProbMatrix======");
		
		for(int i=0;i<overlaymat.length;i++) {
			for(int j=0;j<overlaymat.length;j++) {
				System.out.print(antprob[i][j]+"\t");
			}
			System.out.println();
		}
		
		
	}
	boolean isVisited(int[]nodes,int node) {
		for(int u=0;u<nodes.length;u++) {
			if(node==nodes[u]) {
				return true;
			}
		}
		return false;
	}
	
	double[][] setvap_mat(double vapConst,double [][]overlay,double[][]edgemat,int node) {
		
		for(int i=0;i<overlay.length;i++) {
			for(int j=0;j<overlay.length;j++) {
				
			vapmat[i][j]=BigDecimal.valueOf(CalcPheromoneDensity(vapConst,edgemat[i][j],overlay[i][j])).setScale(3, RoundingMode.HALF_UP).doubleValue();
			edgemat[i][j]=vapmat[i][j];
			}
		}
		System.out.println("======");
		for(int i=0;i<overlay.length;i++) {
			for(int j=0;j<overlay.length;j++) {
				System.out.print(vapmat[i][j]+"\t");
			}
			System.out.println();
		}
		return edgemat;
	}
	int max(int node,int startidx) {
		double max=0.0;
		int idx=0;
		for(int i=startidx;i<arr.length;i++) {
			if(antprob[node][i]>max) {
				max=antprob[node][i];
				idx=i;
			}
		}
		return idx;
	}
	/*I am not simulating n ants but based off max of rows we route ....Solves in the 1st iteration itself*/
	void ACO() {
		int[] nodes=new int[arr.length];
		
		int tn=0,vis=0,i=0,k=arr.length,j=0;
		nodes[vis++]=0;
		while(vis<arr.length) {
			tn=max(tn,j%arr.length);
			if(isVisited(nodes,tn)==false) {
				nodes[vis++]=tn;
				System.out.println(tn);
				i++;
				j=0;
				continue;
			}
			j++;
		}
		
		for(int l=0;l<arr.length;l++) {
			System.out.println(nodes[l]);
		}
		System.out.println(isVisited(nodes,max(0,0)));
	}
}


public class AntColonyOptimization {
	public static void main(String [] args) {
		
		
		int [][]arr= {
				{0,4,15,1},
				{4,0,5,8},
				{15,5,0,4},
				{1,8,4,0}
		};
		/*
		int [][] arr= {
				{0,3,5,7},
				{3,0,2,6},
				{5,2,0,4},
				{7,6,4,0}
		};
		
		*/
		RandomWalk r=new RandomWalk(10,arr,0,2);
		double[][] k=r.init();
		
		double[][]init={
				{0,1,1,1},
				{1,0,1,1},
				{1,1,0,1},
				{1,1,1,0}
		};
		int tmp;
		
		 init=r.setvap_mat(0.5, k, init, 0);
		 int tn=0;
		for(int i=0;i<k.length;i++) {
			r.CalcAntProb(k, 0.5, 100.0);
			tmp=r.max(tn, 0);
			System.out.println("[][][][]"+tmp);
			r.vapmat[tmp][tn]=0.0;
			tn=tmp;
		
		}
		r.CalcAntProb(k, 0.5, 100.0);
		//r.ACO();
		System.out.println("Path: 0->");
		for(int i=0;i<r.vapmat.length;i++) {
			for(int j=0;j<r.vapmat.length;j++) {
				if(r.antprob[i][j]==1.0) {
					System.out.print(j+1+"->");
				}
			}
		}
	}
	
	//for multiple iteration update pheromone matrix and thereby update prob mat that will increase confidence of each idx and max(idx ) for each row will be the path  
	
	
}
