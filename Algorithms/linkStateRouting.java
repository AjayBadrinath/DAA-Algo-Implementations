package Exercise9;
import java.util.*;
class dijkstra{
	int mat[][];
	int nodes,node_no=0,n_nodes;
		// Node no , isVisited, Distance , prev
	dijkstra(int n_nodes,int[][] mat){
		this.mat=mat;
		this.n_nodes=n_nodes;
	}
	ArrayList <Integer> sl=new ArrayList<Integer>(n_nodes*n_nodes);
	int k=0;
	void set() {
		sl.add(k++,node_no++ );
		sl.add(k++, 0);
		sl.add(k++, 0);
		sl.add(k++, null);
		for(int i=1;i<n_nodes;i++) {
			sl.add(k++,node_no++ );
			sl.add(k++, 0);
			sl.add(k++, Integer.MAX_VALUE);
			sl.add(k++, null);
		}
		
	}
	void print() {
		for(int i=0;i<sl.size();i+=4) {
			//System.out.println(i);
			System.out.println(sl.subList(i, i+4));
		}  
	}
	
	int findmin(ArrayList <Integer> k) {
		int min=k.subList(4, 8).get(2),idx=k.subList(4, 8).get(0);
		
		for(int i=4;i<mat.length*4;i+=4) {
			//System.out.println(k.subList(i,i+4).get(2)+"====");
			
			if(k.subList(i,i+4).get(2)<=min && (k.subList(i,i+4).get(1))==0) {
			
				min=k.subList(i,i+4).get(2);
				idx=k.subList(i, i+4).get(0);
			}
		}
		//System.out.println(min+"  "+idx);
		return idx;
	}
	void run() {
		int cur;
		int iter=0;
		
		int k=0,m,i,cost=0;
		for(int j=0;j<mat.length;j++) {
			i=j*4;
			if(mat[k][j]>0) {
				if(sl.subList(i,i+4).get(2)==Integer.MAX_VALUE) {
					sl.subList(i, i+4).set(2, mat[k][j]);
					sl.subList(i, i+4).set(3, 0);
				} 
			}
		}
		sl.subList(0, 4).set(1, 1);
		int listidx,precost,pre,collst,tmp;
		while(!isVisited()) {
			i=findmin(sl);
			//print();
			listidx=i*4;
			precost=sl.subList(listidx,listidx+4).get(3);
			cost=sl.subList(precost*4,(precost*4)+4).get(2);
			//cost=sl.subList(listidx,listidx+4).get();
			for(int c=0;c<mat.length;c++) {
				if(mat[i][c]>0) {
					collst=c*4;
					 if (cost+mat[i][c]<sl.subList(collst,collst+4).get(2)) {
						 tmp=sl.subList(collst,collst+4).get(2);
						sl.subList(collst, collst+4).set(2, cost+mat[i][c]);
						sl.subList(collst,collst+4).set(3, i);
					}
				}
			}
			sl.subList(listidx, listidx+4).set(1, 1);
		}
		
	
	}
	boolean isVisited() {
		
		for(int i=0;i<mat.length;i+=4) {
			if(sl.subList(i, i+4).get(1)==0) {
				return false;
			}
		}
		return true;
	}
	void RouteConfigPrint(int targetNode) {
		int listidx=targetNode;
		listidx*=4;
		int cost=0;
		System.out.print("0");
		while(sl.subList(listidx, listidx+4).get(0)!=0) {
			cost+=sl.subList(listidx, listidx+4).get(2);
			System.out.print("-->"+sl.subList(listidx, listidx+4).get(0));
			listidx=sl.subList(listidx, listidx+4).get(3);
			listidx*=4;
		}
		System.out.print("=" +cost);
		cost=0;
	}	
}
public class linkStateRouting {
	public static void main(String []  args) {
		
		
		int [][]adjmat=	{ { 0, 0, 1, 2, 0, 0, 0 }, 
				{ 0, 0, 2, 0, 0, 3, 0 },
				{ 1, 2, 0, 1, 3, 0, 0 },
		        { 2, 0, 1, 0, 0, 0, 1 },
		        { 0, 0, 3, 0, 0, 2, 0 }, 
		        { 0, 3, 0, 0, 2, 0, 1 },
		        { 0, 0, 0, 1, 0, 1, 0 } };
		
		dijkstra s=new dijkstra(7,adjmat);
		s.set();
		//s.findmin(s.sl);
		s.run();
		
		s.print();
		for(int i=1;i<7;i++) {
			System.out.println();
			s.RouteConfigPrint(i);
	}
		
	}
}
