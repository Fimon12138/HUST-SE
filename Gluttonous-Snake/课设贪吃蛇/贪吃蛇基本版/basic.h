#include<stdio.h>
#include<stdlib.h>
#include<windows.h>
#include<conio.h>
#include<time.h>

//��ֵ // 
#define up 72
#define down 80
#define left 75
#define right 77
#define esc 27
#define enter 13
#define q 113
//��ֵ//

//��ͼ���� 
#define WAY 0
#define WALL 1
#define FOOD 2
#define POISON 3
#define BOMB 4
#define AI 5
#define END 6
#define START 7
//��ͼ���� 
//��Ϸ״̬���� 
#define FAIL 1
#define GO 2
#define WIN 3
#define SAVE 4
#define NONE 5
#define FINISH 6
#define NEWGO 7
//��Ϸ״̬���� 
//ȫ�ֱ��� 
struct Snake *head,*tail;//��ͷ����β 
int map[20][100];//��ͼ 
int direction;//�����з���1.���ϣ�2.���£�3.����4.���� 
int score;//��Ϸ�÷� ����ʼΪ0 
int length;//�߳� ����ʼΪ3 
int poisonnumber,poison[40][2],twinklestate,n0;//���� 
int bombnumber,bomb[40][2]={0};//����
int ainumber,ai[1][1];//�ǻ۲� 
int gamestate;//��Ϸ״̬
int m;//�ؿ���
int difficult;//�Ѷ�ϵ��
int n;//�Ʋ�����ʱ����
//ȫ�ֱ��� 

//�߽ṹ�� 
struct Snake{
	int x;
	int y;
	struct Snake *next;
	struct Snake *previous; 
};
//�߽ṹ��

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
void StartSnake(int n){
	struct Snake *p1,*p2;
	int x0=1,y0;
	int i;
	for(i=0;i<100;i++)
		if(map[1][i]==1)
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
void printfood(void){
	srand(time(NULL));
	int x,y;
	do{
		y=1+rand()%98;
	    x=1+rand()%18;
	}while(map[x][y]!=0);
	gotoxy(x,y);
	printf("+");
	map[x][y]=FOOD;
}
int speed(void){
	int timeinterval;
	timeinterval=(300-15*difficult)-10*m;
	if(timeinterval>100)
		return timeinterval;
	else
		return 100;
}

