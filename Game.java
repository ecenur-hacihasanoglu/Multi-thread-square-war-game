import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;



public class Game {
//	Enemy[] enemies;
//	The last change is adding 5's t x and y
	Boolean hitfire=false;
	Boolean win=false;
	Boolean closed;
	Boolean gothit=false;
	Boolean firetime=false;
	ArrayList<Game.Enemy> enemies = new ArrayList<Game.Enemy>();
	ArrayList<Game.Friend> friends = new ArrayList<Game.Friend>();
	ArrayList<Game.Fire> fires = new ArrayList<Game.Fire>();
	
	ArrayList<Game.Fire> my_fires = new ArrayList<Game.Fire>();
	ArrayList<Game.Fire> friendly_fires = new ArrayList<Game.Fire>();
	AirCraft aircraft;
	Boolean exited = false;
	Random random = new Random();
	Frame frame;
	ArrayList<int[]> starting_positions=new ArrayList<>();
	public Game() {
		frame = new Frame();
		win a= new win();
		a.start();
	}
	public class win extends Thread{
		@Override
		public void run() {
			while (!exited) {
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(enemies);
				if (enemies.isEmpty()) {
					if(!gothit && !hitfire) {
					Frame.ConfirmWindow cw = frame.new ConfirmWindow();
					win=true;
//					System.out.println("---------------------------------------------*********************win?");
					break;
					}
				} 
			}
		}
		
	}

	public int[] positionFind(int[] positions) {
		if (starting_positions != null) {
			if(!starting_positions.contains(positions)&&(positions[0]>280||positions[0]<220)&&(positions[1]>280||positions[1]<220)) {	
				
//				    System.out.println(starting_positions);
//				    starting_positions.add(positions);
					return positions;
			}
				
			else {
				int x = random.nextInt(1,49) * 10;
				int y = random.nextInt(1,49) * 10;
				int[] pos_new = { x, y };
				return positionFind(pos_new);
			}

		} else {
		    
			return positions;
		}
	}
	private int[] random_10(int x,int y,String type) {
		int oldx=x;
		int oldy=y;
		int axis = random.nextInt(2);
		int magnitude = random.nextInt(2) * 2 - 1;
		if ((x > 470 && axis == 1 && magnitude == 1)) {
			magnitude = -1;

		} else if ((x < 30 && axis == 1 && magnitude == -1)) {
			magnitude = 1;

		}
		x += axis * magnitude * 10;
		if ((y > 470 && axis == 0 && magnitude == 1)) {
			magnitude = -1;

		} else if ((y < 30 && axis == 0 && magnitude == -1)) {
			magnitude = 1;

		}
		y += (1 - axis) * magnitude * 10;
		int[] pos= {x,y};
		if(type.equals("friendly")) {
			for (int i = 0; i < friends.size(); i++) {
				if(friends.get(i)!=null&&friends.get(i).x==x && friends.get(i).y==y) {
					try {
						return random_10(oldx,oldy,type);
					} catch (java.lang.StackOverflowError e) {
						// TODO: handle exception
					}
				}
			}
		}else if(type.equals("enemy")) {
			for (int i = 0; i < enemies.size(); i++) {
				if(enemies.get(i)!=null&&enemies.get(i).x==x && enemies.get(i).y==y) {
	try {
						
						return random_10(oldx,oldy,type);
					} catch (java.lang.StackOverflowError e) {
						// TODO: handle exception
					}
				}
			}
		}
		return pos;

	}

	public class AirCraft extends Thread {
		int x;
		int y;

		public AirCraft() {
			x = 250;
			y = 250;
			aircraft=this;
		}
		
		public void run() {
			
			while(!gothit) {
				
				synchronized (aircraft) {
					
					for (int i = 0; i < enemies.size(); i++) {
						if(enemies.get(i)!=null&&x>=enemies.get(i).x-10&&x<=enemies.get(i).x+10) {
							if(y>=enemies.get(i).y-10&&y<=enemies.get(i).y+10) {
								gothit=true;
//								System.out.println("hit the aircraft enemy soldier");
								i--;
								if(!hitfire) {
									
									Frame.ConfirmWindow cw= frame.new ConfirmWindow();
								}
								break;
								
							}
						}
					}
				}
		
			}
			
		}

		
		
	}
	public class Fire extends Thread{
		int x = 0;
		int y = 0;
		String direction;
		String type;
		public Fire(int x,int y,String direction,String type) {
			this.x=x;
			this.y=y;
			this.direction=direction;
			this.type=type;
			if(type.equals("enemy"))fires.add(this);
			else friendly_fires.add(this);
		}
		@Override
		public void run() {
			if(direction.equals("right")) {
				while(x<495) {
					x+=10;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(type.equals("friendly")) {
						synchronized (aircraft) {
							
							for (int i = 0; i < enemies.size(); i++) {
								if(enemies.get(i)!=null&&x<=enemies.get(i).x+10&&x>=enemies.get(i).x) {
									if(y<=enemies.get(i).y+10&&y>=enemies.get(i).y) {
										enemies.get(i).died=true;
										enemies.remove(i);
//										System.out.println(this.direction);
										if(my_fires.contains(this)) {
											
											my_fires.remove(this);
										}
										else {
											friendly_fires.remove(this);
											
										}
										break;
//									System.out.println("hit to enemy");
									}
								}
							}
							if(aircraft!=null&&x<=aircraft.x+10&&x>=aircraft.x) {
								if(y<=aircraft.y+10&&y>=aircraft.y) {
									
//									System.out.println(this.direction);
									
										friendly_fires.remove(this);
//								System.out.println("hit to enemy");
								}
							}
							for (int i = 0; i < friends.size(); i++) {
								if(friends.get(i)!=null&&x<=friends.get(i).x+10&&x>=friends.get(i).x) {
									if(y<=friends.get(i).y+10&&y>=friends.get(i).y) {
//										System.out.println(this.direction);
										
										if(my_fires.contains(this)) {
											
											my_fires.remove(this);
										}
										else {
											friendly_fires.remove(this);
											
										}
											
										
										break;
//									System.out.println("hit to enemy");
									}
								}
							}
						}
					}else if(type.equals("enemy")) {
						synchronized (aircraft) {
							
							for (int i = 0; i < friends.size(); i++) {
								if(friends.get(i)!=null&&x<=friends.get(i).x+10&&x>=friends.get(i).x) {
									if(y<=friends.get(i).y+10&&y>=friends.get(i).y) {
										friends.get(i).died=true;
										friends.remove(i);
										fires.remove(this);
										break;
									}
								}
							}
							if(x<=aircraft.x+10 && x>=aircraft.x-5) {
								if(y<=aircraft.y+10 && y>=aircraft.y-5) {
									hitfire=true;
									fires.remove(this);
//									System.out.println("hit to aircraft");
									aircraft.x=1000;
									aircraft.y=1000;
									if(!win && !gothit ) {
//										System.out.println("enemy fire hit to aircraft");
										Frame.ConfirmWindow cw= frame.new ConfirmWindow();
									}
									break;
								}
							}
						}
						
						
						
					}
				}
				
			}else if(direction.equals("left")) {
				while(x>5) {
					x=x-10;
					try {
						Thread.sleep(100);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(type.equals("friendly")) {
						synchronized (aircraft) {
							
							for (int i = 0; i < enemies.size(); i++) {
								if(enemies.get(i)!=null&&x<=enemies.get(i).x+10&&x>=enemies.get(i).x) {
									if(y<=enemies.get(i).y+10&&y>=enemies.get(i).y) {
										enemies.get(i).died=true;
										enemies.remove(i);
//										System.out.println("enemy removed");
										if(my_fires.contains(this)) {											
											my_fires.remove(this);
										}
										else {
											
											friendly_fires.remove(this);
										}
										break;
//									System.out.println("hit to enemy");
									}
								}
							}
							if(aircraft!=null&&x<=aircraft.x+10&&x>=aircraft.x) {
								if(y<=aircraft.y+10&&y>=aircraft.y) {
									
//									System.out.println(this.direction);
									
										friendly_fires.remove(this);
//								System.out.println("hit to enemy");
								}
							}
							for (int i = 0; i < friends.size(); i++) {
								if(friends.get(i)!=null&&x<=friends.get(i).x+10&&x>=friends.get(i).x) {
									if(y<=friends.get(i).y+10&&y>=friends.get(i).y) {
//										System.out.println(this.direction);
										
										if(my_fires.contains(this)) {
											
											my_fires.remove(this);
										}
										else {
											friendly_fires.remove(this);
											
										}
											
										
										break;
//									System.out.println("hit to enemy");
									}
								}
							}
						}
					}else if(type.equals("enemy")) {
						synchronized (aircraft) { 
							
							for (int i = 0; i < friends.size(); i++) {
								if(friends.get(i)!=null&&x<=friends.get(i).x+10&&x>=friends.get(i).x) {
									if(y<=friends.get(i).y+10&&y>=friends.get(i).y) {
										friends.get(i).died=true;
										friends.remove(i);
										fires.remove(this);
										break;
									}
								}
							}
							if(x<=aircraft.x+10 && x>=aircraft.x-5) {
								if(y<=aircraft.y+10 && y>=aircraft.y-5) {
									hitfire=true;
//									System.out.println("enemy fire hit to aircraft");
									aircraft.x=1000;
									aircraft.y=1000;
									fires.remove(this);
									if(!win && !gothit) {
										
										Frame.ConfirmWindow cw= frame.new ConfirmWindow();
									}
									break;
								}
							}
						}
						
						
						
					}
				}
				
			}

			frame.repaint();
			if(x>490||x<10) {
				if(type.equals("friendly"))
					friendly_fires.remove(this);
				else if(type.equals("enemy"))
					fires.remove(this);
				else
					my_fires.remove(this);
			}
			
		}
		
	}

	public class Enemy extends Thread {
		int x = 0;
		int y = 0;
		Boolean died=false;
		
		public Enemy() {

			x = random.nextInt(1,49) * 10;
			y = random.nextInt(1,49) * 10;
			int[] positions = { x, y };
			int[] pos_new = positionFind(positions);
			starting_positions.add(pos_new);
			x = pos_new[0];
			y = pos_new[1];
			enemies.add(this);
//			System.out.println(this.getId()+" size--->"+enemies.size());
		}

		@Override 
		public void run() {
			int count=0;
			while (!exited) {
				int[]pos;
				synchronized (aircraft) {
					
					pos=random_10(x, y, "enemy");
				}
				x=pos[0];
				y=pos[1];
				frame.repaint();
//				System.out.println(x + "-----" + y);
				
				try {
					Thread.sleep(500);
					if(count!=0 && count%2==0 && enemies.contains(this)) {
						Fire fire_left=new Fire(x, y+5,"left","enemy");
						fire_left.start();
						Fire fire_right= new Fire(x+10, y+5, "right","enemy");
						fire_right.start();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
				if(died)
					break;
			}

		}

	}

	public class Friend extends Thread {
		int x;
		int y;
		Boolean died=false;
		public Friend() {
			x = random.nextInt(1,49) * 10;
			y = random.nextInt(1,49) * 10;
			int[] positions = { x, y };
			int[] pos_new = positionFind(positions);
			starting_positions.add(pos_new);
			x = pos_new[0];
			y = pos_new[1];
			friends.add(this);
		}

		@Override
		public void run() {
			int count=0;
			while (!exited) {
				int[]pos;
				synchronized (aircraft) {
					pos=random_10(x, y, "friendly");
					x=pos[0];
					y=pos[1];
					
				}
				
//				System.out.println(x + "-----" + y);
				try {
					Thread.sleep(500);
					synchronized (aircraft) {
						
						for (int i = 0; i < enemies.size(); i++) {
							if(enemies.get(i)!=null&&x==enemies.get(i).x) {
								if(y==enemies.get(i).y) {

//									System.out.println("enemy and friend removed");
									enemies.get(i).died=true;
									enemies.remove(i);
									friends.remove(this);
									died=true;
									break;
//						System.out.println("hit to enemy");
								}
							}
						}
					}
					if(count!=0 && count%2==0 && friends.contains(this)) {
						Fire fire_left=new Fire(x, y+5,"left","friendly");
						fire_left.start();
						Fire fire_right= new Fire(x+10, y+5, "right","friendly");
						fire_right.start();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
				if(died)
					break;
			}

		}

	}

	class Frame extends JFrame  implements MouseInputListener{
		public Frame() {
			this.setSize(500, 500);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLayout(new BorderLayout());
			this.setVisible(true);
			this.setBackground(Color.white);
			addMouseListener(this);
			addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					

				}

				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_W) {
						if (aircraft.y > 10) {
							aircraft.y -= 10;
						}

					} else if (key == KeyEvent.VK_S) {
						if (aircraft.y < 485) {
							aircraft.y += 10;
						}

					} else if (key == KeyEvent.VK_A) {
						if (aircraft.x > 10) {
							aircraft.x -= 10;
						}

					} else if (key == KeyEvent.VK_D) {
						if (aircraft.x < 485) {
							aircraft.x += 10;
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {

				}
				
			});

		}


		@Override
		public void paint(Graphics g) {
			g.fillRect(0, 0, 10, 10);
			super.paint(g);
			if (enemies != null) {
				int i;

//				System.out.println(enemies.size());
				for (i = 0; i < enemies.size(); i++) {
					if(enemies.get(i)!=null) {
//						System.out.println(enemies.get(i).x+"---"+enemies.get(i).y);
						g.fillRect(enemies.get(i).x , enemies.get(i).y , 10, 10);
//						System.out.println(i);
					}
				}
			}
			g.setColor(Color.green);
			if (friends != null) {
				for (int i = 0; i < friends.size(); i++) {
					if(friends.get(i)!=null)
					g.fillRect(friends.get(i).x, friends.get(i).y, 10, 10);
				}
			}
			if (!gothit) {
				
				
				g.setColor(Color.red);
				g.fillRect(aircraft.x, aircraft.y, 10, 10);
			}
			g.setColor(Color.blue);
			if (fires != null) {
				for (int i = 0; i < fires.size(); i++) {
					if(fires.get(i)!=null)
					g.fillRect(fires.get(i).x, fires.get(i).y, 5, 5);
				}
			}
			g.setColor(new Color(200, 0, 250));
			if (friendly_fires != null) {
				for (int i = 0; i < friendly_fires.size(); i++) {
					if(friendly_fires.get(i)!=null)
					g.fillRect(friendly_fires.get(i).x, friendly_fires.get(i).y, 5, 5);
				}
			}
			g.setColor(Color.orange);
			if (my_fires != null) {
				for (int i = 0; i < my_fires.size(); i++) {
					if(my_fires.get(i)!=null)
					g.fillRect(my_fires.get(i).x, my_fires.get(i).y, 5, 5);
				}
			}
			g.setColor(Color.black);
			
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			Fire fire_left=new Fire(aircraft.x, aircraft.y+5,"left","friendly");
			fire_left.start();
			Fire fire_right= new Fire(aircraft.x+10, aircraft.y+5, "right","friendly");
			fire_right.start();
			my_fires.add(fire_right);
			my_fires.add(fire_left);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}

		



		class ConfirmWindow extends JFrame implements WindowListener {

			public ConfirmWindow() {
				this.setDefaultCloseOperation(EXIT_ON_CLOSE);
				setSize(300, 100);
				setLayout(new FlowLayout());
				JLabel lose_label = new JLabel("       Oyunu kaybettiniz");
				JLabel win_label = new JLabel("        Oyunu kazandınız");
				if(gothit || hitfire)
					add(lose_label);
				else
					add(win_label);

				setVisible(true);
			}

			@Override
			public void windowOpened(WindowEvent e) {
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				frame.setVisible(false);
				frame.dispose();
				System.exit(0);
				closed=true;
				
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}



		}
	}

}
