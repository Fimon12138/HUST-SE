#include"1.c"
#include"BFSandDFS.c"
void pathBFS(point v){
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
		Sleep(3000);
	}
	else{
		int i;
		for(i=0;i<4;i++){
			point u;
			u.x=v.x+dx[i];
			u.y=v.y+dy[i];
			if(map[u.x][u.y].condition!=WALL&&legal(u.x,u.y)==1){
				if(map[u.x][u.y].dis==map[v.x][v.y].dis-1){
					top++;
					way[top][0]=u.x;
					way[top][1]=u.y;
					pathBFS(u);
					top--;
				}
			}
		}
	}
}
int main(){
	printf("请输入地图规格：");
	scanf("%d %d",&n,&m);
	int i,j;
	char c;
	printf("请输入地图：\n");
	c=getchar();
	for(i=0;i<n;i++)
		for(j=0;j<m;j++){
			scanf("%c",&c);
			get(c,i,j);
		}
	DFS(start);
	pathDFS(end);
	getch();
	gotoxy(12,0);
	printf("count=%d",count);/**/
	getch();
	return 0;
} 
