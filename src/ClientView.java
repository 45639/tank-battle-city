//	坦克大战连线版用户端
import javax.swing.*;
import java.awt.*;

//这个类代表服务器的图形界面
public class ClientView extends JFrame{
	public drawingPanel mainPanel;
	public JButton  connectServer, exit, pauseAndResume, help;
	public JTextField  IPfield;
	public JLabel enterIP;
	public Image offScreenImage;

	public ClientControler controler;
	public ClientModel model;


	public ClientView(){
		super("坦克大战");

		try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		getContentPane().setLayout(null);

		//设置动画绘制主面板
		mainPanel = new drawingPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0,  22, 679, 605);
		mainPanel.setBackground(Color.black);

		getContentPane().add(mainPanel);
		mainPanel.setFocusable(true);

		//设置选项按钮和IP文本字段
		enterIP = new JLabel("输入主机IP");
		enterIP.setBounds(5, 0,65,22);
		getContentPane().add(enterIP);

		IPfield = new JTextField();
		IPfield.setBounds(68, 0,88,22);
		getContentPane().add(IPfield);

		connectServer = new JButton("连接主机");
		connectServer.setBounds(160, 0,90,22);
		getContentPane().add(connectServer);
		connectServer.setFocusable(false);

		pauseAndResume = new JButton("暂停/继续");
		pauseAndResume.setBounds(250, 0,90,22);
		getContentPane().add(pauseAndResume);
		pauseAndResume.setFocusable(false);

		help = new JButton("帮助");
		help.setBounds(340, 0,90,22);
		getContentPane().add(help);
		help.setFocusable(false);

		exit = new JButton("退出");
		exit.setBounds(430, 0,90,22);
		getContentPane().add(exit);
		exit.setFocusable(false);

		//设置面框架
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 130, 535, 580);
    	setVisible(true);
    	setResizable( false );

		//设置客户端模型
		model = new ClientModel(this);

		//设置客户端控制器
		controler = new ClientControler(this, model);
	}

	public static void main(String[] args){
		new ClientView();
	}

}