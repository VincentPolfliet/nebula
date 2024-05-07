package dev.vinpol.nebula.javaGOAP.cookie;

public class Cookie {

    public static final String IN_HAND = "cookieInHand";
    public static final String EAT = "eatingCookie";

    private boolean eaten = false;

    public boolean isEaten() {
        return eaten;
    }

    public void eat() {
        this.eaten = true;
    }

    @Override
    public String toString() {
        return "Cookie{" +
               "eaten=" + eaten +
               '}';
    }
}
