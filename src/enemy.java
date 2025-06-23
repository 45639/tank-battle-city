import java.awt.*;

//创建敌方坦克
public class enemy implements Actor{
	public final int UP = 0;
	public final int DOWN = 1;
	public final int LEFT = 2;
	public final int RIGHT = 3;
	public final int size = 12;
	public final Rectangle map = new Rectangle(35, 35, 452, 452);
	public static int freezedTime;
	public static int freezedMoment;
	public int numberOfBullet;
	public int coolDownTime;
	public int type;
	public int direction;
	public int interval;
	public int xPos, yPos, xVPos, yVPos;
	public Rectangle border;
	public boolean flashing;
	public double speed;
	public double firePosibility;
	public Image[] textures;
	public ServerModel gameModel;

	public enemy(int type,  boolean flashing, int xPos, int yPos, ServerModel gameModel){
		this.type = type;
		this. xPos = xPos;
		this.yPos = yPos;
		this.flashing = flashing;
		this.gameModel = gameModel;

		//设置敌人的共同属性
		interval = (int)(Math.random()*200);//生成下一个方向的间隔
		direction = (int)(Math.random()*4);//方向
		numberOfBullet = 1;//子弹数量
		xVPos = xPos;
		yVPos = yPos;
		border = new Rectangle(xPos - size, yPos - size, 25, 25);


		//根据不同类型的敌人设置独特的开火几率和速度
		if(type ==1 ){
			firePosibility = 0.8;
			speed = 2;
			textures = new Image[8];
			for(int i = 0; i < 8; i++)
				textures[i] = gameModel.textures[38+i];
		}else if(type == 2){
			firePosibility = 0.95;
			speed = 4;
			textures = new Image[8];
			for(int i = 0; i < 8; i++)
				textures[i] = gameModel.textures[2+i];
		}

	}

	public void move(){
       if(gameModel.gamePaused){
	         writeToOutputLine();
	  		 return;
		}
      if(freezedTime > ServerModel.gameFlow - freezedMoment){
	   		writeToOutputLine();
	   		return;
		}


        //敌方坦克在一个周期内将会朝着相同的方向继续移动（如果不与其他对象相互影响）
        //在每个周期结束时，它将转向新的方向
        if(interval > 0)
        	interval--;
        if(interval == 0){
			interval = (int)(Math.random()*200);
			int newDirection = (int)(Math.random()*4);
			if(direction != newDirection){
				if(direction/2 != newDirection/2){
					xPos = xVPos; yPos = yVPos;
					border.x = xPos - size; border.y = yPos - size;
				}
				direction = newDirection;
			}

		}


		//完全随机的决定是否要发射一颗子弹，敌方坦克不能开火
		//如果第一个不是摧毁的子弹
		if(coolDownTime > 0)
			coolDownTime--;
		if(Math.random() > firePosibility && coolDownTime == 0 && numberOfBullet > 0){
			//获得子弹方向
			int c = direction;
			//获得子弹位置
			int a, b;
			if(direction == UP){
				a = xPos; b = yPos - size;
			}else if(direction == DOWN){
				a = xPos; b = yPos + size;
			}else if(direction == LEFT){
				a = xPos - size; b = yPos;
			}else{
				a = xPos + size; b = yPos;
			}
			//获得子弹速度
			int d;
			if(type == 1){
				d = 5;
			}else{
				d = 7;
			}
			//添加子弹
			gameModel.addActor(new bullet(a,b,c,d,1, this, gameModel));
			coolDownTime = 5;//冷却时间
			if(type == 2)
				coolDownTime = 7;
			numberOfBullet--;
		}

		//保存当前位置信息,如果确定了新举措无效后,然后改变
		
		int xPosTemp = xPos;
		int yPosTemp = yPos;
		Rectangle borderTemp = new Rectangle(xPosTemp - size, yPosTemp - size, 25,25);

		//定义敌方坦克的下一个边界，假设它有效的根据方向来进行移动
		if(direction == UP){
			yPos-=speed;
		}else if(direction == DOWN){
			yPos+=speed;
		}else if(direction == LEFT){
			xPos-=speed;
		}else{
			xPos+=speed;
	    }


		//更新边界
		border.y = yPos - size;
		border.x = xPos - size;

		//检查下一个边界是否会与地图边界相交，如果不相交则随机生成边界
		if(!border.intersects(map)){
			direction = (int)(Math.random()*4);
			interval = (int)(Math.random()*250);
			xPos = xVPos; yPos = yVPos;
			border.x = xPos - size; border.y = yPos - size;
			writeToOutputLine();
			return;
		}

		//检查下一个边界是否与其他对象相交，例如玩家控制的坦克，墙等等
		for(int i = 0; i < gameModel.actors.length; i++){
			if(gameModel.actors[i] != null){
				if(this != gameModel.actors[i] ){
					if(border.intersects(gameModel.actors[i].getBorder())){
						//静态对象，墙和铁墙
						if(gameModel.actors[i].getType().equals("steelWall") || gameModel.actors[i].getType().equals("wall")){
							if(!gameModel.actors[i].walldestoried()){
								for(int j = 0;j < gameModel.actors[i].getDetailedBorder().length; j++){
									if( gameModel.actors[i].getDetailedBorder()[j] != null){
										if(gameModel.actors[i].getDetailedBorder()[j].intersects(border)){
											if(Math.random() > 0.90)
												direction = (int)(Math.random()*4);
											xPos = xVPos; yPos = yVPos;
											border.x  = xPos - size; border.y = yPos - size;
											writeToOutputLine();
											return;
										}
									}
								}
							}
						}else if(gameModel.actors[i].getType().equals("base")){
							if(Math.random() > 0.90)
								direction = (int)(Math.random()*4);
							xPos = xVPos; yPos = yVPos;
							border.x  = xPos - size; border.y = yPos - size;
							writeToOutputLine();
							return;
						}
						//其他对象，其他的坦克
						if(gameModel.actors[i].getType().equals("Player") || gameModel.actors[i].getType().equals("enemy")){
							if(!borderTemp.intersects(gameModel.actors[i].getBorder())){
								xPos = xPosTemp;
								yPos = yPosTemp;
								border.x = xPos - size; border.y = yPos - size;
								int newDirection = (int)(Math.random()*4);
								if(direction != newDirection){
									if(direction/2 != newDirection/2){
										xPos = xVPos; yPos = yVPos;
										border.x = xPos - size; border.y = yPos - size;
									}
									direction = newDirection;
								}
								writeToOutputLine();
								return;
							}
						}
					}
				}
			}
		}

		//当坦克90度倾斜时，找到坦克的虚拟位置，使用虚拟位置调整坦克的真实位置
		int a = (xPos - 10)/25;
		int b = (xPos - 10)%25;
		if(b < 7)
			b = 0;
		if(b > 18)
			b = 25;
		if((b < 19 && b > 6) || xPos < 17 || xPos > 492)
			b = 13;
		xVPos = a*25 + b + 10;
		int c = (yPos - 10)/25;
		int d = (yPos - 10)%25;
		if(d < 7)
			d = 0;
		if(d > 18)
			d = 25;
		if((d < 19 && d > 6) || yPos < 17 || yPos > 492)
			d = 13;
		yVPos = c*25 + d + 10;
		writeToOutputLine();
	}

	public void writeToOutputLine(){
		//将变化写入输出行 //指令“n”开头显示正常的对象,如坦克、启动符号
		gameModel.outputLine+="n"+ xPos + "," + yPos + ",";
		int textureIndex = 0;
		if(flashing && gameModel.gameFlow%10 > 4){
			if(type == 1)
				textureIndex = 42+ direction;
			else
				textureIndex = 6 + direction;
		}else{
			if(type == 1)
				textureIndex = 38 + direction;
			else
				textureIndex = 2 + direction;
		}
		gameModel.outputLine+= "" + textureIndex + ";";

	}

	//如果敌方坦克打出一颗子弹，判断会发生什么
	public void hurt(){
		boolean death = true;
		if(death){
			level.NoOfEnemy--;
			level.deathCount++;
			gameModel.removeActor(this);
			gameModel.addActor(new bomb(xPos, yPos, "big", gameModel));
		}
	}

	public String getType(){
		return "enemy";
	}

	public void draw(Graphics g){
		if(flashing && gameModel.gameFlow%10 > 4)
			g.drawImage(textures[textures.length-4+direction], xPos - size, yPos - size, null);
		else
			g.drawImage(textures[direction], xPos - size, yPos - size, null);
	}

	public Rectangle getBorder(){
		return border;
	}

	//未使用的方法
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean walldestoried(){return false;}
}