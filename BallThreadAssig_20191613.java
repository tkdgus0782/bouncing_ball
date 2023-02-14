import java.awt.*;
import java.awt.event.*;
public class BallThreadAssig_20191613 extends Frame implements ActionListener
{  	 private Canvas canvas; 
	public BallThreadAssig_20191613()
   	{  	canvas = new Canvas();

      	add("Center", canvas);
      	Panel p = new Panel();
		Button s = new Button("Start");
		Button c = new Button("Close");
		p.add(s);	p.add(c);
      	s.addActionListener(this);
		c.addActionListener(this);
      	add("South", p);
      	Ball.init();
   	}
	public void actionPerformed(ActionEvent evt)
   	{  	
		if (evt.getActionCommand() == "Start"){  	
   		
   			Ball b;
   	      	clear a = new clear(canvas);
   	      	a.start();
   			b= new Ball(canvas, 150,150,-2,-2,16);
         	b = new Ball(canvas, 150,200,2,-2,16);
         	b = new Ball(canvas, 200,150,-2,2,16);
         	b = new Ball(canvas, 200,200, 2, 2,16);
         	b = new Ball(canvas, 250,150,-2,-2,16);
      	}
      	else if (evt.getActionCommand() == "Close")
         	System.exit(0);
   	}
	public static void main(String[] args)
   	{  	
		Frame f = new BallThreadAssig_20191613();
      	f.setSize(400, 300);
      	WindowDestroyer listener = new WindowDestroyer();  
      	f.addWindowListener(listener);
      	f.setVisible(true); 
   }
}

class Ball extends Thread
{
	public static Ball[] bL;
	public static int len;
	
	private Canvas box;
	private Graphics g;
	private  int XSIZE;
	private  int YSIZE;
	private int x;
	private int y;
	private int dx;
	private int dy;  
	
	Ball(Canvas c, int x, int y, int dx, int dy, int size) 	{ 
  		box = c; 
  		g = box.getGraphics();
  		bL[len++] = this;
  		this.x = x;
  		this.y = y;
  		this.dx = dx;
  		this.dy = dy;
  		this.XSIZE = this.YSIZE = size;
  		this.start();
  	}
  	
	public static void init() {
		bL = new Ball[1000000];
		len = 0;
	}
	
	public static void check() {
		int n = len;
		for(int i=0;i<n;i++) {
			if(bL[i] == null || bL[i].XSIZE < 2) {
				continue;
			}
			
			for(int j = i+1; j<n; j++) {
				if(bL[j]==null || bL[j].XSIZE < 2) {
					continue;
				}			
				
				Ball a = bL[i];
				Ball b = bL[j];
				Ball c;
				
				int dist1 = (int) Math.pow(a.XSIZE/2 + b.XSIZE/2, 2);
				int dist2 = (int) (Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2));
				int dist3 = (int) (Math.pow(a.x+a.dx -b.x-b.dx, 2) + Math.pow(a.y+a.dy -b.y-b.dy, 2));
				
				if(dist1 >= dist2 && dist2 >= dist3) {//둘이 서로 가까워지는 중이고, 충돌 했을 때
					
					a.dx = -a.dx;
					a.dy = -a.dy;
					a.x += 2*a.dx;
					a.y += 2*a.dy;
					a.XSIZE = a.YSIZE /= 2;
									
					b.dx = -b.dx;
					b.dy = -b.dy;
					b.x += 2*b.dx;
					b.y += 2*b.dy;
					b.XSIZE = b.YSIZE /= 2;
										
					if(a.dx * b.dx < 0) {//x방향 다름 ==> --, -+로 나누기
						if(a.XSIZE > 1)
							c = new Ball(a.box, a.x, a.y - 4*a.dy, a.dx, -a.dy, a.XSIZE);
						if(b.XSIZE > 1)
							c = new Ball(b.box, b.x, b.y - 4*b.dy, b.dx, -b.dy, b.XSIZE);
					}
					else {//y방향 다름 ==> --, +-로 나누기
						if(a.XSIZE > 1)	
							c = new Ball(a.box, a.x - 4*a.dx, a.y, -a.dx, a.dy, a.XSIZE);
						if(b.XSIZE > 1)
							c = new Ball(b.box, b.x - 4*b.dx, b.y, -b.dx, b.dy, b.XSIZE);
					}	
				}
			}
		}
	}	
	public void draw(){  	
		g.setColor(new Color(0,0,0));
  		g.fillOval(x, y, XSIZE, YSIZE);
  		g.fillOval(x, y, XSIZE, YSIZE);
  		g.fillOval(x, y, XSIZE, YSIZE);
  		g.fillOval(x, y, XSIZE, YSIZE);
  		g.fillOval(x, y, XSIZE, YSIZE);
  		g.fillOval(x, y, XSIZE, YSIZE);

  	}
	public void erase() {
		g.setColor(new Color(255, 255, 255));
  		g.fillOval(x, y, XSIZE, YSIZE);
	}
	public void move(){  
		g = box.getGraphics();
  		draw();
  		erase();
  		
  		x += dx;	y += dy;
  		check();
  		Dimension d = box.getSize();
  		if (x < 0) { x = 0; dx = -dx; }
  		if (x + XSIZE >= d.width) { x = d.width - XSIZE; dx = -dx; }
  		if (y < 0) { y = 0; dy = -dy; }
  		if (y + YSIZE >= d.height) { y = d.height - YSIZE; dy = -dy; }
  		
  		draw();
  	}
	public void run(){  	
		draw();
  		while(XSIZE >= 2){ 
  			move();
  			if(XSIZE <= 1)
  				return;
     		try { Thread.sleep(5); } catch(InterruptedException e) {}
  		}
  		erase();
	}
}

class clear extends Thread{
	private Canvas c;
	
	clear(Canvas c){
		this.c = c;
	}
	
	public void run(){  	
  		while(true){ 
  			c.getGraphics().clearRect(0,0,400,300);
     		try { Thread.sleep(500); } catch(InterruptedException e) {}
  		}
	}
}

class WindowDestroyer extends WindowAdapter
{
    public void windowClosing(WindowEvent e) 
    {
        System.exit(0);
    }
}
