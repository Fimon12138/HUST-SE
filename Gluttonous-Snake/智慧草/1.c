#include<stdio.h>
#include<stdlib.h>
#include<windows.h>

#define maxsize 5000
#define START 1
#define END 2
#define WAY 3
#define WALL 4

int  n,m;
int dx[]={-1,0,1,0};
int dy[]={0,1,0,-1};
int way[5000][2];
int top=-1;
int count=0;
int isVis[1000][1000];

void gotoxy(int x,int y)
{
   HANDLE hOut;
   COORD coord={y,x};
   hOut=GetStdHandle(STD_OUTPUT_HANDLE);
   SetConsoleCursorPosition(hOut,coord);
}

typedef struct{
	int dis;
	int isVis;
	int condition;
}grid;
grid map[1000][1000];

typedef struct{
	int x;
	int y;
}point;
point start,end;

typedef struct{
	int front;
	int rear;
	point data[maxsize];
}Qs; 

void enQueue(Qs *s,point t){
	s->rear=(s->rear+1)%maxsize;
	s->data[s->rear]=t;
}

point deQueue(Qs *s){
	if(s->rear!=s->front){
		s->front=(s->front+1)%maxsize;
		return s->data[s->front];
	}
}

void get(char c,int i,int j){
	map[i][j].dis=-1;
	map[i][j].isVis=0;
	switch(c){
		case '1':map[i][j].condition=WALL;break;
        case '0':map[i][j].condition=WAY;break;
        case 'S':map[i][j].condition=START;start.x=i;start.y=j;break;
        case 'E':map[i][j].condition=END;end.x=i;end.y=j;break;
	}
}

int legal(int x,int y){
	if(x>=0&&x<n)
		if(y>=0&&y<m)
			return 1;
	else 
		return 0;
}



















