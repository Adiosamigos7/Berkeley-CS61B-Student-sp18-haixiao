public class NBody {
    private int N, R;
    /*Return a double corresponding to the radius of the universe in that file*/
    public static double readRadius (String filename) {
        /* Start reading in national_salt_production.txt */
        In in = new In(filename);
        int r = in.readInt();
        double radius = in.readDouble();
        return radius;
    }
    /* return an array of Planets corresponding to the Planets in the file*/
    public static Planet[] readPlanets (String filename) {
        In in = new In(filename);
        int size = in.readInt();
        double radius = in.readDouble();
        double xxPos, yyPos, xxVel, yyVel, mass;
        String imgFileName;
        Planet[] Planets = new Planet[size];
        for(int i = 0; i < size; i += 1) {
            xxPos = in.readDouble();
            yyPos = in.readDouble();
            xxVel = in.readDouble();
            yyVel = in.readDouble();
            mass = in.readDouble();
            imgFileName = in.readString();
            Planet b = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
            Planets[i] = b;
        }
        return Planets;
    }
    public static void main(String[] args) {
        /*Read in planet data*/
        String[] a = new String[3];
        /**a[0] = "157788000.0";
        a[1] = "25000.0";
        a[2] = "data/planets.txt";
        /**double T = Double.parseDouble(a[0]);
        double dt = Double.parseDouble(a[1]);
        String filename = a[2]; */

        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double radius = readRadius(filename);
        Planet[] Planets = readPlanets(filename);
        System.out.println(Planets.length);
        /*Start to draw.*/
        StdDraw.enableDoubleBuffering();
        /* Sets up the scale of the canvas to be the universe radius */
        StdDraw.setScale(-radius, radius);
        /* Draw starfield background. */
        StdDraw.picture(0, 0, "images/starfield.jpg", radius * 2, radius * 2);
        StdDraw.show();
        /* Draw Planets*/
        for (int i = 0; i < Planets.length; i += 1) {
            Planets[i].draw();
            StdDraw.show();
        }
        /* Draw animation */
        double time = 0;
        while (time <= T) {
            StdDraw.clear();
            System.out.println(time);
            StdDraw.picture(0, 0, "images/starfield.jpg", radius * 2, radius * 2);
            double [] xForces = new double[Planets.length];
            double [] yForces = new double[Planets.length];
            for (int i = 0; i < Planets.length; i += 1) {
                xForces[i] = Planets[i].calcNetForceExertedByX(Planets);
                yForces[i] = Planets[i].calcNetForceExertedByY(Planets);
            }
            for (int i = 0; i < Planets.length; i += 1) {
                Planets[i].update(time, xForces[i], yForces[i]);
            }
            for (int i = 0; i < Planets.length; i += 1) {
                Planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }
        /*Print the Universe*/
        StdOut.printf("%d\n", Planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < Planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    Planets[i].xxPos, Planets[i].yyPos, Planets[i].xxVel,
                    Planets[i].yyVel, Planets[i].mass, Planets[i].imgFileName);
        }
    }
}
