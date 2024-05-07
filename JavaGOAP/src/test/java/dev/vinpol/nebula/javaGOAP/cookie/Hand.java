package dev.vinpol.nebula.javaGOAP.cookie;

public class Hand {

    private boolean cookieInHand;

    public boolean isCookieInHand() {
        return cookieInHand;
    }

    public void setCookieInHand() {
        cookieInHand = true;
    }

    public void setCookieInHand(boolean result) {
        cookieInHand = result;
    }

    @Override
    public String toString() {
        return "Hand{" +
               "cookieInHand=" + cookieInHand +
               '}';
    }
}
