/*
* Vanilla C implementation of Approximal PairWise Genetic Sequencing Algorithm.(PSA);  /Non Penalty Model
*Non recursive dp model 
*@ author: B.Ajay
* @ date: 12-01-2023
*/

/*
*Implementation of the same in recursive version.(traceback function) 
*Implementation using pthreads /parellel processing(mpc in c/c++);
*Porting the code base   to c++;
*/


#include <stdio.h>

#include <stdlib.h>

#include <string.h>


/*Helper Function to Create nxm matrix  
* @param : int ,int
* @return :int** matrix .
Description  :malloc memory and fill the buffer with 0 and return the zero - matrix.
*/

int** CreateMatrix(int row,int col){
	int** align=(int **)malloc(row*sizeof(int*));


	for(int i=0;i<col;i++){
		*(align+i)=(int *)malloc((col)*sizeof(int));

	}


	for(int i=0;i<row;i++){
		for(int j=0;j<col;j++){
			*(*align+j)=0;
		}
	}

	return align ;

}
/*Helper Function to Find the max of three integers
*@param: int ,int ,int 
@returns: int

*/

int max(int a,int b,int c){

	if(a>=b && a>=c){//37 25 46
		return a;
	}
	else if(b>=c && b>=a){
	        return b;
	}
	else{
		return c;
	}

}
/*
*Function to Print Matrix with row  and col aligned with genome string sequences as index.
*@ param: int **,char*,char*
*@returns: NONE
*/

void print_matrix(int** arr,char* s1,char*s2){
	int row=(int)strlen(s1);
	int col=(int)strlen(s2);

	printf("\n");
	printf("\t\t");

	for(int i=0;i<strlen(s1);i++){
		printf("\t%c\t",*(s1+i));

	}
	printf("\n");
	for(int i=0;i<row;i++){

		printf("\t%c\t",*(s2+i));

		for(int j=0;j<col;j++){

			printf("\t%d\t",*(*(arr+i)+j));

		}

		printf("\n");

	}
	printf("\n");
}
/*Function to fill the Alignment Matrix 
*@param : int*,char*,char*,int ,int ;
*@returns: int**
*/
int**  fill_alignment_matrix(int*max_arr,char* sequence1,char* sequence2,int matchCost,int Mismatch_penalty){

	int row,col;
	int k=0;
	int up_neighbour,left_neighbour,diag_neighbour;
	int max1=0;
	int row_max, col_max;

	row=(int )strlen(sequence1);
	col=(int)strlen(sequence2);

	int** alignment_matrix=CreateMatrix(row,col);

	for(int i=1;i<=row-1;i++){

		for(int j=1;j<=col-1;j++){

			diag_neighbour=*(*(alignment_matrix+i-1)+j-1);
			left_neighbour=*(*(alignment_matrix+i)+j-1);
			up_neighbour=*(*(alignment_matrix+i-1)+j);

			if(sequence1[j]==sequence2[i]){

				*(*(alignment_matrix+i)+j)=diag_neighbour+2;
				//continue;

			}
			else{

				*(*(alignment_matrix+i)+j)=max(diag_neighbour,left_neighbour,up_neighbour)-1;

			}
			/*new*/if((*(*alignment_matrix+i)+j)>=max1){
				max1=(*(*alignment_matrix+i)+j);
				row_max=i;
				col_max=j;
				}

		}

	}
//	int* arr=(int*)malloc(3*sizeof(int));
	*(max_arr)=max1;
	*(max_arr+1)=row_max;
	*(max_arr+2)=col_max;
	//passer(arr);
	//print_matrix(alignment_matrix,sequence1,sequence2);
	return alignment_matrix;

}
/*Iterative Traceback Function based off max val of neighbours.
*@param:int**,int*,char*,char*
*@return:char**
*/

char** traceback(int** alignment_matrix ,int* max_arr ,char* sequence1,char*sequence2){

	int row=*(max_arr+1);
	int col=*(max_arr+2);
	char* possible_sequence=(char*)malloc((int)strlen(sequence1)*sizeof(char));
	int left_neighbour,up_neighbour,diag_neighbour;
	int max_val_trcbk=0;
	char* alignment_col=(char*)malloc(((int)strlen(sequence2))*sizeof(char));
	char* alignment_row=(char*)malloc(((int)strlen(sequence2))*sizeof(char));
	int i=row;
	int j=col;	
	int rs=0;
	int colc=0,rowc=0;
	int cs=0;
	char** align=(char **)malloc(2*sizeof(int*));


	for(int i=0;i<col;i++){
		*(align+i)=(char *)malloc((int)strlen(sequence1)*sizeof(int));

	}
	
	while(left_neighbour+up_neighbour+diag_neighbour!=0){	
		left_neighbour=*(*(alignment_matrix+i)+j-1);
		up_neighbour=*(*(alignment_matrix+i-1)+j);
		diag_neighbour=*(*(alignment_matrix+i-1)+j-1);
		max_val_trcbk=max(left_neighbour,up_neighbour,diag_neighbour);
		if(max_val_trcbk==diag_neighbour){

			*(alignment_col+colc++)=sequence1[j];
			*(alignment_row+rowc++)=sequence2[i];
			//printf("%c",sequence1[i]);
			//strcat(alignment_col,&sequence1[j]);
			//strcat(alignment_row,&sequence2[i]);
			rs+=i;
			cs+=j;
			i-=1;
			j-=1;
		}
		else if(max_val_trcbk==up_neighbour){
			alignment_col[colc++]=sequence1[j];
			*(alignment_row+rowc++)=sequence2[i];
			//printf("%c",sequence1[i]);
			
			//strcat(alignment_col,&sequence1[j]);
			//strcat(alignment_row,&sequence2[i]);
			rs+=i;
			i-=1;
		}
		else{
			alignment_col[colc++]=sequence1[j];
			*(alignment_row+rowc++)=sequence2[i];
			//printf("%c",sequence1[i]);
			
			//strcat(alignment_col,&sequence1[j]);
			//strcat(alignment_row,&sequence2[i]);
			rs+=j;
			j-=1;
		}
	}
	//printf("%s %s",alignment_row,alignment_col);
	//printf("row: %d col:%d rowsum %d colsum:%d" ,i,j,rs,cs);
	*(align)=alignment_row;
	*(align+1)=alignment_col;
	return align;

}
/*Helper Function to reverse the string .
*@param: char*
*@returns:char*
*/

char* reverse(char*a){
	int len=(int )strlen(a);
	char * ret=(char*)malloc(len*sizeof(char));
	int retc=0;
	for(int i=len-1;i>=0;i--){
		*(ret+retc++)=a[i];
	}
	return ret;
}


int main(){
//
	//int ** alignment_matrix=CreateMatrix(4,4);
	int k=0;
/*
	for(int i=1;i<=3;i++){
		for(int j=1;j<=3;j++){
			*(*(alignment_matrix+i)+j)=1;
			//alignment_matrix[i][j]=1;
		}
	}
*/

//	print_matrix(alignment_matrix,"_hii","_hom");

//String sequence
//char* seq1="_ACACACTA";

//char* seq2="_AGCACACA";
//char* seq1="_AAGT";
char* seq1="_ACACGTCGGATACATTACG";
char* seq2="_ACACGTCGATCCAATACGG";
//char* seq2="_AT";

/*Alloc mar array to pass to fill_matrix fn */
//int** mat=CreateMatrix(strlen(seq1),strlen(seq2));
//print_matrix(mat,seq1,seq2);

int *mar=(int* )malloc(3*sizeof(int));

int**	alignment_matrix=fill_alignment_matrix(mar,seq1,seq2,2,3);


print_matrix(alignment_matrix,seq1,seq2);

printf("\nSequence 1: ");

for(int i=0;i<strlen(seq1);i++){printf(" %c ",seq1[i]);}

printf("\nSequence 2: ");

for(int i=0;i<strlen(seq2);i++){printf(" %c ",seq2[i]);}

printf("\n");

//int* a=max_matrix(alignment_matrix,(int)strlen(seq1),(int)strlen(seq2));

for(int i=0;i<3;i++){

printf("%d\n",*(mar+i));

}

//printf("%d",*(*(alignment_matrix+8)+8));
char** align;//=(char*)malloc((int)strlen(seq1)*sizeof(char));

align=traceback(alignment_matrix,mar,seq1,seq2);

//for(int i=strlen(align)-1;i>=0;i--){
	printf("\nProbable Row Alignment:%s\n",reverse(align[0]));
	printf("\nProbable Column Alignment:%s\n",reverse(align[1]));
//}

}



