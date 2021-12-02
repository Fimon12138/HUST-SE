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
	else  headadd(); 
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
    else  headadd(); 
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
	else  headadd(); 
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
	else  headadd(); 
}
void getmap(int n){
	FILE *fp;
	char ch;
	int i,j;
	if((fp=fopen("map.dat","rb"))==NULL){
		printf("地图加载失败！");
		exit(0);
	}
	rewind(fp);
	fseek(fp,2000*(n-1),0);
	for(i=0;i<20;i++){
		for(j=0;j<100;j++){
			ch=fgetc(fp);
			switch(ch){
				case '1':printf("#");map[i][j]=WALL;break;
				case '0':printf("%c",'\0');map[i][j]=WAY;break;
				default:system("cls");printf("地图信息错误！");exit(0);break;
			}
		}
		printf("\n");
	}
	fclose(fp);
	direction=4;
	length=3;
	difficult=1;
	poisonnumber=0;
	bombnumber=0;
	ainumber=0;
	gotoxy(2,102);
    printf("得分:%3d",score);
    gotoxy(4,102);
	printf("蛇长:%3d",length);
	gotoxy(6,102);
	printf("关卡:%3d",m);
	StartSnake(3); 
}
void printpoison(void){
	if(poisonnumber==0){
		int i,x,y;
		poisonnumber=difficult+m;
		for(i=0;i<poisonnumber;i++){
			srand(time(NULL));
			do{
				x=1+rand()%18;
				y=1+rand()%98;
			}while(map[x][y]!=WAY);
			gotoxy(x,y);
			printf("-");
			poison[i][0]=x;
			poison[i][1]=y;
			map[x][y]=POISON;
		}
		n0=n;
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
void twinkle(void){
	int i;
	if(poisonnumber!=0){
		if(twinklestate==0){
			for(i=0;i<poisonnumber;i++){
				gotoxy(poison[i][0],poison[i][1]);
				printf("-");
			}
			twinklestate=1;
		}
		else{
			for(i=0;i<poisonnumber;i++){
				gotoxy(poison[i][0],poison[i][1]);
				printf("%c",'\0');
			}
			twinklestate=0;
		}
		n0=n;
	}
}

void printbomb(void){
	if(bombnumber==0){
		int i,x,y;
		bombnumber=difficult+m;
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
void printai(void){
	if(ainumber==0){
		int x,y;
		srand(time(NULL));
		do{
			x=1+rand()%18;
			y=1+rand()%98;
		}while(map[x][y]!=WAY);
		gotoxy(x,y);
		printf("$");
		ai[0][0]=x;
		ai[0][1]=y;
		map[x][y]=AI;
		ainumber++;
	}
}
void eatai(void){
	map[ai[0][0]][ai[0][1]]=WAY;
	ainumber--;
}
void clearai(void){
	if(ainumber!=0){
		gotoxy(ai[0][0],ai[0][1]);
		printf("%c",'\0');
		map[ai[0][0]][ai[0][1]]=WAY;
		ainumber=0;
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
			gamestate=FAIL;
			break;
		}
		case FOOD:{
			map[head->x][head->y]=WALL;
			difficult++;
			score+=10;
			gotoxy(2,107);
			printf("%3d",score);
			length++;
			gotoxy(4,107);
			printf("%3d",length);
			if(length>=20)	gamestate=WIN;
			else	printfood();
			break;
		}
		case POISON:{
			eatpoison();
			map[head->x][head->y]=WALL;
			score-=10;
			length--;
			if(length<=0){
				gamestate=FAIL;
			}
			else{
				tailcut();
				tailcut();
				gotoxy(2,107);
				printf("%3d",score);
				gotoxy(4,107);
				printf("%3d",length);
			}
			break;
		}
		case BOMB:{
			eatbomb();
			map[head->x][head->y]=WALL;
			score-=20;
			if((length/2)<=0){
				gamestate=FAIL;
				break;
			}
			else{
				tailcut();
				int i;
				for(i=length;i>length/2;i--)
					tailcut();
				length=length/2;
				gotoxy(2,107);
				printf("%3d",score);
				gotoxy(4,107);
				printf("%3d",length);
				break;
			}
		}
		case AI:{
			eatai();
			map[head->x][head->y]=WALL;
			tailcut();
			autorun();
			break;
		}
    }
}
void dopoison(void){
	if(poisonnumber==0)
		printpoison();
	else
		clearpoison();
}
void dobomb(void){
	if(bombnumber==0)
		printbomb();
	else
		clearbomb();
}
void doai(void){
	if(ainumber==0)
		printai();
	else
		clearai();
}		
	
void save(void){
	FILE *fp,*fp1;
	if((fp=fopen("save.dat","wb"))==NULL||(fp1=fopen("savestate.dat","wb"))==NULL){
		system("cls");
		gotoxy(4,30);
		printf("进度保存失败");
		return;
	}
	int i,j;
	int s[4]={0};
	struct Snake *p; 
	for(i=0;i<20;i++){
		for(j=0;j<100;j++){
			switch(map[i][j]){
				case WALL:fputc('1',fp);break;
				case WAY:fputc('0',fp);break;
				case FOOD:fputc('2',fp);break;
				case POISON:fputc('3',fp);break;
				case BOMB:fputc('4',fp);break;
				case AI:fputc('5',fp);break;
				default:break;
			}
		}
	}
	s[1]=length%10;
	s[0]=length/10;
	for(i=0;i<2;i++)
		fputc(s[i]+48,fp);//蛇长，2 
	p=head;
	while(p!=NULL){
		s[1]=p->x%10;
		s[0]=p->x/10;
		s[3]=p->y%10;
		s[2]=p->y/10;
		for(i=0;i<4;i++)
			fputc(s[i]+48,fp);
		p=p->next;
	}//蛇每一节的坐标 
	s[3]=score%10;
	s[2]=(score%100)/10;
	s[1]=(score%1000)/100;
	s[0]=(score%10000)/1000;
	for(i=0;i<4;i++)
		fputc(s[i]+48,fp);//分数 
	s[1]=m%10;
	s[0]=m/10;
	for(i=0;i<2;i++)
		fputc(s[i]+48,fp);//关卡 
	fputc(direction+48,fp); //方向
	s[1]=poisonnumber%10;
	s[0]=poisonnumber/10;
	for(i=0;i<2;i++)
		fputc(s[i]+48,fp);//毒草个数
	for(i=0;i<poisonnumber;i++){
		s[1]=poison[i][0]%10;
		s[0]=poison[i][0]/10;
		s[3]=poison[i][1]%10;
		s[2]=poison[i][1]/10;
		for(j=0;j<4;j++)
			fputc(s[j]+48,fp);
	}//毒草的位置信息
	s[1]=bombnumber%10;
	s[0]=bombnumber/10;
	for(i=0;i<2;i++)
		fputc(s[i]+48,fp);//地雷个数
	for(i=0;i<bombnumber;i++){
		s[1]=bomb[i][0]%10;
		s[0]=bomb[i][0]/10;
		s[3]=bomb[i][1]%10;
		s[2]=bomb[i][1]/10;
		for(j=0;j<4;j++)
			fputc(s[j]+48,fp);
	}//地雷位置
	fputc(ainumber+48,fp);//智慧草个数
	for(i=0;i<ainumber;i++){
		s[1]=ai[i][0]%10;
		s[0]=ai[i][0]/10;
		s[3]=ai[i][1]%10;
		s[2]=ai[i][1]/10;
		for(j=0;j<4;j++)
			fputc(s[j]+48,fp);
	}//智慧草位置 
	s[1]=difficult%10;
	s[0]=difficult/10;
	for(i=0;i<2;i++)
		fputc(s[i]+48,fp);//难度系数 
	fclose(fp);
	fputc('1',fp1);
	fclose(fp1);
	system("cls");
	gotoxy(4,30);
	printf("游戏保存成功"); 
	return;
}
void getsave(void){
	FILE *fp,*fp1;
	char ch;
	int i,j,x0,y0;
	struct Snake *p1,*p2;
	if((fp=fopen("save.dat","rb"))==NULL||(fp1=fopen("savestate.dat","rb"))==NULL)
		return;
	else{
		system("cls");
		for(i=0;i<20;i++){
			for(j=0;j<100;j++){
				ch=fgetc(fp);
				switch(ch){
					case '1':printf("#");map[i][j]=WALL;break;
					case '0':printf("%c",'\0');map[i][j]=WAY;break;
					case '2':printf("+");map[i][j]=FOOD;break;
					case '3':printf("-");map[i][j]=POISON;break;
					case '4':printf("@");map[i][j]=BOMB;break;
					case '5':printf("$");map[i][j]=AI;break;
				}
			} 
			printf("\n");
		}
		j=0;
		for(i=0;i<2;i++){
			ch=fgetc(fp);
			j=j*10+(ch-48);
		}
		length=j;
		gotoxy(4,102);
		printf("蛇长:%3d",length);
		for(i=1;i<=length;i++){
			x0=fgetc(fp)-48;
			x0=10*x0+fgetc(fp)-48;
			y0=fgetc(fp)-48;
			y0=10*y0+fgetc(fp)-48;
			p1=(struct Snake*)malloc(sizeof(struct Snake));
			p1->x=x0;
			p1->y=y0;
			gotoxy(x0,y0);
			printf("*");
			if(i==1)
				p2=head=p1;
			else{
				p2->next=p1;
				p1->previous=p2;
				p2=p1;
				if(i==length)	tail=p1;
			}
		}
		tail->next=NULL;
		j=0;
		for(i=0;i<4;i++){
			ch=fgetc(fp);
			j=j*10+(ch-48);
		}
		score=j;
		gotoxy(2,102);
		printf("得分:%3d",score);
		j=0;
		for(i=0;i<2;i++){
			ch=fgetc(fp);
			j=j*10+(ch-48);
		}
		m=j;
		gotoxy(6,102);
		printf("关卡:%3d",m);
		ch=fgetc(fp);
		direction=ch-48;
		j=0;
		for(i=0;i<2;i++){
			ch=fgetc(fp);
			j=j*10+(ch-48);
		}
		poisonnumber=j;
		for(i=0;i<poisonnumber;i++){
			x0=fgetc(fp)-48;
			x0=10*x0+fgetc(fp)-48;
			y0=fgetc(fp)-48;
			y0=10*y0+fgetc(fp)-48;
			poison[i][0]=x0;
			poison[i][1]=y0;
		}
		j=0;
		for(i=0;i<2;i++){
			ch=fgetc(fp);
			j=j*10+(ch-48);
		}
		bombnumber=j;
		for(i=0;i<bombnumber;i++){
			x0=fgetc(fp)-48;
			x0=10*x0+fgetc(fp)-48;
			y0=fgetc(fp)-48;
			y0=10*y0+fgetc(fp)-48;
			bomb[i][0]=x0;
			bomb[i][1]=y0;
		}
		ainumber=fgetc(fp)-48;
		for(i=0;i<ainumber;i++){
			x0=fgetc(fp)-48;
			x0=10*x0+fgetc(fp)-48;
			y0=fgetc(fp)-48;
			y0=10*y0+fgetc(fp)-48;
			ai[i][0]=x0;
			ai[i][1]=y0;
		}	
		j=0;
		for(i=0;i<2;i++){
			ch=fgetc(fp);
			j=j*10+(ch-48);
		}
		difficult=j;
		fclose(fp);
		fclose(fp1);
	}
}
void honor(void){
	FILE *fp1,*fp2;
	if((fp1=fopen("honor_roll.dat","rb+"))==NULL||(fp2=fopen("honor_number.dat","rb+"))==NULL){
		system("cls");
		gotoxy(4,30);
		printf("记录保存失败!");
		Sleep(500);
		return;
	}
	typedef struct _man{
		char name[20];
		int score;
	}man;
	char ch;
	int i,j,number,m;
	j=0;
	for(i=0;i<2;i++){
		ch=fgetc(fp2);
		j=10*j+ch-48;
	}
	number=j;
	man records[number+1];
	man key;
	i=0;
	while(i<number){
		fgets(records[i].name,20,fp1);
		m=0;
		for(j=0;j<4;j++){
			ch=fgetc(fp1);
			m=10*m+ch-48;
		}
		records[i].score=m;
		i++;
	}
	gotoxy(4,30);
	printf("请输入你的姓名:");
	PrintCursor();
	gets(records[number].name);
	HideCursor();
	records[number].score=score;
	key=records[number];
	for(i=0;i<number;i++)
		if(key.score<records[i].score)
			break;
	m=i;
	for(j=number-1;j>=i;j--)
		records[j+1]=records[j];
	records[i]=key;
	if(number+1>10){
		rewind(fp1);
		for(i=1;i<number+1;i++){
			fputs(records[i].name,fp1);
			if(i==m)	fputs("\n",fp1);
			fputc(records[i].score/1000+48,fp1);
			fputc(records[i].score%1000/100+48,fp1);
			fputc(records[i].score%100/10+48,fp1);
			fputc(records[i].score%10+48,fp1);
		}
		rewind(fp2);
		fputc('1',fp2);
		fputc('0',fp2);
	}
	else{
		rewind(fp1);
		for(i=0;i<number+1;i++){
			fputs(records[i].name,fp1);
			if(i==m)	fputs("\n",fp1);
			fputc(records[i].score/1000+48,fp1);
			fputc(records[i].score%1000/100+48,fp1);
			fputc(records[i].score%100/10+48,fp1);
			fputc(records[i].score%10+48,fp1);
		}
		rewind(fp2);
		fputc((number+1)/10+48,fp2);
		fputc((number+1)%10+48,fp2);
	}
	fclose(fp1);
	fclose(fp2);
	system("cls");
	gotoxy(4,30);
	printf("记录保存成功!");
	return;
}
void gethonor(void){
	FILE *fp1,*fp2;
	if((fp1=fopen("honor_roll.dat","rb+"))==NULL||(fp2=fopen("honor_number.dat","rb+"))==NULL){
		system("cls");
		gotoxy(4,30);
		printf("记录读取失败!");
		return;
	}
	else{
		int i,j,number,m;
 		char ch,str[20];
		j=0;
		for(i=0;i<2;i++){
			ch=fgetc(fp2);
			j=10*j+ch-48;
		}
		number=j;
		if(number==0){
			gotoxy(4,30);
			printf("暂无记录!");
			gotoxy(6,25);
			printf("**按空格键返回主菜单**");
			return;
		}
		for(i=0;i<number;i++){
			fgets(str,20,fp1);
			printf("%s",str);
			m=0;
			for(j=0;j<4;j++){
				ch=fgetc(fp1);
				m=10*m+ch-48;
			}
			printf("\t%d\n",m);
		}
		printf("\n\n**按空格键返回主菜单**"); 
		fclose(fp1);
		fclose(fp2);
	}
}
void margin(void){
	int s[20][100]={0};
	int i,j;
	for(j=0;j<100;j++)	s[0][j]=s[19][j]=1;
	for(i=0;i<20;i++)	s[i][0]=s[i][99]=1;
	for(i=0;i<20;i++){
		for(j=0;j<100;j++)
			switch(s[i][j]){
				case 1:printf("#");break;
				case 0:printf(" ");break;
			}
		printf("\n");
	}
	return;
}
void gethelp(void){
	system("cls");
	printf("* 上下左右键控制蛇的运动方向\n");
	printf("* +代表食物，每吃一个食物蛇会长长一节，得分增加十分\n");
	printf("* -代表毒草，每吃到一个毒草蛇长减一节，得分减十分\n");
	printf("* @代表地雷，毎吃到一个地雷蛇长减少一半，得分减少二十分\n");
	printf("* $代表智慧草，毎吃到一个智慧草，蛇会自动吃两个食物\n");
	printf("* 当蛇长为零时游戏结束\n");
	printf("* 按esc键可保存并退出游戏\n");
	printf("* 按q键可保存游戏并返回至主菜单\n");
	printf("* 按空格键可暂停游戏，再次按空格键可继续\n"); 
	printf("* 游戏共有五关\n");
	printf("\n\n**按空格键返回主菜单**"); 
	return;
}
void choose(void){
	system("cls");
	int r=1,w=0;
	gotoxy(4,20);
	printf("关卡数:%2d",r);
	gotoxy(7,15);
	printf("**上下键选择   Enter键确认**");
	while(w==0){
		switch(getch()){
			case enter:w=1;break;
			default:break;
		}
		if(w!=0)
			break;
		switch(getch()){
			case up:{
				if(r==5)
					break;
				else{
					r++;
					gotoxy(4,27);
					printf("%2d",r);
					break;
				}
			}
			case down:{
				if(r==1)
					break;
				else{
					r--;
					gotoxy(4,27);
					printf("%2d",r);
					break;
				}
			}
			default:break;
		}
	}
	m=r-1;
}
void startgame(void){
	FILE *fp;
	int b,s=4,t,a=5;
	margin();
	gotoxy(s,20);
	printf("新游戏");
	if((fp=fopen("savestate.dat","rb"))!=NULL){
		if(fgetc(fp)=='1'){
			a++;
			s+=2;
			gotoxy(s,20);
			printf("继续游戏");
		}
	}
	gotoxy(s+2,20);
	printf("选关开始"); 
	gotoxy(s+4,20);
	printf("排行榜");
	gotoxy(s+6,20);
	printf("游戏帮助");
	gotoxy(s+8,20);
	printf("退出游戏");
	gotoxy(18,1);
	printf("**按上下左右及Enter键进行选择**"); 
	gotoxy(4,18);
	printf("*");
	b=1;s=4;
	t=0;
	while(t==0){
		switch(getch()){
			case enter:t=1;break;
			default:break;
		}
		if(t!=0)	break;
		switch(getch()){
			case up:{
				if(b==1)	break;
				else{
					gotoxy(s,18);
					printf("%c",'\0');
					s-=2;
					gotoxy(s,18);
					printf("*");
					b--;
					break;
				}
			}
			case down:{
				if(b==a)	break;
				else{
					gotoxy(s,18);
					printf("%c",'\0');
					s+=2;
					gotoxy(s,18);
					printf("*");
					b++;
					break;
				}
			}
			default:break;
		}
	}
	if(a==6){
		switch(b){
			case 1:{
				gamestate=NEWGO;
				m=0;
				score=0; 
				break;
			}
			case 2:{
				gamestate=GO;
				getsave();
				if(poisonnumber!=0)
					n0=n=10;
				else
					n=10;
				break;
			}
			case 3:{
				choose();
				gamestate=NEWGO;
				break;
			}
			case 4:{
				system("cls");
				gethonor();
				getch();
				break;
			}
			case 5:{
				gethelp();
				getch();
				break;
			}
			case 6:{
				gamestate=FINISH;
				break;
			}
		}
	}
	else{
		switch(b){
			case 1:{
				gamestate=NEWGO;
				m=0;
				score=0;
				break;
			}
			case 2:{
				choose();
				gamestate=NEWGO;
				break;
			}
			case 3:{
				system("cls");
				gethonor();
				getch();
				break;
			}
			case 4:{
				gethelp();
				getch();
				break;
			}
			case 5:{
				gamestate=FINISH;
				break;
			}
		}
	}
	return;
}

