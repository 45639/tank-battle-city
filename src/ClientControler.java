import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import javax.swing.JFrame;
import java.awt.event.*;

//这个类处理来自客户端视图框架的输入
public class ClientControler{
	public boolean serverConnected;;
	public boolean gameStarted;
	public boolean gamePaused;
	public ClientView view;
	public ClientModel model;
	public int helpMessageCount = 1;


	public ClientControler(ClientView thisview, ClientModel thismodel){
		view = thisview;
		model = thismodel;

		//handel connectServer按钮操作
		view.connectServer.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.serverConnected){
						model.serverIP = view.IPfield.getText();
						model.t.start();
					}
				}
			}
		);

		//handel pauseAndResume 按钮操作
		view.pauseAndResume.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.gameOver && model.gameStarted){
						model.pausePressed = true;
						if(!model.gamePaused){
							model.gamePaused = true;
							model.addMessage("用户端玩家暂停了游戏");
						}else{
							model.gamePaused = false;
							model.addMessage("用户端玩家取消了暂停");
						}
					}
				}
			}
		);

		//handel help 按钮操作
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

		//handel exit 按钮操作
		view.exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);


		//处理从键盘输入
//		view.messageField.addKeyListener( new KeyAdapter(){
//			public void keyPressed(KeyEvent e){
//				if(helpMessageCount  > 0){
//					model.addMessage("提示：用\"tab\"键可以自由切换于控制界面和对话界面");
//					model.addMessage("提示：按回车键可以直接发送您的对话");
//					helpMessageCount--;
//				}
//
//				if(e.getKeyCode()==e.VK_ENTER){
//					if(!view.messageField.getText().equals("")){
//						model.addMessage("用户端玩家说：" + view.messageField.getText());
//						model.playerTypedMessage += "e" + view.messageField.getText() + ";";
//						view.messageField.setText("");
//					}else{
//						model.addMessage("对话内容不能为空");
//					}
//				}
//			}
//		});

		JPanel temp = view.mainPanel;
		temp.addKeyListener( new KeyAdapter(){
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_W){
						model.moveUp = true;
						model.moveDown = false;
						model.moveLeft = false;
						model.moveRight = false;
					}
					if(e.getKeyCode() == KeyEvent.VK_S ){
						model.moveDown = true;
						model.moveUp = false;
						model.moveLeft = false;
						model.moveRight = false;
					}
					if(e.getKeyCode() == KeyEvent.VK_A ){
						model.moveLeft = true;
						model.moveUp = false;
						model.moveDown = false;
						model.moveRight = false;
					}
					if(e.getKeyCode() == KeyEvent.VK_D ){
						model.moveLeft = false;
						model.moveUp = false;
						model.moveDown = false;
						model.moveRight = true;
					}

					if(e.getKeyChar() == ' ')
							model.fire = true;

//					if(e.getKeyCode()==e.VK_ENTER){
//						if(!view.messageField.getText().equals("")){
//							model.addMessage("用户端玩家说：" + view.messageField.getText());
//							model.playerTypedMessage += "e" + view.messageField.getText() + ";";
//							view.messageField.setText("");
//						}
//					}

					if(e.getKeyChar() == 'y' && model.gameOver && !model.clientVoteYes){
						model.clientVoteYes = true;
						model.addMessage("等待主机端玩家回应...");
					}

					if(e.getKeyChar() == 'n'  && model.gameOver)
						model.clientVoteNo = true;
				}

				public void keyReleased(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_W)
						model.moveUp = false;
					if(e.getKeyCode() == KeyEvent.VK_S )
						model.moveDown = false;
					if(e.getKeyCode() == KeyEvent.VK_A )
						model.moveLeft = false;
					if(e.getKeyCode() == KeyEvent.VK_D )
						model.moveRight = false;
					if(e.getKeyChar() == ' ')
							model.fire = false;
				}
			}
		);

	}
}

