package Exercise9;

import java.util.ArrayList;
import java.util.List;

class BellmanFord{
	 int [][] adjmat;
	 int source,len;
	 int inf=45558;
	
	BellmanFord(int[][]adjmat,int arrlen,int source){
		this.adjmat=adjmat;
		this.source=source;
		len=arrlen;
	}
	List <Integer>vector=new ArrayList <Integer>(len);

	
	 void initialize() {
		 for(int i=0;i<len;i++) {
				
				vector.add(adjmat[source][i]);
			}
		 
		
	}
	 void iterate() {
		 initialize();
		 int cur;
		 int j=(source+1)%len;
		 
		  int [][] arr1=new int[len-1][len];
		  int row=0;
		 	while(j!=source) {
		 		cur=vector.get(j);
		 		for(int k=0;k<len;k++) {
		 			arr1[row][k]=adjmat[j][k];
		 		
		 		}
		 		row++;
		 		if(row==len-1) {break;}
		 		
		 		j++;
		 		j=j%4;
		 	}
		 	for(int i=0;i<len-1;i++) {
		 		for(int m=0;m<len;m++) {
		 			//System.out.print(arr1[i][m]+" ");
		 		}
		 		//System.out.println();
		 	}
		 	
		 	j=(source+1)%len;
		 	
		 	
		 	for(int i=0;i<len-1;i++) {
		 		cur=vector.get(j);
		 		for(int k=0;k<len;k++) {
		 			if(Math.abs(cur+arr1[i][k])<vector.get(k)) {
		 				
						vector.set(k, (cur+arr1[i][k]));
					}
		 		}
		 		
		 				
		 		j++;
		 		j=j%len;
		 	}
		 	
		 	
	}
	void printVector() {
		for(int i=0;i<len;i++) {
			System.out.print(vector.get(i)+"\t");
		}
		System.out.println();
	}
	void PrintRouterVectorTable(int[][]mat,int arrlen,int src) {
		for(int i=0;i<len;i++) {
			System.out.println(i+" Routing Table: ");
			this.source=i;
			vector=new ArrayList <Integer>(len);
			iterate();
			printVector();
			System.out.println("------------------");
		}
	}
	
}
public class bellmanford {
	 
	
	public static void main(String []args) {
		int inf=45556;
		int [][] adjacencyMatrix= {{0,2,inf,1},
					   {2,0,3,7},
					   {inf,3,0,11},
					   {1,7,11,0}};
		
		
		BellmanFord f= new BellmanFord(adjacencyMatrix,4,0);
		f.PrintRouterVectorTable(adjacencyMatrix,4,0);
		
		
	}
	
}
