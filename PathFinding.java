import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

class Box{
	
	int i = 0;
	int j = 0;
	int value =0;
	boolean visited = false ;

	Box(){
		i = j = 0;
		visited = false;
	}
	
	Box(int j1,int i1){
		j=j1;
		i=i1;
	}
	
	Box(int j1, int i1,int nodeValue){  
	//x = i , y = j 
		j = j1;
		i = i1;
		value = nodeValue;
		visited = false ;
	}
}

class PathFinding extends JPanel {

	final int height = 400;
	final int width = 400;
	final int unit = 20;
	final int gameUnit = (height/unit) + 1;
	final int delay = 10;
	int yellowDelay = 10;
	int mouseX,mouseY;
	int startX ;
	int startY;
	int endX;
	int endY;

	boolean running = false;
	boolean stop = false;
	boolean finalPathDrawing = false;
	boolean bfs = false;
	boolean dfs = false;
	
	int nodeValue ;
	int count=0;
	int visited[][] = new int[gameUnit][gameUnit];
	int visitedBack[][] = new int[gameUnit][gameUnit]; 
	int grid[][] = new int[gameUnit][gameUnit];
	Box parentNode[][] = new Box[gameUnit][gameUnit];
	Queue<Box> back = new LinkedList<>();
	Queue<Box> que = new LinkedList<>();
	Stack<Box> stack = new Stack<Box>();
	Stack<Box> backStack = new Stack<Box>();
	
	
	Random random = new Random();
	Timer timer;
	Timer yellowTimer;
	JFrame frame = new JFrame("Click");
	
	Color littleBlue = new Color(108,173,223);
	Color littleLittleBlue = new Color(86,138,178);
	Color baseColor = new Color(105,129,131);
	
	PathFinding(){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(300,200);
		frame.add(this);
		frame.setResizable(false);
		this.setPreferredSize( new Dimension(height,width));
		frame.setTitle("BFS PATH FINDING");
		frame.pack();
		this.setBackground(Color.BLACK);
		
		//Action
		timer = new Timer(delay , new ActionListener(){
			public void actionPerformed(ActionEvent e){
			if(running){
				bfs();
				if(que.isEmpty()){
					timer.stop();
					}

// 				dfs();
// 				if(stack.empty()){
// 					timer.stop();
// 					}

				}
			}
		});

		yellowTimer = new Timer(yellowDelay , new ActionListener(){
			public void actionPerformed(ActionEvent e){
				finalPathDrawing = true;
				repaint();
			}
		});

		
		//Key
		frame.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){		
				running = true;
				timer.start();
				}
				
		});
		frame.setVisible(true);
		start();
	}
	
	
	public void start(){
	
		nodeValue = 0;
		startX = startY = 0;	
		endX = endY = gameUnit-2;
		//endY = 10;
		
		for(int i=0; i<gameUnit; i++){
			for(int j=0; j<gameUnit; j++){
				visited[j][i] =grid[j][i] = generateRandomNumber() ;	
				visitedBack[j][i] = 1;
			}
		}
		grid[startX][startY] = -1;			//starting node
		grid[endX][endY] = 9; 	//ending node
		
	}
		
	public void draw(Graphics g){

		for(int i=0; i<gameUnit; i++){
			for(int j=0; j<gameUnit; j++){
			
				g.setColor(Color.BLACK);
				g.drawRect(j*unit, i*unit, unit-1, unit-1); 

					/*
						white = 0
						end = 9
						start = -1
						visited = 1
						visiting = 2
						
						visited = 1
						visited = 0 (not visited)
					*/
					
				grid[startX][startY] = -1;		
				grid[endX][endY] = 9;
					
				switch(grid[j][i]){
					case -1:
						g.setColor(Color.BLUE);
						break;
					case 0:	
						g.setColor(Color.GRAY);
						break;
					case 2:		//visited
						g.setColor(Color.GREEN);
						break;
					case 3:		//visiting or inside que 
						g.setColor(Color.RED);
						break;
					case 4:		//Maze
						g.setColor(Color.BLACK);
						break;
					case 5:		//Shotest path
						g.setColor(Color.YELLOW);
						break;
					case 9:
						g.setColor(Color.CYAN);
						break;
				}
				g.fillRect(j*unit, i*unit, unit-2, unit-2 );
			}
		}	
	}	
	
	public void bfs(){
		Box node = new Box();
		if(visited[startX][startY] == 0){
			bfs = true;
			que.add(new Box(startY,startX,nodeValue));
			visited[startY][startX] = 1; 
			parentNode[startY][startX] = new Box(startY,startX,nodeValue);
		}
	
		if(!que.isEmpty()){
			node = que.remove();	
			visited[node.j][node.i] = 1;	//visited = 1
			grid[node.j][node.i] = 2;		//visited GREEN
			checkCollision(node.j,node.i,node);
			nodeValue = node.value + 1; 
			neighbourNode(node.j,node.i,nodeValue);	
		}
		else{
			timer.stop();
		}
		repaint();
	}

	public void neighbourNode(int j,int i,int nodeVa){

		if( (j+1)<gameUnit-1 && i<gameUnit-1 && visited[j+1][i]!=1  ){
			que.add(new Box( (j+1),i ,nodeVa));	
			grid[j+1][i] = 3;   
			visited[j+1][i]=1;
			parentNode[j+1][i] = new Box(j+1,i,nodeVa);
			visitedBack[j+1][i] = 0;
			checkCollision(j+1,i,parentNode[j+1][i]);
		}
		
		if( (j-1)>=0 && i>=0 && visited[j-1][i]!=1 ){
			que.add(new Box( (j-1),i ,nodeVa));	
			grid[j-1][i] = 3;   
			visited[j-1][i]=1;
			parentNode[j-1][i] = new Box(j-1,i,nodeVa);
			visitedBack[j-1][i] = 0;
			checkCollision(j-1,i,parentNode[j-1][i]);			
		}

		if( (i-1)>=0 && j>=0 && visited[j][i-1]!=1 ){
			que.add(new Box( j,(i-1) ,nodeVa));	
			grid[j][i-1] = 3;   
			visited[j][i-1] = 1;
			parentNode[j][i-1] = new Box(j,i-1,nodeVa);
			visitedBack[j][i-1] = 0;
			checkCollision(j,i-1,parentNode[j][i-1]);
		}
		
		if( (i+1)<gameUnit-1 && j<gameUnit-1 && visited[j][i+1]!=1 ){
			que.add(new Box( j,(i+1) ,nodeVa));	
			grid[j][i+1] = 3;   
			visited[j][i+1] = 1;
			parentNode[j][i+1] = new Box(j,i+1,nodeVa);
			visitedBack[j][i+1] = 0;
			checkCollision(j,i+1,parentNode[j][i+1]);
		}
		
	}
	
	public void findPathBfs( Box node ){
		while( node.value!=1 ){
				if( (node.j-1)>=0 && node.i>=0 && visitedBack[node.j-1][node.i]!=1 ){ //&& parentNode[node.j-1][node.i].value<node.value
					if(parentNode[node.j-1][node.i].value < node.value){
						back.add(new Box(node.j-1,node.i));
						visitedBack[node.j-1][node.i] = 1;	
						node = parentNode[node.j-1][node.i];
						repaint();
						continue;
					}
				}

				if( (node.j+1)<gameUnit-1 && node.i<gameUnit-1 && visitedBack[node.j+1][node.i]!=1 ){  //&& parentNode[node.j+1][node.i].value < node.value
					if( parentNode[node.j+1][node.i].value < node.value ){
						back.add(new Box(node.j+1,node.i));
						visitedBack[node.j+1][node.i] = 1;
						node = parentNode[node.j+1][node.i];
						repaint();
						continue;
					}
				}

		
				if( (node.i-1)>=0 && node.j>=0 && visitedBack[node.j][node.i-1]!=1 ){ //&& parentNode[node.j][node.i-1].value<node.value
					if(parentNode[node.j][node.i-1].value < node.value){
						back.add(new Box(node.j,node.i-1));
						visitedBack[node.j][node.i-1] = 1;
						node = parentNode[node.j][node.i-1];
						repaint();
						continue;
					}
				}
				
				if( (node.i+1)<gameUnit-1 && node.j<gameUnit-1 && visitedBack[node.j][node.i+1]!=1 ){  //&& parentNode[node.j][node.i+1].value<node.value
					if(parentNode[node.j][node.i+1].value < node.value){
						back.add(new Box(node.j,node.i+1));
						visitedBack[node.j][node.i+1] = 1;
						node = parentNode[node.j][node.i+1];
						repaint();
						continue;
					}
				}						
			}
			yellowTimer.start();
	}
	 
	public void dfs(){
		Box node;
		if(visited[startX][startY] == 0){
			dfs = true;
			stack.push(new Box(startY,startX,nodeValue));
			visited[startY][startX] = 1; 
			parentNode[startY][startX] = new Box(startY,startX,nodeValue); 
		}		
		if(!stack.empty()){
			node = stack.pop();	
			visited[node.j][node.i] = 1;	//visited = 1
			grid[node.j][node.i] = 2;		//visited GREEN
			nodeValue++;
			checkCollision(node.j,node.i,node);
			DfsNeighbourNode(node.j,node.i,nodeValue);	
		}
		repaint();
	} 

	public void DfsNeighbourNode(int j,int i,int nodeVa){

		if( (j+1)<gameUnit-1 && i<gameUnit-1 && visited[j+1][i]!=1  ){
			stack.push(new Box( (j+1),i ,nodeVa));	
			grid[j+1][i] = 3;   
			visited[j+1][i]=1;
			visitedBack[j+1][i] = 0;
			parentNode[j+1][i] = new Box(j+1,i,nodeVa);
			checkCollision(j+1,i,parentNode[j+1][i]);
		}
		
		if( (j-1)>=0 && i>=0 && visited[j-1][i]!=1 ){
			stack.push(new Box( (j-1),i ,nodeVa));	
			grid[j-1][i] = 3;   
			visited[j-1][i]=1;
			visitedBack[j-1][i] = 0;
			parentNode[j-1][i] = new Box(j-1,i,nodeVa);
			checkCollision(j-1,i,parentNode[j-1][i]);			
		}

		if( (i-1)>=0 && j>=0 && visited[j][i-1]!=1 ){
			stack.push(new Box( j,(i-1) ,nodeVa));	
			grid[j][i-1] = 3;   
			visited[j][i-1] = 1;
			visitedBack[j][i-1] = 0;
			parentNode[j][i-1] = new Box(j,i-1,nodeVa);
			checkCollision(j,i-1,parentNode[j][i-1]);
		}
		
		if( (i+1)<gameUnit-1 && j<gameUnit-1 && visited[j][i+1]!=1 ){
			stack.push(new Box( j,(i+1) ,nodeVa));	
			grid[j][i+1] = 3;   
			visited[j][i+1] = 1;
			visitedBack[j][i+1] = 0;
			parentNode[j][i+1] = new Box(j,i+1,nodeVa);
			checkCollision(j,i+1,parentNode[j][i+1]);
		}
		repaint();
	}


	public void findPathDfs( Box node ){
		while( node.value!=1 ){
			System.out.println("Node value is "+ node.value);
				if( (node.j-1)>=0 && node.i>=0 && visitedBack[node.j-1][node.i]!=1 ){ //&& parentNode[node.j-1][node.i].value<node.value
					if(parentNode[node.j-1][node.i].value < node.value){
						backStack.push(new Box(node.j-1,node.i));
						visitedBack[node.j-1][node.i] = 1;	
						node = parentNode[node.j-1][node.i];
						repaint();
						continue;
					}
				}

				if( (node.j+1)<gameUnit-1 && node.i<gameUnit-1 && visitedBack[node.j+1][node.i]!=1 ){  //&& parentNode[node.j+1][node.i].value < node.value
					if( parentNode[node.j+1][node.i].value < node.value ){
						backStack.push(new Box(node.j+1,node.i));
						visitedBack[node.j+1][node.i] = 1;
						node = parentNode[node.j+1][node.i];
						repaint();
						continue;
					}
				}

		
				if( (node.i-1)>=0 && node.j>=0 && visitedBack[node.j][node.i-1]!=1 ){ //&& parentNode[node.j][node.i-1].value<node.value
					if(parentNode[node.j][node.i-1].value < node.value){
						backStack.push(new Box(node.j,node.i-1));
						visitedBack[node.j][node.i-1] = 1;
						node = parentNode[node.j][node.i-1];
						repaint();
						continue;
					}
				}
				
				if( (node.i+1)<gameUnit-1 && node.j<gameUnit-1 && visitedBack[node.j][node.i+1]!=1 ){  //&& parentNode[node.j][node.i+1].value<node.value
					if(parentNode[node.j][node.i+1].value < node.value){
						backStack.push(new Box(node.j,node.i+1));
						visitedBack[node.j][node.i+1] = 1;
						node = parentNode[node.j][node.i+1];
						repaint();
						continue;
					}
				}						
			}
		yellowTimer.start();
	}

	public void drawFinalPath(Graphics g){
	
		if( finalPathDrawing && bfs ){
			while(!back.isEmpty()){
				Box node = back.remove();
				grid[node.j][node.i] = 5;
		}
	}
		if( finalPathDrawing && dfs ){
			while(!backStack.empty()){
				Box node = backStack.pop();
				grid[node.j][node.i] = 5;
			}
		}
		repaint();
	}
	
	public boolean checkCollision(int j,int i,Box node){
		if( endX==j && endY==i ){
			if(bfs){
				findPathBfs(node);
			}
			else{
				finalPathDrawing = true;
				 findPathDfs(node);
				 
				 System.out.println("DFS");
			}
			timer.stop();
			System.out.println("Found it");
			return true;
		}
		return false;
	}
	 
	public int generateRandomNumber(){
		int num;
		if(random.nextInt(4)==1){
			return 0;
		}
		else
			return 0;
	}
	 
	public void reverseQueue(Queue<Box> container){
		Stack<Box> reverseStack = new Stack<Box>();
		
		while(!container.isEmpty()){
			reverseStack.push(container.remove());
		}
		while(!reverseStack.empty()){
			container.add(reverseStack.pop());
		}
		System.out.println("Queue is Reversed");
	} 
	
	public void paint(Graphics g){
		super.paint(g);
		draw(g);
		drawFinalPath(g);
	}

	public static void main(String arg[]){
		new PathFinding();
	}
	
}




//					OLD METHOD OF FINDING NEIGHBOUR NODES
//
// 	public void neighbourNode(int j,int i){
// 		if(j>0 && i>0 && i<gameUnit && j<gameUnit ){
// 	
// 				if(j<gameUnit-2 && visited[j+1][i] != 1){
// 					que.add(new Box(j+1,i));	grid[j+1][i]=3;	visited[j+1][i] = 1;
// 				}
// 	
// 				if(i>0 && visited[j][i-1] != 1){
// 					que.add(new Box(j,i-1));	grid[j][i-1]=3;	visited[j][i-1] = 1;
// 				}
// 	
// 				if(j>0 && visited[j-1][i] != 1){
// 					que.add(new Box(j-1,i));	grid[j-1][i]=3;	visited[j-1][i] = 1;
// 				}
// 				
// 				if(i<gameUnit-2 && visited[j][i+1] != 1){
// 					que.add(new Box(j,i+1));	grid[j][i+1]=3;	visited[j][i+1] = 1; 				
// 				}
// 		}
// 		repaint();
// 	}
