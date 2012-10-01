import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class WelFrame extends JFrame
{
  JLabel welcome, creators, howTo;
  
  WelFrame(String title)
  {
    super(title);
    setSize(500, 700); // sets the size of the frame in pixels (width, height)
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // disposes window when "X" is clicked
    
    setLayout(new FlowLayout()); // sets the layout method to FlowLayout()
    ImageIcon howToPlay = new ImageIcon("howToPlay.PNG");
    welcome = new JLabel("WELCOME TO DAS INVADERS!"); // creates a label welcoming the player
    creators = new JLabel("CREATED BY:  Rhyle Majarais, Arjun Kalyan, Christabelle Fu, Sandeep Saldanha"); // states the creators of the game
    howTo = new JLabel(howToPlay); 
    add(welcome); // adds the label to the JFrame
    add(creators);
    add(howTo); // adds the picture to the JFrame
  }
}

public class DasInvaders extends JFrame implements KeyListener,MouseMotionListener
{
  Container cont;
  
  //game level
  int currentLevel = 1;
  
  //amount of enemies
  int numOfEnemies = 1;
  
  ImageIcon shipBullet = new ImageIcon("shipBullet.PNG"); //imports player bullet image
  ImageIcon enemyBullet = new ImageIcon("enemyBullet.PNG"); //imports alien bullet image
  
  //holds the player's bullets
  ArrayList playerBullets = new ArrayList();
  
  //holds the enemies
  ArrayList enemies = new ArrayList();
  
  //holds the enemy's bullets
  ArrayList enemyBullets = new ArrayList();
 
  ImageIcon shipImg = new ImageIcon("ship.PNG"); //imports player image
  ImageIcon shipHit = new ImageIcon("shipHit.PNG"); //imports damaged player image
  
  ImageIcon enemyImg = new ImageIcon("enemy.PNG"); //imports enemy image
  ImageIcon enemyHit = new ImageIcon("enemyHit.PNG"); //imports damaged enemy image
  
  //player's icon
  JLabel ship = new JLabel(shipImg);

  public DasInvaders()
  {
    super("DAS INVADERS");
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the window when 'X' is clicked
    setSize(500, 700);
    
    cont = getContentPane();
    cont.setLayout(null);
    
    cont.setBackground(Color.BLACK);
    
    cont.add(ship); //adds the player's image to the JFrame
    ship.setBounds(225, 550,50,50); //sets ship's bounds
    
    addKeyListener(this); // tells the keyboard to start listening for commands
    addMouseMotionListener(this); // tells mouse to start listening for commands
    
    populateEnemies(); //tells program to go to populateEnemies() method
    
    Play play = new Play();
    play.start();
    
    setContentPane(cont);
  }
  
  public void populateEnemies()
  {
    for (int i=0; i<=numOfEnemies; i++)
    {
      JLabel tempEnemy = new JLabel(enemyImg); 
      int randomLocation = (int)(Math.random()*500); // assign a random location for the enemies to appear
      enemies.add(tempEnemy);
      cont.add((JLabel)(enemies.get(i))); //adds enemies to the JFrame
      tempEnemy.setBounds(randomLocation,10,30,30);
      cont.setComponentZOrder(((JLabel)(enemies.get(i))),0);
    }
  }
  
  public class Play extends Thread
  {
    public void run()
    {
      while(true)
      {
        try
        {
          for (int i=0; i<enemies.size(); i++)
          {
           JLabel tempEnemy = (JLabel)(enemies.get(i));
           int distance = (int)(Math.random()*2); // sets a random location
           tempEnemy.setBounds(tempEnemy.getX(),tempEnemy.getY()+distance,30,30);
           
           if(tempEnemy.getBounds().intersects(ship.getBounds()))
           {
            cont.remove(tempEnemy); 
           }
           if (tempEnemy.getY()>550)
             tempEnemy.setBounds(tempEnemy.getX(), 10, 30, 30);
           int fire = (int)(Math.random()*2500);
           
           if (fire<=1)
           {
             JLabel tempBullet = new JLabel(enemyBullet);
             tempBullet.setBounds(tempEnemy.getX()+5,tempEnemy.getY()+30,10,20);
             enemyBullets.add(tempBullet);
             cont.add((JLabel)(enemyBullets.get(enemyBullets.size()-1)));
             cont.setComponentZOrder((JLabel)(enemyBullets.get(enemyBullets.size()-1)),0);
           }
        }

        //check to see if bullets hit the aliens
        boolean breakAll = false;
        for (int i=0; i<playerBullets.size(); i++)
        {
          JLabel temp = ((JLabel)(playerBullets.get(i)));
          temp.setBounds(temp.getX(), temp.getY()-8,10,20);
          
          if (temp.getY()<0)
          {
            cont.remove(temp);
            playerBullets.remove(i);
            i--;
          }
          
          for (int j=0; j<enemies.size(); j++)
          {
            JLabel tempEnemy = (JLabel)(enemies.get(j));
            
            if(temp.getBounds().intersects(tempEnemy.getBounds()))
            {
              tempEnemy.setIcon(enemyHit);
              Thread.sleep(100);
              enemies.remove(j);
              cont.remove(tempEnemy);
              numOfEnemies--;
              
              if(numOfEnemies<=0)
              {
                currentLevel++;
      
                numOfEnemies = currentLevel*currentLevel;
                populateEnemies();
                breakAll = true;
                break;
              }
            }
          }
           if(breakAll)
             break;
        }
        

        //check if alien's bullets hit player
        breakAll = false;
        for (int i = 0; i<enemyBullets.size(); i++)
        {
          JLabel temp = ((JLabel)(enemyBullets.get(i)));
          temp.setBounds(temp.getX(),temp.getY()+2,10,20);
          
          if (temp.getY()>600)
          {
            cont.remove(temp);
            enemyBullets.remove(i);
            i--;
          }
          
          if(ship.getBounds().intersects(temp.getBounds()))
          {
            ship.setIcon(shipHit);
            JFrame lose = new JFrame("END OF GAME");
            lose.setSize(500, 700);
            lose.setVisible(true);
            ImageIcon loseImg = new ImageIcon("youlost.PNG"); // imports image showing player they lost
            JLabel playerLost = new JLabel(loseImg);
            lose.add(playerLost); // adds image to the JFrame
            
            lose.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            Thread.sleep(100000); // game will be inactive for 100000 ms
            ship.setIcon(shipImg);
          }
          if(breakAll)
            break;
        }
         cont.repaint();
         Thread.sleep(10);
        }
        catch(Exception e){}
      }
    }
 }
  public void mouseMoved(MouseEvent event)
  {
    ship.setBounds(event.getX()-25,event.getY()-40,50,50);
  }
  public void mouseDragged(MouseEvent event){}
  public void keyPressed(KeyEvent event)
  {
    if(event.getKeyChar()==' ') //tells the computer how to respond when a key is pressed
    {
      JLabel tempBullet = new JLabel(shipBullet);
      tempBullet.setBounds(ship.getX()+20,ship.getY()-20,10,20);
      playerBullets.add(tempBullet);
      cont.add((JLabel)(playerBullets.get(playerBullets.size()-1)));
      cont.setComponentZOrder((JLabel)(playerBullets.get(playerBullets.size()-1)),0);
    }
  }
 
  public void keyReleased(KeyEvent event) {}
  public void keyTyped(KeyEvent event) {}
  public static void main(String[] args)
  {
    new DasInvaders();
    
    WelFrame title = new WelFrame("Welcome to Space Invaders!"); //creates a new JFrame
    title.setVisible(true); // makes the JFrame visible

  }
}