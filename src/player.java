import java.awt.*;

public class player implements Actor{
	public final int UP = 0;
	public final int DOWN = 1;
	public final int LEFT = 2;
	public final int RIGHT = 3;
	public final int size = 12;
	public final Rectangle map = new Rectangle(35, 35, 452, 452);
	public String type;
	public int life;
	public int speed;
	public int direction;

	public int freezed; //游戏结束后 坦克静止
	public boolean moveUp;
	public boolean moveDown;
	public boolean moveLeft;
	public boolean moveRight;
	public boolean fire;
	public int numberOfBullet;//子弹数量
	public int coolDownTime; //发射子弹的时间间隔

	public int xPos, yPos, xVPos, yVPos;//坦克位置
	public Rectangle border; //坦克位置
	public Image standardImage;
	public Image[] textures;
	public ServerModel gameModel;

	//玩家信息初始化 life derection health 位置 图像
	public player(String type, ServerModel gameModel){
		this.type = type;
		life = 3;
		direction = UP;

		numberOfBullet = 1;

		this.gameModel = gameModel;

		textures = new Image[4];
		if(type.equals( "1P")){
			//玩家1游戏开启时位置
			xPos  = 198;
			yPos = 498;
			//玩家1的图像
			for(int i = 0; i < 4; i ++)
				textures[i] = gameModel.textures[54+i];
			standardImage = textures[0];
		}else{
			//玩家2游戏开启时位置
			xPos  = 323;
			yPos = 498;
			//玩家2的图像
			for(int i = 0; i < 4; i ++)
				textures[i] = gameModel.textures[72+i];
			standardImage = textures[0];
		}

		xVPos = xPos;
		yVPos = yPos;
		border = new Rectangle(xPos - size, yPos - size, 25, 25);

	}

	public void move(){
       if(gameModel.gamePaused){ //0停止
       		writeToOutputLine();
		    return;
		}

       if(coolDownTime > 0)
			coolDownTime--;//发射子弹的时间间隔

		 if(freezed == 1){
        	writeToOutputLine();
        	return;
		}

		// ab坐标 c方向 d速度 e威力
		//如果玩家点击“开火”键，并且到达下次发射子弹时间，则创建一个子弹目标（即发射子弹）
		if(fire && coolDownTime == 0 && numberOfBullet > 0){
			//子弹方向
			int c = direction;
			//子弹位置
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
			//子弹速度
			int d=7;

			//子弹威力
			int e=1;

			//添加子弹 bullet.java实现子弹数加一
			gameModel.addActor(new bullet(a,b,c,d,e, this, gameModel));
			//coolDownTime是发射子弹的时间间隔
			coolDownTime = 6;

			numberOfBullet--;
		}


		//保存当前位置信息，如果新的移动确定后无效，则更改
		//以前的位置
		int xPosTemp = xPos;
		int yPosTemp = yPos;
		Rectangle borderTemp = new Rectangle(xPosTemp - size, yPosTemp - size, 25,25);

		//根据玩家坦克的移动定义玩家坦克的下一个边界，假设它的下一个移动是有效的；
		boolean notMoving = false;
		if(moveUp){
			if(direction != UP && direction != DOWN)
				xPos = xVPos;
			yPos-=speed;
			direction = UP;
		}else if(moveDown){
			if(direction != UP && direction != DOWN)
				xPos = xVPos;
			yPos+=speed;
			direction = DOWN;
		}else if(moveLeft){
			if(direction != LEFT && direction != RIGHT)
				yPos = yVPos;
			xPos-=speed;
			direction = LEFT;
		}else if(moveRight){
			if(direction != LEFT && direction != RIGHT)
				yPos = yVPos;
			xPos+=speed;
			direction = RIGHT;
		}else{
				notMoving = true;
		}
		// 没动
		if(notMoving){
			if(speed > 0)
				speed--;
		}else{
			if(speed < 3)
				speed++;
		}

		//更新边界
		border.y = yPos - size;
		border.x = xPos - size;

		//检查下一个边界是否与地图边界相交，如果重叠 则恢复原位置坐标
		if(!border.intersects(map)){
			xPos = xVPos; yPos = yVPos;
			border.x  = xPos - size; border.y = yPos - size;
			writeToOutputLine();
			return;
		}


		//检查下个边界是否与其他对象相交 如玩家控制的坦克，墙等等
		for(int i = 0; i < gameModel.actors.length; i++){
			if(gameModel.actors[i] != null){
				if(this != gameModel.actors[i] ){
					if(border.intersects(gameModel.actors[i].getBorder())){ // 矩形相交
						
						//墙壁
						if(gameModel.actors[i].getType().equals("steelWall") || gameModel.actors[i].getType().equals("wall")){
							if(!gameModel.actors[i].walldestoried()){
								for(int j = 0;j < gameModel.actors[i].getDetailedBorder().length; j++){
									if( gameModel.actors[i].getDetailedBorder()[j] != null){
										if(gameModel.actors[i].getDetailedBorder()[j].intersects(border)){
												xPos = xVPos; yPos = yVPos;
												border.x  = xPos - size; border.y = yPos - size;
												writeToOutputLine();
												return;
										}
									}
								}
							}
						}

						//移动对象，例如敌人坦克
						else if(gameModel.actors[i].getType().equals("enemy") || gameModel.actors[i].getType().equals("Player") ){
							if(!borderTemp.intersects(gameModel.actors[i].getBorder()) || gameModel.actors[i].getType().equals("enemy")){
								xPos = xPosTemp;
								yPos = yPosTemp;
								border.x  = xPos - size; border.y = yPos - size;
								writeToOutputLine();
								return;
							}
						}
					}
				}
			}
		}
		
		//找到坦克的虚拟位置，当90度转弯时，虚拟位置用来调整坦克的真实位置。
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
		//将变化写入输出行
		gameModel.outputLine+="n"+ xPos + "," + yPos + ",";
		int textureIndex = 0;
		// P1 P2图像
		if(type.equals("1P")){	
				textureIndex = 54 + direction;
			
		}else{		
				textureIndex = 72 + direction;
		}

		gameModel.outputLine+= "" + textureIndex + ";";

	}

	public void draw(Graphics g){
		//绘制玩家坦克
		g.drawImage(textures[direction], xPos - size, yPos - size, null);

	}

	public Rectangle getBorder(){
		return border;
	}

	public String getType(){
		return "Player";
	}

	
	//坦克被子弹击中后的效果
	public void hurt(){

		//如果坦克被击中，那么玩家坦克失去一个生命，如果玩家坦克是最后一次生命，被击中，则game over

		gameModel.addActor(new bomb(xPos, yPos, "big",  gameModel));
		life--;
		if(life == 0){
			xPos = 100000; yPos = 100000;           //死亡之后 把图像移到面板外面
			border = new Rectangle(xPos - size, yPos - size, 25, 25);
			xVPos = xPos; yVPos = yPos;
			return;
		}else{
			direction = UP;

			numberOfBullet = 1;

			if(type.equals( "1P")){
				xPos  = 198;
				yPos = 498;
				border = new Rectangle(xPos - size, yPos - size, 25, 25);
				xVPos = xPos; yVPos = yPos;
				for(int i = 0; i < 4; i ++)
					textures[i] = gameModel.textures[54+i];
			}else{
				xPos = 323;
				yPos = 498;
				border = new Rectangle(xPos - size, yPos - size, 25, 25);
				xVPos = xPos; yVPos = yPos;
				for(int i = 0; i < 4; i ++)
					textures[i] = gameModel.textures[72+i];
			}
		}
		
	}


//重置 重新初始化
	public void reset(){
		direction = UP;
		life=3;
		if(type.equals( "1P")){
			xPos  = 198;
			yPos = 498;
		}else{
			xPos  = 323;
			yPos = 498;
		}

		xVPos = xPos;
		yVPos = yPos;
		border = new Rectangle(xPos - size, yPos - size, 25, 25);
	}

	//未使用的方法
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean walldestoried(){return false;}

}
