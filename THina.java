import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import java.lang.Math;

import Graphics3D.Perspective;
import Graphics3D.Perspective.*;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class THina extends JFrame{

    Perspective P;

    public THina(){
        setTitle("Perspective");
        setSize(600, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClosing());
        TPanel tPanel = new TPanel();
        Container contentPane = getContentPane();
        contentPane.add(tPanel);
        setVisible(true);

        P = new Perspective(0,0,0,0,0, 500, 300, Perspective.FISHEYE);
    }

    public static void main(String[] args) {
        new THina();
    }
    
    public class TPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, KeyListener{

        double x0 = 3, y0 = 3, z0 = 3;
        double x1 = -3, y1 = -3, z1 = -3;
        int i, j, fine = 100, k = 0;
        public final Timer timer = new Timer(1,this);

        private TPanel(){
            setBackground(Color.BLACK);
            getContentPane().addMouseListener(this);
            getContentPane().addMouseMotionListener(this);
            setFocusable(true);
            addKeyListener(this);
        }
    
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;

            setFocusable(true);

            g.setColor(Color.WHITE);
            if(P.getProjectionTYPE() == Perspective.FISHEYE)g.fillOval(50, 50, 500, 500);
            if(P.getProjectionTYPE() == Perspective.PANORAMA)g.fillRect(50, 50, 500, 500);
            g.setColor(Color.BLACK);
            //-------------------------------------------------------------------------------------------------

            P.projectionQube(g, x0+Math.cos(k*0.01)*1, y0+Math.cos(k*0.01)*1, z0+Math.cos(k*0.01)*1, x1-Math.cos(k*0.01)*1, y1-Math.cos(k*0.01)*1, z1-Math.cos(k*0.01)*1, fine);
            P.projectionQube(g, x0, y0, z0, x1, y1, z1, fine);
            P.projectionQube(g, x0-Math.cos(k*0.01)*1, y0-Math.cos(k*0.01)*1, z0-Math.cos(k*0.01)*1, x1+Math.cos(k*0.01)*1, y1+Math.cos(k*0.01)*1, z1+Math.cos(k*0.01)*1, fine);

        }

        public void mouseClicked(MouseEvent me)
        {
            if(P.getProjectionTYPE() == Perspective.FISHEYE){
                P.switchProjection(Perspective.PANORAMA);
            } else if(P.getProjectionTYPE() == Perspective.PANORAMA){
                P.switchProjection(Perspective.FISHEYE);
            }
            repaint();
        }
        public void mousePressed(MouseEvent me)
        {
        }
        public void mouseReleased(MouseEvent me)
        {
        }
        public void mouseExited(MouseEvent me)
        {
        }
        public void mouseEntered(MouseEvent me)
        {
        }
        public void mouseMoved(MouseEvent me)
        {
        }
        public void mouseDragged(MouseEvent me)
        {
        }

        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == timer) {
                k++;
            }
            repaint();
        }

        public void keyPressed(KeyEvent e)
		{
            double displacement = 0.2;
            int key = e.getKeyCode();
            
            if(key == KeyEvent.VK_T){
                timer.start();
            }
            if(key == KeyEvent.VK_P){
                timer.stop();
            }

            switch (key) {
                case KeyEvent.VK_UP:
                    P.observer.lookUp();
                    break;
                case KeyEvent.VK_DOWN:
                    P.observer.lookDown();
                    break;
                case KeyEvent.VK_RIGHT:
                    P.observer.turnRight();
                    break;
                case KeyEvent.VK_LEFT:
                    P.observer.turnLeft();
                    break;
                case KeyEvent.VK_W:
                    P.observer.go(displacement);
                    break;
                case KeyEvent.VK_S:
                    P.observer.goBack(displacement);
                    break;
                case KeyEvent.VK_SPACE:
                    P.observer.Rise(displacement);
                    break;
            }

            switch (key) {
                case KeyEvent.VK_X:
                    P.observer.Down(displacement);
                    break;
                case KeyEvent.VK_D:
                    P.observer.goRight(displacement);
                    break;
                case KeyEvent.VK_A:
                    P.observer.goLeft(displacement);
                    break;
                case KeyEvent.VK_O:
                    System.out.println(P.observer.getX());
                    System.out.println(P.observer.getY());
                    System.out.println(P.observer.getZ());
                    System.out.println(P.observer.getTheta());
                    System.out.println(P.observer.getPhi());
                    break;
                default:
                    break;
            }
			repaint();
		}
		public void keyReleased(KeyEvent e)
		{
		}
		public void keyTyped(KeyEvent e)
		{
		}
    }


    class WindowClosing extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            int ans = JOptionPane.showConfirmDialog(THina.this, "Exit now?");
            if (ans == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}