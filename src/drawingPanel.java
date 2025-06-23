import java.awt.*;
import javax.swing.*;

//绘图面板类属于客户端程序
public class drawingPanel extends JPanel{
	public Image offScreenImage;

	public String[] messageQueue;
	public Actor[] drawingList;
	public boolean gameStarted;
	public int green, red, blue;
	public int P1Life, P2Life, P1Score, P2Score, TotalScore, EnemyLeft,  LevelIndex;
	public Image P1Image, P2Image;

	public drawingPanel() {
		P1Image = Toolkit.getDefaultToolkit().getImage("image\\" +  55 + ".jpg");
		P2Image = Toolkit.getDefaultToolkit().getImage("image\\" +  73 + ".jpg");
	}



	public void paintComponent(Graphics g) {
		Graphics offScreenGraphics;
		if (offScreenImage == null) {
				offScreenImage = createImage(640, 550);
		}
		offScreenGraphics = offScreenImage.getGraphics();
		myPaint(offScreenGraphics);
		g.drawImage(offScreenImage, 0, 0, this);
	}

	public void myPaint(Graphics g) {
		super.paintComponent(g);

		if(gameStarted){
						
			//绘制背景
			g.setColor(Color.red);				//边框颜色
			g.drawRect(10, 10, 501, 501);

			//绘制坦克等等
			if(drawingList != null)
				for(int i = 0; i < drawingList.length; i++)
					if(drawingList[i] != null)
						drawingList[i].draw(g);

			//绘制获胜场景
			if(level.winningCount > 150){
				int temp = level.winningCount - 150;
				if(temp*10 >  300)
					temp = 30;
				if(level.winningCount  > 470)
					temp = 500 - level.winningCount;
				g.setColor(Color.gray);
				g.fillRect(11,11, 500, temp*10);
				g.fillRect(11, 500 - temp*10, 500, (1+temp)*10 + 2);

				if(level.winningCount  > 190  &&level.winningCount  < 470){
					if(level.winningCount > 400 ){
						red+=(int)((128-red)*0.2);
						green+=(int)((128-green)*0.2);
					}
					g.setColor(new Color(red, green, blue));
					g.drawString("过 关 了  ！", 240, 250);
				}
			}else{
				green = 23; red = 34; blue = 128;
			}
		}

		//消息
		g.setColor(new Color(255, 255, 255));
		if(messageQueue  != null){
			for(int i = 0 ; i < 8; i++){
				if(messageQueue[i] != null)
					g.drawString(messageQueue[i] , 5,  12 +i*16);
				else
					break;
			}
		}

	}
}