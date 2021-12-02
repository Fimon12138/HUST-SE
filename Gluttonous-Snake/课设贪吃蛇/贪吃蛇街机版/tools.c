void printspeed(void){
	if(dif>=30){
		gotoxy(5,156);
		printf(" full");
	}
	else{
		gotoxy(5,156);
		printf("%5d",dif);
	}
}
void priftool(void){
	srand(time(NULL));
	int x,y;
	do{
		x=1+rand()%29;
		y=1+rand()%149; 
	}while(map[x][y]!=0);
	gotoxy(x,y);
	printf("?");
	map[x][y]=TOOL;
	tool[0][0]=x;
	tool[0][1]=y;
	toolnums=1;
}
void eattool(void){
	map[tool[0][0]][tool[0][1]]=WAY;
	toolnums=0;
}
void cleartool(void){
	if(toolnums!=0){
		gotoxy(tool[0][0],tool[0][1]);
		printf(" ");
		map[tool[0][0]][tool[0][1]]=WAY;
		toolnums=0;
	}
}
void dotool(void){
	if(toolnums==0)
		priftool();
	else
		cleartool();
}
void printtoolname(int i,int j,int k){
	gotoxy(30,15*j);
	switch(i){
		case 1:{
			printf("     ");
			gotoxy(30,15*j);
			printf("*食物多多");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：食物多多");
			}
			break;
		}
		case 2:{
			printf("     ");
			gotoxy(30,15*j);
			printf("*双倍积分卡");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：双倍积分卡");
			}
			break;
		}
		case 3:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*减速卡");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：减速卡");
			}
			break;
		}
		case 4:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*变短卡");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：变短卡");
			}
			break;
		}
		case 5:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*双头蛇");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：双头蛇");
			}
			break;
		}
		case 6:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*戴上头盔");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：戴上头盔");
			}
			break;
		}
		case 7:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*清除毒草");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：清除毒草");
			}
			break;
		}
		case 8:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*清除地雷");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：清除地雷");
			}
			break;
		}
		case 9:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*清除墙壁");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：清除墙壁");
			}
			break;
		}
		case 10:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*全部清除");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：全部清除");
			}
			break;
		}
		case 11:{
			printf("             ");
			gotoxy(30,15*j);
			printf("*智慧草");
			if(k){
				gotoxy(36,10);
				printf("                                             ");
				gotoxy(36,10);
				printf("恭喜获得道具：智慧草");
			}
			break;
		}
	}
}
void movetoolnum(void){
	if(toolnumber==0)
		return;
	if(toolnum<toolnumber){
		gotoxy(31,15*toolnum);
		printf("  ");
		toolnum++;
		gotoxy(31,15*toolnum);
		printf("↑");
	}
	else{
		gotoxy(31,15*toolnum);
		printf("  ");
		toolnum=1;
		gotoxy(31,15*toolnum);
		printf("↑");
	}
}
void gettool(void){
	srand(time(NULL));
	int i,j;
	j=1+rand()%11;
	if(toolnumber==6){
		for(i=1;i<6;i++){
			tools[i-1]=tools[i];
			printtoolname(tools[i],i,0);
		}
		printtoolname(j,6,1);
		tools[5]=j;
	}
	else{
		toolnumber++;
		printtoolname(j,toolnumber,1);
		tools[toolnumber-1]=j;
		if(toolnumber==1)
			movetoolnum();
 	}
}
void printpoison(void){
	if(poisonnumber==0){
		int i,x,y;
		if(score/10>=100)
			poisonnumber=100;
		else
			poisonnumber=score/10;
		for(i=0;i<poisonnumber;i++){
			srand(time(NULL));
			do{
				x=1+rand()%28;
				y=1+rand()%148;
			}while(map[x][y]!=WAY);
			gotoxy(x,y);
			printf("-");
			poison[i][0]=x;
			poison[i][1]=y;
			map[x][y]=POISON;
		}
	}
}
void eatpoison(void){
	int i,j;
	for(i=0;i<poisonnumber;i++)
		if(poison[i][0]==head->x&&poison[i][1]==head->y)
			break;
	map[poison[i][0]][poison[i][1]]=WAY;
	for(j=i+1;j<poisonnumber;j++){
		poison[i][0]=poison[j][0];
		poison[i][1]=poison[j][1];
		i++;
	}
	poisonnumber--;
}
void clearpoison(void){
	int i;
	for(i=0;i<poisonnumber;i++){
		gotoxy(poison[i][0],poison[i][1]);
		printf("%c",'\0');
		map[poison[i][0]][poison[i][1]]=WAY;
	}
	poisonnumber=0;
}
void dopoison(void){
	if(poisonnumber==0)
		printpoison();
	else
		clearpoison();	
}//毒草 
void printbomb(void){
	if(bombnumber==0){
		int i,x,y;
		if(score/10>=100)
			bombnumber=100;
		else
			bombnumber=score/10;
		for(i=0;i<bombnumber;i++){
			srand(time(NULL));
			do{
				x=1+rand()%18;
				y=1+rand()%98;
			}while(map[x][y]!=WAY);
			gotoxy(x,y);
			printf("@");
			bomb[i][0]=x;
			bomb[i][1]=y;
			map[x][y]=BOMB;
		}
	}
}
void eatbomb(void){
	int i,j;
	for(i=0;i<bombnumber;i++)
		if(bomb[i][0]==head->x&&bomb[i][1]==head->y)
			break;
	map[bomb[i][0]][bomb[i][1]]=WAY;
	for(j=i+1;j<bombnumber;j++){
		bomb[i][0]=bomb[j][0];
		bomb[i][1]=bomb[j][1];
		i++;
	}
	bombnumber--;
}
void clearbomb(void){
	int i;
	for(i=0;i<bombnumber;i++){
		gotoxy(bomb[i][0],bomb[i][1]);
		printf("%c",'\0');
		map[bomb[i][0]][bomb[i][1]]=WAY;
	}
	bombnumber=0;
}
void dobomb(void){
	if(bombnumber==0)
		printbomb();
	else
		clearbomb();
}//地雷 
void printwall(void){
	if(wallnumber<10){
		int i,x,y;
		srand(time(NULL));
		for(i=wallnumber;i<wallnumber+5;i++){
			do{
				x=1+rand()%28;
				y=1+rand()%148;
			}while(map[x][y]!=WAY);
			gotoxy(x,y);
			printf("#");
			map[x][y]=WALL;
			wall[i][0]=x;
			wall[i][1]=y;
		}
		wallnumber+=5;
	}
}
void clearwall(void){
	int i;
	for(i=0;i<wallnumber;i++){
		gotoxy(wall[i][0],wall[i][1]);
		printf(" ");
		map[wall[i][0]][wall[i][1]]=WAY;
	}
	wallnumber=0;
}//墙
void clearall(void){
	clearwall();
	clearpoison();
	clearbomb();
}//全清 
void printfood(void){
	srand(time(NULL));
	int x,y;
	do{
		y=1+rand()%148;
	    x=1+rand()%28;
	}while(map[x][y]!=0);
	gotoxy(x,y);
	printf("+");
	map[x][y]=FOOD;
	food[0][0]=x;
	food[0][1]=y;
	foodnumber++;
}
void muchfood(void){
	clearall();
	cleartool();
	srand(time(NULL));
	int x,y,i;
	for(i=1;i<100;i++){
		do{
			y=1+rand()%148;
			x=1+rand()%28;
		}while(map[x][y]!=0);
		gotoxy(x,y);
		printf("+");
		map[x][y]=FOOD;
		food[i][0]=x;
		food[i][1]=y;
	}
	foodnumber=100;
	foodstate=1;
}
void eatfood(void){
	int i,j;
	for(i=0;i<foodnumber;i++)
		if(food[i][0]==head->x&&food[i][1]==head->y)
			break;
	map[food[i][0]][food[i][1]]=WAY;
	for(j=i+1;j<foodnumber;j++){
		food[i][0]=food[j][0];
		food[i][1]=food[j][1];
		i++;
	}
	foodnumber--;
}
void clearfood(void){
	int i;
	for(i=1;i<foodnumber;i++){
		gotoxy(food[i][0],food[i][1]);
		printf("%c",'\0');
		map[food[i][0]][food[i][1]]=WAY;
	}
	foodnumber=1;
}//食物 
void lengthcut(void){
	if((length/2)<=0){
		gotoxy(36,6);
		printf("无法使用此道具!");
	}
	else{
		tailcut();
		int i;
		for(i=length;i>length/2;i--)
			tailcut();
		length=length/2;
		gotoxy(3,156);
		printf("%5d",length);
	}
}//道具/蛇身变短/函数
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
grid aimap[30][150];
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
	if(x>=0&&x<30)
		if(y>=0&&y<150)
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
		eatfood();
		n++;
		map[head->x][head->y]=WALL;
		if(doublescore)	score+=20;
		else	score+=10;
		gotoxy(1,156);
		printf("%5d",score);
		length++;
		gotoxy(3,156);
		printf("%5d",length);
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
	for(t=0;t<3;t++){
		flag=0;
		for(n=0;n<30;n++)
			for(m=0;m<150;m++)
				get(map[n][m],n,m);
		aimap[head->x][head->y].condition=START;
		start.x=head->x;
		start.y=head->y;
		BFS(start);
		pathBFS(end);
	}
	x0=head->x;y0=head->y;
	x1=head->next->x;y1=head->next->y;
	if(x0==x1-1)  direction=1;
	if(x0==x1+1)  direction=2;
	if(y0==y1-1)  direction=3;
	if(y0==y1+1)  direction=4;
}//道具/智慧草/函数 
void usetool(void){
	if(toolnum==0){
		gotoxy(36,10);
		printf("                                             ");
		gotoxy(36,10);
		printf("暂无道具可用");
		return;
	}
	switch(tools[toolnum-1]){
		case 1:{
			muchfood();
			foodstate=1;
			n2=n;
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：食物多多");
			break;
		}
		case 2:{
			doublescore=1;
			n3=n;
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：双倍积分卡");
			break;
		}
		case 3:{
			dif=10;
			printspeed();
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：减速卡");
			break;
		}
		case 4:{
			lengthcut();
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：变短卡");
			break;
		}
		case 5:{
			twohead=1;
			n1=n;
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：双头蛇");
			break;
		}
		case 6:{
			headprotect=1;
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：戴上头盔");
			break;
		}
		case 7:{
			clearpoison();
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：清除毒草");
			break;
		}
		case 8:{
			clearbomb();
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：清除地雷");
			break;
		}
		case 9:{
			clearwall();
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：清除墙壁");
			break;
		}
		case 10:{
			clearall();
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：全部清除");
			break;
		}
		case 11:{
			autorun();
			gotoxy(36,10);
			printf("                                          ");
			gotoxy(36,10);
			printf("使用道具：智慧草");
			break;
		}
	}
	int i;
	for(i=toolnum;i<=toolnumber;i++){
		tools[i-1]=tools[i];
		printtoolname(tools[i],i,0);
	}
	gotoxy(30,15*toolnumber);
	printf("             ");
	toolnumber--;
	if(toolnumber==0){
		gotoxy(31,15*toolnum);
		printf("  ");
		toolnum=0;
	}
	else{
		gotoxy(31,15*toolnum);
		printf("  ");
		toolnum=1;
		gotoxy(31,15*toolnum);
		printf("↑");
	}
}
void printchange(void){
	if(changenumber<1){
		srand(time(NULL));
		int x,y;
		do{
			x=1+rand()%28;
			y=1+rand()%148;
		}while(map[x][y]!=0);
		gotoxy(x,y);
		printf("&");
		map[x][y]=CHANGE;
		changenumber=1;
	}
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
