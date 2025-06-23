import java.awt.*;

//创建接口
public interface Actor {
	public void draw(Graphics g);		//游戏地图界面绘制
	public int getxPos();
	public int getyPos();
}
