int speed(void){
	int timeint;
	timeint=500-15*dif;
	if(timeint>50)
		return timeint;
	else
		return 50;
}
void getmap(int n){
	FILE *fp;
	char ch;
	int i,j;
	if((fp=fopen("maps.dat","r"))==NULL){
		printf("地图错误!");
		exit(0);
	}
	rewind(fp);
	fseek(fp,4500*(n-1),0);
	for(i=0;i<30;i++){
		for(j=0;j<150;j++){
			ch=fgetc(fp);
			switch(ch){
				case '1':printf("#");map[i][j]=WALL;break;
				case '0':printf(" ");map[i][j]=WAY;break;
				case '3':printf("-");map[i][j]=CWALL;break;
				case '4':printf("|");map[i][j]=CWALL;break;
				default:break;
			}
		}
		printf("\n");
	}
	fclose(fp);
	if(state==0){
		gotoxy(1,151);
		printf("得分:%5d",score);
		gotoxy(3,151);
		printf("长度:%5d",length);
		gotoxy(5,151);
		printf("速度:");
		printspeed();
		gotoxy(7,151);
		printf("场景:");
		gotoxy(30,0);
		printf("道具:");
		gotoxy(36,0);
		printf("小喇叭:");
	}
}

void new(void){
	if(state==0){
		direction=4;score0=score=0;length=5;twohead=0;
		poisonnumber=0;bombnumber=0;foodnumber=0;
		doublescore=0;foodstate=0;dif=1;wallnumber=0;
		headprotect=0;toolnum=toolnumber=toolnums=0;
		background=lawn;n=0;changenumber=0;
		getmap(background);
		StartSnake(5);
		printfood();
	}
	else{
		poisonnumber=bombnumber=foodnumber=0;
		wallnumber=0;changenumber=0;
		score0=score;
		int i,j;
		for(i=0;i<30;i++){
			for(j=0;j<150;j++){
				gotoxy(i,j);
				printf(" ");
				map[i][j]=WAY;
			}
		}
		gotoxy(0,0);
		getmap(background);
		struct Snake *p;
		p=head;
		while(p!=NULL){
			gotoxy(head->x,head->y);
			printf("*");
			map[head->x][head->y]=WALL;
			p=p->next;
		}
		printfood();
	}
}
void check(void){
	switch(map[head->x][head->y]){
		case WAY:{
			map[head->x][head->y]=WALL;
			tailcut();
			break;
		}
		case WALL:{
			if(headprotect==1){
				mergehead();
				headprotect=0;
				int x0,y0;
				x0=tail->x;
				y0=tail->y;
				headadd();
				tailcut();
				gotoxy(x0,y0);
				printf("#");
				gotoxy(36,10);
				printf("                                           ");
				gotoxy(36,10);
				printf("道具已失效：戴上头盔"); 
			}
			else
				gamestate=FAIL;
			break;
		}
		case FOOD:{
			eatfood();
			map[head->x][head->y]=WALL;
			dif++;
			printspeed();
			if(doublescore)	score+=20;
			else	score+=10;
			gotoxy(1,156);
			printf("%5d",score);
			length++;
			gotoxy(3,156);
			printf("%5d",length);
			if(foodstate==0)
				printfood();
			break;
		}
		case POISON:{
			eatpoison();
			map[head->x][head->y]=WALL;
			score-=10;
			length--;
			if(length<=0)
				gamestate=FAIL;
			else{
				tailcut();
				tailcut();
				gotoxy(1,156);
				printf("%5d",score);
				gotoxy(3,156);
				printf("%5d",length);
			}
			break;
		}
		case BOMB:{
			eatbomb();
			map[head->x][head->y]=WALL;
			score-=20;
			if((length/2)<=0)
				gamestate=FAIL;
			else{
				tailcut();
				int i;
				for(i=length;i>length/2;i--)
					tailcut();
				length=length/2;
				gotoxy(1,156);
				printf("%5d",score);
				gotoxy(3,156);
				printf("%5d",length);
			}
			break;
		}
		case TOOL:{
			eattool();
			gettool();
			map[head->x][head->y]=WALL;
			tailcut();
			break;
		}
		case CHANGE:{
			int i;
			srand(time(NULL));
			do{
				i=1+rand()%4;
			}while(i==background);
			background=i;
			state=1;
			new();
			break;
		}
		case CWALL:{
			struct Snake *p;
			int x0,y0;
			x0=head->x;
			y0=head->y;
			p=(struct Snake*)malloc(sizeof(struct Snake));
			head->previous=p;
			p->next=head;
			switch(direction){
				case 1:p->x=28;p->y=head->y;break;
				case 2:p->x=1;p->y=head->y;break;
				case 3:p->x=head->x;p->y=148;break;
				case 4:p->x=head->x;p->y=1;break;
				default:break;
			}
			head=p;
			gotoxy(head->x,head->y);
			printf("*");
			tailcut();
			tailcut();
			gotoxy(x0,y0);
			if(direction==1||direction==2)
				printf("-");
			else
				printf("|");
			break;
		}
    }
}
void checktool(void){
	int flag=0;
	if(n2!=0&&n-n2>=300){
		foodstate=0;
		clearfood();
		n2=0;
		gotoxy(36,10);
		printf("                                      ");
		gotoxy(36,10);
		printf("道具已失效：食物多多");
		flag=1;
	}
	if(n3!=0&&n-n3>=500){
		doublescore=0;
		n3=0;
		if(flag!=0)
			printf("  双倍积分卡");
		else{
			gotoxy(36,10);
			printf("                                       ");
			gotoxy(36,10);
			printf("道具已失效：双倍积分卡");
			flag=1;
		}
	}
	if(n1!=0&&n-n1>=500){
		twohead=0;
		n1=0;
		if(flag!=0)
			printf("  双头蛇");
		else{
			gotoxy(36,10);
			printf("                                       ");
			gotoxy(36,10);
			printf("道具已失效：双头蛇");
		}
	}
}
void margin(void){
	int s[30][150]={0};
	int i,j;
	for(j=0;j<150;j++)	s[0][j]=s[29][j]=1;
	for(i=0;i<30;i++)	s[i][0]=s[i][149]=1;
	for(i=0;i<30;i++){
		for(j=0;j<150;j++)
			switch(s[i][j]){
				case 1:printf("#");break;
				case 0:printf(" ");break;
			}
		printf("\n");
	}
	return;
}

