import java.awt.*;

//创建子弹并设置属性
public class bullet implements Actor{
	public final Rectangle map = new Rectangle(18, 18, 486, 486);
	private Rectangle border;
	private int direction;
	private int Speed;
	private int bulletpower;
	public int xPos, yPos;
	public Actor owner;
	public ServerModel gameModel;
	public boolean hitTarget;

	// ab坐标 c方向 d速度 e威力
	public bullet(int a, int b, int c, int d, int e, Actor owner, ServerModel gameModel){
		this.owner = owner;
		this.gameModel = gameModel;
		xPos = a; yPos = b;
		direction = c;
		if(direction == 0 || direction == 1)
			border = new Rectangle(a - 2, b - 5, 5, 13);
		else
			border = new Rectangle(a - 5, b - 2, 13, 5);

		Speed = d;
		bulletpower = e;
	}

	public void draw(Graphics g) {
		g.setColor(Color.lightGray);
		if(direction == 0 || direction == 1)
			g.fillRect(border.x + 1, border.y +1, 3, 9);
		if(direction == 2 || direction == 3)
			g.fillRect(border.x +1, border.y + 1, 9, 3);
	}

	public void move(){
		if(gameModel.gamePaused){
			writeToOutputLine();
			return;
		}


		//检查这子弹是否撞击地图边界
		if(!border.intersects(map)){
			gameModel.removeActor(this);
			notifiyOwner();
			makeBomb();
			writeToOutputLine();
			return;
		}
		//检查这颗子弹是否击中其他对象
		for(int i = 0; i < gameModel.actors.length; i++){
			if(gameModel.actors[i] != null){
				if(gameModel.actors[i] != this && gameModel.actors[i] != owner){
					if(border.intersects(gameModel.actors[i].getBorder())){

						if(gameModel.actors[i].getType().equals("steelWall")){
							Steelwall temp = (Steelwall)gameModel.actors[i];
							if(!temp.walldestoried){
								temp.damageWall(border,  bulletpower, direction);
								if(temp.bulletdestoried)
									hitTarget = true;
							}
						}else if(gameModel.actors[i].getType().equals("wall")){
							wall temp = (wall)gameModel.actors[i];
							if(!temp.walldestoried){
								temp.damageWall(border,  bulletpower, direction);
								if(temp.bulletdestoried)
									hitTarget = true;
							}
						}else if(gameModel.actors[i].getType().equals("bullet")){
							bullet temp = (bullet)gameModel.actors[i];
							if(temp.owner.getType().equals("Player")){
								hitTarget = true;
								gameModel.removeActor(gameModel.actors[i]);
								temp.notifiyOwner();
							}
						}else if(gameModel.actors[i].getType().equals("Player")){
							if(owner.getType().equals("enemy")){
								player temp = (player)gameModel.actors[i];
							    temp.hurt();
							}else{
							}
							hitTarget = true;
						}else if(gameModel.actors[i].getType().equals("enemy") && owner.getType().equals("Player")){
							enemy temp = (enemy)gameModel.actors[i];
							player tempe = (player)owner;
							temp.hurt();
							hitTarget = true;
						}else if(gameModel.actors[i].getType().equals("base")){
							base temp = (base)gameModel.actors[i];
							temp.doom();
							hitTarget = true;
							gameModel.gameOver = true;
						}
					}
				}
			}
		}

		//如果子弹打到其他对象,从游戏系统中删除这个子弹对象
		if(hitTarget){
			gameModel.removeActor(this);
			notifiyOwner();
			makeBomb();
			writeToOutputLine();
			return;
		}

		if(direction == 0){
				border.y -= Speed;
				yPos -= Speed;
			}
			if(direction == 1){
				border.y += Speed;
				yPos += Speed;
			}
			if(direction == 2){
				border.x -= Speed;
				xPos -= Speed;
			}
			if(direction == 3){
				border.x += Speed;
				xPos += Speed;
		}
		writeToOutputLine();
	}

	public void writeToOutputLine(){
		gameModel.outputLine+="t"+ xPos + "," + yPos + "," + direction + ";";
	}

	public Rectangle getBorder(){
		return border;
	}

	public String getType(){
		return "bullet";
	}

	public void notifiyOwner(){
			if(owner != null){
				if(owner.getType().equals("Player")){
					player temp = (player)owner;
					temp.numberOfBullet++;
				}else if(owner.getType().equals("enemy")){
					enemy temp = (enemy)owner;
					temp.numberOfBullet++;
				}
			}
	}

	public void makeBomb(){
		gameModel.addActor(new bomb(xPos, yPos, "small", gameModel));
	}

	//未使用的方法
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean walldestoried(){return false;}




}
