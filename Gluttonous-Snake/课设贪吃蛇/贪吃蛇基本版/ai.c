int dx[]={-1,0,1,0};
int dy[]={0,1,0,-1};
int way[5000][2];
int top=-1;
int flag;
typedef struct{
	int dis;
	int isVis;
	int condition;
}grid;
grid aimap[20][100];
typedef struct{
	int x;
	int y;
}point;
point start,end;
typedef struct{
	int front;
	int rear;
	point data[2000];
}Qs; 
void enQueue(Qs *s,point t){
	s->rear=(s->rear+1)%2000;
	s->data[s->rear]=t;
}
point deQueue(Qs *s){
	if(s->rear!=s->front){
		s->front=(s->front+1)%2000;
		return s->data[s->front];
	}
}
void get(int c,int i,int j){
	aimap[i][j].dis=-1;
	aimap[i][j].isVis=0;
	switch(c){
		case WALL:aimap[i][j].condition=WALL;break;
        case WAY:aimap[i][j].condition=WAY;break;
        case POISON:aimap[i][j].condition=WALL;break;
        case BOMB:aimap[i][j].condition=WALL;break;
        case FOOD:aimap[i][j].condition=END;end.x=i;end.y=j;break;
	}
}
int legal(int x,int y){
	if(x>=0&&x<20)
		if(y>=0&&y<100)
			return 1;
	else 
		return 0;
}
void BFS(point S){
	Qs Queue;
	Queue.front=Queue.rear=0;
	enQueue(&Queue,S);
	aimap[S.x][S.y].dis=0;
	aimap[S.x][S.y].isVis=1;
	int i;
	while(Queue.front!=Queue.rear){
		point u=deQueue(&Queue);
		for(i=0;i<4;i++){
			int cx=u.x+dx[i];
			int cy=u.y+dy[i];
			if(aimap[cx][cy].isVis!=1&&legal(cx,cy)==1){
				if(aimap[cx][cy].condition!=WALL&&aimap[cx][cy].condition!=START){
					aimap[cx][cy].isVis=1;
					aimap[cx][cy].dis=aimap[u.x][u.y].dis+1;
					point v;
					v.x=cx;
					v.y=cy;
					enQueue(&Queue,v);
				}
			}
		}
	}
}
void pathBFS(point v){
	if(aimap[v.x][v.y].condition==START&&flag==0){
		flag++;
		int i;
		for(i=top-1;i>=0;i--){
			struct Snake *p;
			p=(struct Snake*)malloc(sizeof(struct Snake));
			head->previous=p;
			p->next=head;
			p->x=way[i][0];
			p->y=way[i][1];
			head=p;
			gotoxy(head->x,head->y);
			printf("*");
			map[head->x][head->y]=WALL;
			tailcut();
			n++;
			Sleep(50);	
		}
		struct Snake *p;
		p=(struct Snake*)malloc(sizeof(struct Snake));
		head->previous=p;
		p->next=head;
		p->x=end.x;
		p->y=end.y;
		head=p;
		gotoxy(head->x,head->y);
		printf("*");
		n++;
		map[head->x][head->y]=WALL;
		score+=10;
		gotoxy(2,107);
		printf("%3d",score);
		length++;
		gotoxy(4,107);
		printf("%3d",length);
		printfood();
    }
	else{
		if(flag==0){
			int i;
			for(i=0;i<4;i++){
				point u;
				u.x=v.x+dx[i];
				u.y=v.y+dy[i];
				if(aimap[u.x][u.y].condition!=WALL&&legal(u.x,u.y)==1){
					if(aimap[u.x][u.y].dis==aimap[v.x][v.y].dis-1){
						top++;
						way[top][0]=u.x;
						way[top][1]=u.y;
						pathBFS(u);
						top--;
					}
				}
			}
	    }
	    else
	    	return;
	}
}
void autorun(void){
	int  n,m,t,x0,y0,x1,y1;
	if(twinklestate==0){
		for(t=0;t<poisonnumber;t++){
			gotoxy(poison[t][0],poison[t][1]);
			printf("-");
		}
		twinklestate=1;
	}
	for(t=0;t<2;t++){
		flag=0;
		for(n=0;n<20;n++)
			for(m=0;m<100;m++)
				get(map[n][m],n,m);
		aimap[head->x][head->y].condition=START;
		start.x=head->x;
		start.y=head->y;
		BFS(start);
		pathBFS(end);
		if(length>=20){
			gamestate=WIN;
			return;
		}
	}
	x0=head->x;y0=head->y;
	x1=head->next->x;y1=head->next->y;
	if(x0==x1-1)  direction=1;
	if(x0==x1+1)  direction=2;
	if(y0==y1-1)  direction=3;
	if(y0==y1+1)  direction=4;
}
