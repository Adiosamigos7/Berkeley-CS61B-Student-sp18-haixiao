public class Corgi extends Dog {
    public Corgi() {

    }
    public void bark(Corgi c) { System.out.println("Method B"); }
    @Override
    public void bark(Dog d) { System.out.println("Method C"); }
    public void play(Dog d) { System.out.println("Method D");}
    public void play(Corgi c) { System.out.println("Method E"); }



    public static void main(String[] args) {
        Dog d = new Corgi();
        Corgi c = new Corgi();
        d.bark(d);
        d.bark(c);
    }
}