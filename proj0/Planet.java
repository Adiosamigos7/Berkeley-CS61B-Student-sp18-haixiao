/*
import static java.lang.Math;
*/
public class Planet{
  public double xxPos, yyPos, xxVel, yyVel, mass;
  public String imgFileName;
  /* Constructor */
  public Planet(double xP, double yP, double xV,
              double yV, double m, String img) {
    this.xxPos = xP;
    this.yyPos = yP;
    this.xxVel = xV;
    this.yyVel = yV;
    this.mass = m;
    this.imgFileName = img;
  }
  public Planet(Planet b) {
    this.xxPos = b.xxPos;
    this.yyPos = b.yyPos;
    this.xxVel = b.xxVel;
    this.yyVel = b.yyVel;
    this.mass = b.mass;
    this.imgFileName = b.imgFileName;
  }
  /*Calculate distance between two Bodies*/
  public double calcDistance(Planet b) {
    double xsq = (this.xxPos - b.xxPos) * (this.xxPos - b.xxPos);
    double ysq = (this.yyPos - b.yyPos) * (this.yyPos - b.yyPos);
    double dist = java.lang.Math.sqrt(xsq + ysq);
    return dist;
  }
  /*Calculate exerted force*/
  public double calcForceExertedBy(Planet b) {
    double G = 6.67e-11;
    return G * this.mass * b.mass / java.lang.Math.pow(this.calcDistance(b), 2);
  }
  /*Calculate exerted force in X direction*/
  public double calcForceExertedByX(Planet b) {
    double dx = b.xxPos - this.xxPos;
    return this.calcForceExertedBy(b) * dx / this.calcDistance(b);
  }
  /*Calculate exerted force in Y direction*/
  public double calcForceExertedByY(Planet b) {
    double dy = b.yyPos - this.yyPos;
    return this.calcForceExertedBy(b) * dy / this.calcDistance(b);
  }
  /*Calculate net exerted forces in X direction*/
  public double calcNetForceExertedByX(Planet[] bs) {
    int i = 0;
    double fx = 0.0;
    while (i < bs.length) {
      if (! this.equals(bs[i])) {
        fx += this.calcForceExertedByX(bs[i]);
      }
      i += 1;
    }
    return fx;
  }
  /*Calculate net exerted forces in Y direction*/
  public double calcNetForceExertedByY(Planet[] bs) {
    int i = 0;
    double fy = 0.0;
    while (i < bs.length) {
      if (! this.equals(bs[i])) {
        fy += this.calcForceExertedByY(bs[i]);
      }
      i += 1;
    }
    return fy;
  }

  /*Calculate how much forces exerted on the Planet will cause acceleration*/
  public void update(double dt, double fX, double fY) {
    double ax = fX / this.mass;
    double ay = fY / this.mass;
    this.xxVel += dt * ax;
    this.yyVel += dt * ay;
    this.xxPos += dt * this.xxVel;
    this.yyPos += dt * this.yyVel;
  }

  /*Draw Planet*/
  public void draw() {
    StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
  }

}
