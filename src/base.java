import java.awt.*;

//创建并设置基地
public class base implements Actor{
	private Rectangle border;
	public Image base;
	public int xPos, yPos;
	public ServerModel gameModel;
	public int steelWallTime;
	public boolean baseKilled;

	public base(ServerModel gameModel){
		this.gameModel = gameModel;
		xPos = 260;
		yPos = 498;
		base = gameModel.textures[0];
		border = new Rectangle(xPos - 11, yPos - 11, 23, 23);

	}

	public Rectangle getBorder(){
		return border;
	}

	public void doom(){
		base = gameModel.textures[1];
		if(!baseKilled)
			gameModel.addActor(new bomb(xPos, yPos, "big", gameModel));
		baseKilled = true;

		//记录变化到输出行
		gameModel.outputLine+="b"+ xPos + "," + yPos + "," + "1;";

	}


	public void move(){
		return;

	}

	public void writeToOutputLine(String type, boolean[] shape, int  xPos, int  yPos){
		//记录变化到输出行
		gameModel.outputLine+=type + xPos + ","+ yPos+",";
		for(int i = 0; i < shape.length; i++){
			if(shape[i])
				gameModel.outputLine+="1";
			else
				gameModel.outputLine+="0";
		}
		gameModel.outputLine+=";";
	}

	public String getType(){
		return "base";
	}

	//设置一幅图片来代表基地
	public void draw(Graphics g){
		g.drawImage(base, xPos - 12, yPos - 12, null );
	}

	//未使用的方法
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean walldestoried(){return false;}

}