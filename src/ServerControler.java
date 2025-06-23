import javax.swing.*;
import java.awt.event.*;

//这个类处理来自服务器视图的输入
public class ServerControler{
	public ServerView view;
	public ServerModel model;
	public int helpMessageCount = 1;

	//一个玩家坦克的参考

	public ServerControler(ServerView thisview,  ServerModel thismodel){
		view = thisview;
		model = thismodel;

		
		//操作建立主机按钮的动作
		view.createServer.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.serverCreated)
						model.t.start();
				}
			}
		);

		//操作暂停/继续按钮的动作
		view.pauseAndResume.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					model.pausePressed = true;;
					if(!model.gameOver && model.gameStarted){
						if(!model.gamePaused){
							model.gamePaused = true;
							model.addMessage("主机端玩家暂停了游戏");
						}else{
							model.gamePaused = false;
							model.addMessage("主机端玩家取消了暂停");
						}
					}
				}
			}
		);

		//操作帮助按钮的动作
		view.help.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					model.addMessage("******************************坦克大战 ******************************");
					model.addMessage("帮助: 按空格键发射子弹,按键盘的wsad来控制坦克的移动");
					model.addMessage("如果按键没有反应请切换为英文输入法");
					model.addMessage("每个坦克有三次生命，三次生命都耗光或基地被破坏则游戏失败");
					model.addMessage("********************************************************************");
				}
			}
		);

		//操作退出按钮的动作
		view.exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);

		
		JPanel temp = view.mainPanel;
		temp.addKeyListener( new KeyAdapter(){
				public void keyPressed(KeyEvent e){
					if(model.P1 != null){
						if(e.getKeyCode() == KeyEvent.VK_W){
							model.P1.moveUp = true;
							model.P1.moveDown = false;
							model.P1.moveLeft = false;
							model.P1.moveRight = false;
						}
						if(e.getKeyCode() == KeyEvent.VK_S ){
							model.P1.moveDown = true;
							model.P1.moveUp = false;
							model.P1.moveLeft = false;
							model.P1.moveRight = false;
						}
						if(e.getKeyCode() == KeyEvent.VK_A ){
							model.P1.moveLeft = true;
							model.P1.moveUp = false;
							model.P1.moveDown = false;
							model.P1.moveRight = false;
						}
						if(e.getKeyCode() == KeyEvent.VK_D ){
							model.P1.moveLeft = false;
							model.P1.moveUp = false;
							model.P1.moveDown = false;
							model.P1.moveRight = true;
						}
						if(e.getKeyChar() == ' ')
							model.P1.fire = true;


						if(e.getKeyChar() == 'y' && model.gameOver && !model.serverVoteYes){
							model.serverVoteYes = true;
							model.addMessage("等待用户端玩家的回应...");
						}

						if(e.getKeyChar() == 'n'  && model.gameOver)
							model.serverVoteNo = true;
					}
				}

				public void keyReleased(KeyEvent e){
						if(model.P1 != null){
							if(e.getKeyCode() == KeyEvent.VK_W)
								model.P1.moveUp = false;
							if(e.getKeyCode() == KeyEvent.VK_S )
								model.P1.moveDown = false;
							if(e.getKeyCode() == KeyEvent.VK_A )
								model.P1.moveLeft = false;
							if(e.getKeyCode() == KeyEvent.VK_D )
								model.P1.moveRight = false;
							if(e.getKeyChar() == ' ')
								model.P1.fire = false;
					}
				}
			}
		);

	}
}