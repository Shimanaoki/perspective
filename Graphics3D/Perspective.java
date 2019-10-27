package Graphics3D;

import java.awt.*;
import javax.swing.*;

import java.awt.Graphics;
import java.lang.Math;

public class Perspective{

    public Observer observer;
    private final int screensize, center;
    private int PROJECTION_TYPE;

    public static final int PANORAMA = 0;
    public static final int FISHEYE = 1;

    public Perspective(double x, double y, double z, int theta, int phi, int screensize, int center, int PROJECTION_TYPE){

        this.observer = new Observer(x,y,z,theta,phi);
        this.screensize = screensize;
        this.center = center;
        this.PROJECTION_TYPE = PROJECTION_TYPE;

    }

    public class Observer{

        private double x, y, z;
        private int theta, phi;

        public Observer(double x, double y, double z, int theta, int phi){

            if(-90 < theta && theta < 90){

                this.x = x;
                this.y = y;
                this.z = z;
                this.theta = theta; 
                this.phi = phi;
                
            }

        }public Observer(){new Observer(0, 0, 0, 0, 0);}

        public void setPosition(double x, double y, double z){

            this.x = x;
            this.y = y;
            this.z = z;

        }

        public void setDirection(int theta, int phi){

            if(-90 < theta && theta < 90){

                this.theta = theta; 
                this.phi = phi;

            }
        }

        public double getX(){
            return this.x;
        }

        public double getY(){
            return this.y;
        }

        public double getZ(){
            return this.z;
        }

        public int getTheta(){
            return this.theta;
        }

        public int getPhi(){
            return this.phi;
        }

        public void go(double displacement){
            this.z += displacement*Math.cos(this.phi * Math.PI / 180);
            this.x += displacement*Math.sin(this.phi * Math.PI / 180);
            // System.out.println(this.x);
            // System.out.println(this.y);
            // System.out.println(this.z);
        }

        public void goBack(double displacement){
            this.z -= displacement*Math.cos(this.phi * Math.PI / 180);
            this.x -= displacement*Math.sin(this.phi * Math.PI / 180);
            // System.out.println(this.x);
            // System.out.println(this.y);
            // System.out.println(this.z);
        }
        
        public void Rise(double displacement){
            this.y += displacement;
            // System.out.println(this.x);
            // System.out.println(this.y);
            // System.out.println(this.z);
        }
        
        public void Down(double displacement){
            this.y -= displacement;
            // System.out.println(this.x);
            // System.out.println(this.y);
            // System.out.println(this.z);
        }

        public void goRight(double displacement){
            this.z -= displacement*Math.sin(this.phi * Math.PI / 180);
            this.x += displacement*Math.cos(this.phi * Math.PI / 180);
            // System.out.println(this.x);
            // System.out.println(this.y);
            // System.out.println(this.z);
        }

        public void goLeft(double displacement){
            this.z += displacement*Math.sin(this.phi * Math.PI / 180);
            this.x -= displacement*Math.cos(this.phi * Math.PI / 180);
            // System.out.println(this.x);
            // System.out.println(this.y);
            // System.out.println(this.z);
        }

        public void lookUp(){
            if(this.theta < 80){
                this.theta += 1;
                // System.out.println(this.theta);
            }
        }

        public void lookDown(){
            if(this.theta > -80){
                this.theta -= 1;
                // System.out.println(this.theta);
            }
        }

        public void turnRight(){
            this.phi += 1;
            // System.out.println(this.phi);
        }

        public void turnLeft(){
            this.phi -= 1;
            // System.out.println(this.phi);
        }
    }

    public void set_Observer(Observer O){
        
        this.observer = O;

    }

    public void switchProjection(int PROJECTION_TYPE){

        this.PROJECTION_TYPE = PROJECTION_TYPE;

    }

    public int getProjectionTYPE(){

        return this.PROJECTION_TYPE;
    }

    private class Coordinate3D{

        double x, y, z;

        public Coordinate3D(double x, double y, double z){

            this.x = x;
            this.y = y;
            this.z = z;

        }
    }
    
    private class Projection_Map{

        double xi, mu;
        boolean front = true;

        public Projection_Map(double xi, double mu, Boolean front){

            this.xi = xi;
            this.mu = mu;
            this.front = front;
            
        }
    }

    private Coordinate3D Transformation(double x, double y, double z){

        double x_o = observer.getX();
        double y_o = observer.getY();
        double z_o = observer.getZ();
        double theta = observer.getTheta()%360 * Math.PI / 180;
        double phi = observer.getPhi()%360 * Math.PI / 180;

        double Costheta = Math.cos(theta);
        double Cosphi = Math.cos(phi);
        double Sintheta = Math.sin(theta);
        double Sinphi = Math.sin(phi);

        Coordinate3D r, tr, ttr;
        
        r = new Coordinate3D(x-x_o, y-y_o, z-z_o);
        tr = new Coordinate3D(Cosphi*r.x - Sinphi*r.z, r.y, Sinphi*r.x + Cosphi*r.z);
        ttr =  new Coordinate3D(tr.x, Costheta*tr.y - Sintheta*tr.z, Sintheta*tr.y + Costheta*tr.z);

        return ttr;
    }

    private Projection_Map Projection_Mapping(Coordinate3D r){

        double xi, mu;
        boolean front = true;

        Projection_Map pm;

        if(r.z <= 0){

            front = false;
            xi = 0;
            mu = 0;

        } else if(r.x == 0 && r.y == 0) {

            xi = 0;
            mu = 0;

        } else {

            switch (PROJECTION_TYPE) {
                case PANORAMA:
                    xi = 2/Math.PI*Math.atan(r.x/r.z);
                    mu = 2/Math.PI*Math.atan(r.y/Math.sqrt(r.x*r.x + r.z*r.z));
                    break;

                case FISHEYE:
                    double rho = 2/Math.PI*Math.asin(Math.sqrt((r.x*r.x + r.y*r.y)/(r.x*r.x + r.y*r.y + r.z*r.z)));
                    xi = rho*r.x/Math.sqrt(r.x*r.x + r.y*r.y);
                    mu = rho*r.y/Math.sqrt(r.x*r.x + r.y*r.y);
                    break;

                default:
                    xi = 2/Math.PI*Math.atan(r.x/r.z);
                    mu = 2/Math.PI*Math.atan(r.y/Math.sqrt(r.x*r.x + r.z*r.z));
                    break;
            }
        }

        return new Projection_Map(xi, mu, front);
    }

    public void projectionLine(Graphics g, double x0, double y0, double z0 , double x1, double y1, double z1, double fine){

        double dx, dy, dz;
        int px0, py0, px1, py1;
        Projection_Map p0, p1;


        dx = (x1 - x0)/fine;
        dy = (y1 - y0)/fine;
        dz = (z1 - z0)/fine;

        p0 = Projection_Mapping(Transformation(x0, y0, z0));

        px0 = (int)(this.screensize/2*p0.xi + this.center);
        py0 = (int)(-this.screensize/2*p0.mu + this.center);


        for(int i = 1; i <= fine; i++){

            p1 = Projection_Mapping(Transformation(x0 + dx*i, y0 + dy*i, z0 + dz*i));

            px1 = (int)(this.screensize/2*p1.xi + this.center);
            py1 = (int)(-this.screensize/2*p1.mu + this.center);

            if(p0.front && p1.front){
                g.drawLine(px0, py0, px1, py1);
            }

            p0 = p1;

            px0 = px1;
            py0 = py1;
        }
    }

    public void projectionQube(Graphics g, double x0, double y0, double z0, double x1, double y1, double z1, double fine){

        this.projectionLine(g, x0, y0, z0, x0, y0, z1, fine);
        this.projectionLine(g, x0, y0, z0, x0, y1, z0, fine);
        this.projectionLine(g, x0, y0, z0, x1, y0, z0, fine);

        this.projectionLine(g, x0, y0, z1, x1, y0, z1, fine);
        this.projectionLine(g, x0, y0, z1, x0, y1, z1, fine);

        this.projectionLine(g, x0, y1, z0, x1, y1, z0, fine);
        this.projectionLine(g, x0, y1, z0, x0, y1, z1, fine);

        this.projectionLine(g, x1, y0, z0, x1, y1, z0, fine);
        this.projectionLine(g, x1, y0, z0, x1, y0, z1, fine);

        this.projectionLine(g, x1, y1, z1, x1, y1, z0, fine);
        this.projectionLine(g, x1, y1, z1, x1, y0, z1, fine);
        this.projectionLine(g, x1, y1, z1, x0, y1, z1, fine);
    }
}