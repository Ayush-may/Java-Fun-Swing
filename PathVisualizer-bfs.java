import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

class Box{
   int x,y;
   Box(int i, int j){
      x=i;
      y=j;
      }
   }

class PathVisualizer extends JPanel{

   int windowHeight = 500;
   int windowWidth = 500;
   int col = 20;
   int row = 20;
   int unit = windowHeight / col;
   int delay = 100;
   Box grid[][] = new Box[col][row];
   Box startNode  = grid[0][0];
   ArrayList<Box> openSet = new ArrayList<>();
   JPanel panel;
   ActionListener action = new ActionListener(){
	  	 public void actionPerformed(ActionEvent e){
			System.out.println("sdsd");
		}
	  };
   
   Timer timer ;
		
   PathVisualizer(){
   	  panel = new JPanel();
      this.setPreferredSize( new Dimension(windowHeight,windowWidth));
      this.setFocusable(true);
// 	  panel.addActionListener(action);
	  timer = new Timer( delay , action );
	  timer .start();
      start();
   	
      }
	
   public void start(){
 
 
   
//       for(int i=0 ;i<col ;i++){
//          for(int j=0;j<row ;j++){
//             grid[i][j] = new Box(i,j);
//             }	
//          }
//     	openSet.add(startNode);
      }
	
   public void paint(Graphics g){
      super.paint(g);
   	
	//Grid
      g.setColor(Color.BLACK);
      for(int i=0;i<col;i++){
         for(int j=0;j<row;j++){
            g.drawRect(i*unit,j*unit,unit,unit);
            }
         }
   		
// 		if(openSet.size()>0){
// 			System.out.println("If");
// 		}
// 		else{
// 			System.out.println("Else");		
// 		}
      }
	  

	public void updateX_Y(){
	
	}
   }