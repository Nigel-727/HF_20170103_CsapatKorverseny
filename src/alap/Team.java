package alap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Nigel-727
 */
public class Team {
  //OSZTÁLY adattagok:
  public static byte INVALID_VALUE = Byte.MIN_VALUE;
  public static byte PLAYERS_ON_A_TEAM = 4; //hány fősnek kell lennie egy csapatnak; #TODO Lehessen szebben beállítani  
  private static int nOfInstances = 0; //példányszámláló mint (példány)azonosító
  //példány adattagok:
  private int id;
  private String name;
  private ArrayList<Player> players = new ArrayList<>();  
  private boolean is_sorted_by_rating=false;   
  //PÉLDÁNY metódusok:
  public boolean  sortPlayersByRating() { //rendezés rating szerint csökkenőbe; könnyebbség kedvéért; #TODO: A felhasználó is megtehesse!;
  //de csak HA még nem rendezett && egyik játékosuk sem kezdte el a versenyt #TODO utóbbit hogyan kezeljük?
    if (this.is_sorted_by_rating) 
      return false; //mivel nem szabad többször
    Collections.sort(players, new Comparator<Player>() { //(ezúttal) fide rating szerinti rendezést kérünk!
      @Override
      public int compare(Player p1, Player p2) {
        short rating1, rating2;
        rating1 = p1.hasFide() ? p1.getFideRating() : Player.TEAM_PLAYER_RATING_DEFAULT;
        rating2 = p2.hasFide() ? p2.getFideRating() : Player.TEAM_PLAYER_RATING_DEFAULT;
        return (int)Math.signum(rating2-rating1); //mivel _csökkenőt_ akarunk
      }
    });
    return is_sorted_by_rating=true; 
  }
  @Override
  public boolean  equals(Object obj) {
    if (this == obj) 
      return true;
    if (obj == null) 
      return false;
    if (getClass() != obj.getClass()) 
      return false;
    return ((Team)obj).id == this.id;
  }
  @Override
  public String   toString() {
    String ezkellneked = 
            this.getClass().getSimpleName()
            +"{" 
//            + "id=" + this.id //#teszt
            + "| csapatnév:\"" + this.name + "\""
            + "| játékosok:[";
    int játékosokszáma = this.players.size();
    if (0<játékosokszáma) {
      ezkellneked += "\n";
      for (int i = 0; i < játékosokszáma-1; i++) {
        ezkellneked += "..."+ this.players.get(i) + "\n";
      } 
      ezkellneked += "..."+ this.players.get(játékosokszáma-1);//#TODO
    }
    return ezkellneked + "]}";
  }  
  public int      getNumberOfPlayers() { //adott csapatnak aktuálisan ennyi játékosa van; feltöltéskor hasznos
    return this.players.size();
  }
  public boolean  hasPlayers() {  //#TODO lehet h nem szép, hiszen getNumberOfPlayers() is megmondaná
    return 0<this.players.size();
  }  
  public String   getName() {
    return this.name;
  }
  public void     setName(String name) {
    this.name = name;
  }
  public boolean  addPlayer(Player newplayer) {    
    if (this.players.size()<PLAYERS_ON_A_TEAM)
      return this.players.add(newplayer);
    return false;
  }
  public Player   getPlayer(int index) { 
    return ( index<this.players.size() && 0<=index ) 
            ? this.players.get(index) : null;
  }
  Team() {
    id = ++nOfInstances;
  }
  Team(String name, Player... newplayers) throws IllegalArgumentException {
    this();
    this.setName(name);
    try {
      for (int i = 0; i < newplayers.length; i++) {
        this.addPlayer(  newplayers[i]  );
      }    
    }
    catch (IllegalArgumentException e)
    {
      ;
    }
  }
}//class Team