#include"basic.2.h"
#include"tools.c"
#include"functions.2.c"

int main(){
	system("color 07");
	HideCursor();
	system("mode con cols=170 lines=40 ");
	
	state=0;
	int flag;
	gamestate=GO;
	new();
	
	while(gamestate!=FAIL){
		flag=0;
		while(!kbhit()){
			if(foodstate!=1&&n%10==0){
				switch((n/10)%10){
					case 0:dopoison();break;
					case 4:dobomb();break;
					case 7:dotool();break;
					case 9:printwall();break;
					default:break;
				}
			}
			checktool();
			headadd();
			n++;
			check();
			if(gamestate==FAIL)	break;
			Sleep(speed());
		}
		if(gamestate==FAIL)	break;
		switch(getch()){
			case z:movetoolnum();flag=1;break;
			case _x:usetool();flag=1;break;
			case 32:dif++;printspeed();flag=1;break;
			default:break;
		}
		if(flag==1)	continue;
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
		if(gamestate==FAIL)	break;
		Sleep(speed());
	} 
	system("cls");
	printf("You die!");
	getch();
	return 0;
}
