void BFS(point S){
	Qs Queue;
	Queue.front=Queue.rear=0;
	enQueue(&Queue,S);
	map[S.x][S.y].dis=0;
	map[S.x][S.y].isVis=1;
	int i;
	while(Queue.front!=Queue.rear){
		point u=deQueue(&Queue);
		for(i=0;i<4;i++){
			int cx=u.x+dx[i];
			int cy=u.y+dy[i];
			if(map[cx][cy].isVis!=1&&legal(cx,cy)==1){
				if(map[cx][cy].condition!=WALL&&map[cx][cy].condition!=START){
					map[cx][cy].isVis=1;
					map[cx][cy].dis=map[u.x][u.y].dis+1;
					point v;
					v.x=cx;
					v.y=cy;
					enQueue(&Queue,v);
				}
			}
		}
	}
}



void DFS(point S){
	if(map[S.x][S.y].condition==END)
		return;
	else{
		int i;
		for(i=0;i<4;i++){
			int cx=S.x+dx[i];
			int cy=S.y+dy[i];
			if(map[cx][cy].isVis!=1&&legal(cx,cy)==1&&map[cx][cy].condition!=WALL&&map[cx][cy].condition!=START){
					map[cx][cy].isVis=1;
					map[cx][cy].dis=map[S.x][S.y].dis+1;
					point v;
					v.x=cx;
					v.y=cy;
					DFS(v);
				}
		}
	}
}

void pathDFS(point v){
	if(map[v.x][v.y].condition==START){
		count++;
		system("cls");
		int i,j;
		for(i=0;i<n;i++){
			for(j=0;j<m;j++){
				switch(map[i][j].condition){
					case WALL:printf("#");break;
					case WAY:printf("%c",'\0');break;
					case START:printf("S");break;
					case END:printf("E");break;
				}
			}
			printf("\n");
		}
		for(i=top-1;i>=0;i--){
			gotoxy(way[i][0],way[i][1]);
			printf("+");
		}
		Sleep(5000);
	}
	else{
		int i;
		for(i=0;i<4;i++){
			point u;
			u.x=v.x+dx[i];
			u.y=v.y+dy[i];
			if(map[u.x][u.y].condition!=WALL&&map[u.x][u.y].condition!=END&&legal(u.x,u.y)==1&&isVis[u.x][u.y]==0){
				isVis[u.x][u.y]=1;
				way[top+1][0]=u.x;
				way[top+1][1]=u.y;
				top++;
				pathDFS(u);
				top--;
				isVis[u.x][u.y]=0;
			}
		}
	}
}




















