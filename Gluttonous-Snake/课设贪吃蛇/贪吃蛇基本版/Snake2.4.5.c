#include"basic.h"
#include"ai.c"
#include"functions.c"

int main(){
	HideCursor();
	system("color 07");
	gamestate=NONE;
	while(gamestate!=FINISH){
		gamestate=NONE;
		while(gamestate==NONE){
			system("cls");
			startgame();
		}
		while(gamestate!=FINISH&&gamestate!=FAIL){
			if(gamestate==NEWGO){
				system("cls");
				m++;
				getmap(m);
				printfood();
				n=0;
			}
			gamestate=GO;
			while(gamestate==GO){
				while(!kbhit()){
					if(n%10==0){
						switch((n/10)%10){
							case 0:dopoison();break;
							case 4:dobomb();break;
							case 7:doai();break;
							default:break;
						}
					}
					if(n-n0>=2)
						twinkle();
					headadd();
					n++;
					check();
					if(gamestate==FAIL||gamestate==WIN)
						break;
					Sleep(speed());
				}
				if(gamestate==FAIL||gamestate==WIN)
					break;
				switch(getch()){
					case esc:save();gamestate=FINISH;break;
					case q:save();gamestate=SAVE;break;
					default:break;
				}
				if(gamestate==FINISH||gamestate==SAVE)
					break;
				switch(getch()){
					case up:moveup();break;
					case down:movedown();break;
					case left:moveleft();break;
					case right:moveright();break;
					case 32:headadd();break;
					default:break;
				}
				n++;
				check();
				if(gamestate==FAIL||gamestate==WIN)
					break;
				Sleep(speed());
			}
			switch(gamestate){
				case FAIL:{
					system("cls");
					gotoxy(2,30);
					printf("You die!");
					honor();
					gotoxy(6,25);
					printf("**按空格键返回主菜单**");
					getch();
					FILE *fp;
					if((fp=fopen("savestate.dat","wb"))!=NULL){
						fputc('0',fp);
						fclose(fp);
					}
					break;
				}
				case WIN:{
					if(m<5){
						system("cls");
						gotoxy(4,30);
						printf("You win!");
						gotoxy(6,25);
						printf("**按空格键进入下一关**");
						getch();
						gamestate=NEWGO;
					}
					else{
						system("cls");
						gotoxy(2,30);
						printf("你已通过所有关卡!");
						honor();
						gotoxy(6,25);
						printf("**按空格键返回主菜单**");
						FILE *fp;
						if((fp=fopen("savestate.dat","wb"))!=NULL){
							fputc('0',fp);
							fclose(fp);
						}
						gamestate=FAIL;
					}
					break;
				}
				case SAVE:{
					gotoxy(6,30);
					printf("**按空格键返回主菜单**");
					getch();
					gamestate=FAIL;
					break;
				}
				default:break;
			}
		}
	}
	return 0;
}
