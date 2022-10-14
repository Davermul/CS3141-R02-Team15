import java.util.Scanner;
import java.util.ArrayList;
public class Room {

    public static Player player;
    public static Dealer dealer;
    public static Deck deck;
    public static Scanner scanner;

    public  static Room room;

    public Room(){
        player = new Player();
        player.setMoney(1000);
        dealer = new Dealer();
        deck = new Deck();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        //start gaming
        Room room = new Room();
        System.out.println("Welcome to play BlackJack");
    }

}
// see you...........someone help ~~~~~