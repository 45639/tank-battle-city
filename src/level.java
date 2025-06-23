import java.awt.*;

//服务器端的类
//因为只有一层对象,所以在这个类是一个静态变量
public class level{
	public static int currentLevel = 1;		//当前关卡数，最初是1
	public static int enemySpawnTime = 100;	//敌人生成的时间间隔150
	public static int enemyLeft = 5;		//敌人数量20
	public static int deathCount = 0;
	public static int maxNoEnemy = 3;
	public static int NoOfEnemy = 0;
	public static int[] enemySequence;

	//制作获胜场景所需的变量
	public static int winningCount;

	public static void loadLevel(ServerModel gameModel){
		
		//从上个关卡清除所有东西
		for(int i = 0; i <  400; i ++)
			gameModel.actors[i] = null;

		//启动时各关卡共享
		enemyLeft = 5;

		//加载基地，每个关卡都一样
		gameModel.actors[0] = new wall(248, 498, 2, gameModel);
		gameModel.actors[1] = new wall(273, 498, 3, gameModel);
		gameModel.actors[2] = new wall(248, 473, 1, gameModel);
		gameModel.actors[3] = new wall(273, 473, 1, gameModel);
		gameModel.actors[4] = new base(gameModel);

		//加载一个关卡
		if(1+ (currentLevel-1)%8 == 1){
			enemySequence = new int[]{1,1,2,1,1};
			String[] level = new String[]{
					"__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__",
					"__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__",
					"__", "##", "##", "__", "##", "__", "##", "__", "##", "__", "__", "##", "__", "##", "__", "##", "__", "##", "##", "__",
					"__", "##", "##", "__", "##", "__", "##", "__", "##", "ss", "ss", "##", "__", "##", "__", "##", "__", "##", "##", "__",
					"__", "##", "##", "__", "##", "__", "##", "__", "##", "__", "__", "##", "__", "##", "__", "##", "__", "##", "##", "__",
					"__", "##", "##", "__", "##", "__", "##", "__", "__", "__", "__", "__", "__", "##", "__", "##", "__", "##", "##", "__",
					"__", "##", "##", "__", "__", "__", "##", "__", "__", "__", "__", "__", "__", "##", "__", "__", "__", "##", "##", "__",
					"__", "##", "##", "__", "__", "__", "__", "__", "##", "__", "__", "##", "__", "__", "__", "__", "__", "##", "##", "__",
					"__", "##", "##", "__", "__", "__", "__", "__", "##", "__", "__", "##", "__", "__", "__", "__", "__", "##", "##", "__",
					"__", "__", "__", "__", "__", "##", "__", "__", "__", "__", "__", "__", "__", "__", "##", "__", "__", "__", "__", "__",
					"##", "__", "__", "__", "__", "##", "__", "__", "__", "__", "__", "__", "__", "__", "##", "__", "__", "__", "__", "##",
					"s0", "__", "__", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "__", "__", "s0",
					"__", "__", "__", "__", "__", "##", "__", "__", "##", "##", "##", "##", "__", "__", "##", "__", "__", "__", "__", "__",
					"__", "##", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "##", "__",
					"__", "##", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "##", "__",
					"__", "##", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "__", "__", "##", "##", "__",
					"__", "##", "##", "__", "__", "ss", "__", "__", "#0", "__", "__", "#0", "__", "__", "ss", "__", "__", "##", "##", "__",
					"__", "__", "__", "__", "__", "##", "__", "__", "__", "__", "__", "__", "__", "__", "##", "__", "__", "__", "__", "__",
					"__", "__", "__", "__", "__", "##", "__", "__", "__", "__", "__", "__", "__", "__", "##", "__", "__", "__", "__", "__",
					"__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__", "__"
	       };
	       loadLevel(gameModel, level);
		}

		gameModel.addActor(gameModel.P1);
		gameModel.addActor(gameModel.P2);
	}

	public static void loadLevel(ServerModel gameModel, String[] level){
		for(int i = 0; i < level.length; i++){
			if(level[i].equals("##"))
				gameModel.addActor(new wall(23 + (i%20)*25,  23 + (i/20)*25,   gameModel));
           	if(level[i].equals("#0"))
				gameModel.addActor(new wall(23 + (i%20)*25,  23 + (i/20)*25,  0,  gameModel));
           	
		    if(level[i].equals("ss"))
				gameModel.addActor(new Steelwall(23 + (i%20)*25,  23 + (i/20)*25,  gameModel));
			if(level[i].equals("s0"))
				gameModel.addActor(new Steelwall(23 + (i%20)*25,  23 + (i/20)*25,  0,  gameModel));

		}
	}

	public static void spawnEnemy(ServerModel gameModel){
    	if(NoOfEnemy < maxNoEnemy && enemyLeft > 0 && (gameModel.gameFlow % enemySpawnTime == 0)){
			int xPos = 23 + (5 -enemyLeft)%3*238;
			boolean flashing = (enemyLeft%3 == 0);

			gameModel.addActor(new enemy(enemySequence[5-enemyLeft], flashing, xPos, 23, gameModel));
			enemyLeft--;
			NoOfEnemy++;
		}
	}

	public static void reset(){
		currentLevel = 1;
		enemySpawnTime = 100;
		enemyLeft = 5;
		deathCount = 0;
		maxNoEnemy = 3;
		NoOfEnemy = 0;
	}
}