#include<stdio.h>
#include<stdlib.h>
#include<windows.h>
#include<conio.h>
#include<time.h>
#include<string.h>

//键值 // 
#define up 72
#define down 80
#define left 75
#define right 77
#define esc 27
#define enter 13
#define q 113
#define z 122
#define _x 120
//键值//

//场景
#define lawn 1
#define forest 2
#define ice 3
#define sea 4
//场景
 
//地图参数 
#define WAY 0
#define WALL 1
#define FOOD 2
#define POISON 3
#define BOMB 4
#define TOOL 5
#define END 6
#define START 7
#define CWALL 8
#define CHANGE 9
//地图参数 

//游戏状态参量 
#define FAIL 1
#define GO 2
#define WIN 3
#define SAVE 4
#define NONE 5
#define FINISH 6
#define NEWGO 7
//游戏状态参量 

//全局变量
struct Snake *head,*tail;
int map[30][150];
int direction,twohead;
int score,score0;
int length;
int wallnumber,wall[100][2];
int poisonnumber,poison[100][2];
int bombnumber,bomb[100][2];
int foodnumber,foodstate,doublescore,food[100][2];
int dif;//速度控制 
int headprotect;//道具/头盔/参数 
int tools[6],toolnum,toolnumber,tool[1][2],toolnums;//道具/存放栏/参数 
int gamestate,state,changenumber;
int background; 
long long int n,n1/*doublehead*/,n2/*muchfood*/,n3/*doublescore*/;
//全局变量 

void gotoxy(int x,int y)
{
   HANDLE hOut;
   COORD coord={y,x};
   hOut=GetStdHandle(STD_OUTPUT_HANDLE);
   SetConsoleCursorPosition(hOut,coord);
}
void HideCursor(void){
	CONSOLE_CURSOR_INFO cursor_info={1,0};
	SetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE),&cursor_info);
}
void PrintCursor(void){
	CONSOLE_CURSOR_INFO cursor_info={1,1};
	SetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE),&cursor_info);
}
struct Snake{
	int x;
	int y;
	struct Snake *next;
	struct Snake *previous; 
};
void StartSnake(int n){
	struct Snake *p1,*p2;
	int x0=1,y0;
	int i;
	for(i=0;i<150;i++)
		if(map[1][i]!=0)
			break;
	y0=i+1; 
	for(i=1;i<=n;i++){
		p1=(struct Snake*)malloc(sizeof(struct Snake));
		p1->x=x0;p1->y=y0;
		if(i==1){
			p2=tail=p1;
			tail->next=NULL;
		}
		else{
			p2->previous=p1;
			p1->next=p2;
			p2=p1;
			if(i==n)  head=p1;
		}
		y0++;
	}
	p1=head;
	while(p1!=NULL){
		gotoxy(p1->x,p1->y);
		printf("%c",42);
		map[p1->x][p1->y]=WALL;
		p1=p1->next;
	} 
} 
void headadd(void){
	struct Snake *p;
	p=(struct Snake*)malloc(sizeof(struct Snake));
	head->previous=p;
	p->next=head;
	switch(direction){
		case 1:p->x=head->x-1;p->y=head->y;break;
		case 2:p->x=head->x+1;p->y=head->y;break;
		case 3:p->x=head->x;p->y=head->y-1;break;
		case 4:p->x=head->x;p->y=head->y+1;break;
		default:break;
	}
	head=p;
	gotoxy(head->x,head->y);
	printf("*");
}
void tailcut(void){
	struct Snake *p=tail;
	tail=tail->previous;
	tail->next=NULL;
	gotoxy(p->x,p->y);
	printf("%c",'\0');
	map[p->x][p->y]=WAY;
	free(p);
}
void mergehead(void){
	struct Snake *p1,*p2,*p;
	tail=p1=p2=head;
	while(p1!=NULL){
		if(p1==head)
			p1=head->next;
		p=p1->next;
		p1->next=p2;
		p2->previous=p1;
		p2=p1;
		p1=p;
	}
	head=p2;
	tail->next=NULL;
	int x0,y0,x1,y1;
	x0=head->x;
	y0=head->y;
	x1=head->next->x;
	y1=head->next->y;
	if(x0==x1-1)  direction=1;
	if(x0==x1+1)  direction=2;
	if(y0==y1-1)  direction=3;
	if(y0==y1+1)  direction=4;
}//道具/双头蛇/函数 
void moveup(void){
	if(direction!=2){
		struct Snake *p;
		p=(struct Snake*)malloc(sizeof(struct Snake));
		p->x=head->x-1;
		p->y=head->y;
		p->next=head;
		head->previous=p;
		head=p;
		gotoxy(head->x,head->y);
		printf("%c",42);
		direction=1;
	}
	else{
		if(twohead==1){
			mergehead();
			headadd();
		}
		else
			headadd();
	} 
}
void movedown(void){
	if(direction!=1){
		struct Snake *p;
		p=(struct Snake*)malloc(sizeof(struct Snake));
		p->x=head->x+1;
		p->y=head->y;
		p->next=head;
		head->previous=p;
		head=p;
		gotoxy(head->x,head->y);
		printf("%c",42);
		direction=2;
    }
    else{
		if(twohead==1){
			mergehead();
			headadd();
		}
		else
			headadd();
	} 
}
void moveleft(void){
	if(direction!=4){
		struct Snake *p;
		p=(struct Snake*)malloc(sizeof(struct Snake));
		p->x=head->x;
		p->y=head->y-1;
		p->next=head;
		head->previous=p;
		head=p;
		gotoxy(head->x,head->y);
		printf("%c",42);
		direction=3;
	}
	else{
		if(twohead==1){
			mergehead();
			headadd();
		}
		else
			headadd();
	} 
}
void moveright(void){
	if(direction!=3){
		struct Snake *p;
		p=(struct Snake*)malloc(sizeof(struct Snake));
		p->x=head->x;
		p->y=head->y+1;
		p->next=head;
		head->previous=p;
		head=p;
		gotoxy(head->x,head->y);
		printf("%c",42);
		direction=4;
	}
	else{
		if(twohead==1){
			mergehead();
			headadd();
		}
		else
			headadd();
	} 
}
