package alap;

/**
 *
 * @author Nigel-727
 */
public class Player {
  //OSZTÁLY:
  private static int  nOfInstances = 0; //példányszámláló mint (példány)azonosító;
  public  static byte INVALID_VALUE = //amikor vmit lehet értelmesen beállítani, ezt kapja;
                                     0;
//                                     Byte.MIN_VALUE; 
  public static short TEAM_PLAYER_RATING_DEFAULT = 1200; 
  //PÉLDÁNY adattagok:
  private int     id;
  private long    fide_number; //!: lehet 0 (amikor nincs azonosítója); másnéven: FRD, FIDE azonosító;
  private boolean has_fide_number=false; //?: ez most példányban kezdeti értékadás?
  private String  name;
  private short   rating; //ha van fide_number; másnéven: FIDE rating;
  private byte    K; //ha van fide_number; másnéven: (FIDE) coefficient; 
  private short   b_year;
  private char    sex; //lehet: 'm', 'f';
  //amiknek közül van más (saját) osztályhoz:
  private Team    team;
  //PÉLDÁNY metódusok:
  Player() {
    id = ++nOfInstances;
  }
  //#TODO Alább minden this..=  beállítást setterekkel kellene!;
  Player(long fideAzonosító, String név, short fideÉrtékszám, byte K, short születésiÉv, char nem) 
  {
    this();
    this.name = név;
    this.b_year = születésiÉv;
    this.sex = nem; 
    this.fide_number = fideAzonosító;    
    if (this.fide_number!=0) {
      this.has_fide_number = true;
      this.rating = fideÉrtékszám;
      this.K = K;      
    } else
      this.has_fide_number = false;
  }
  //#TODO lásd fejebb;
  Player(String fide_number, String name, String fide_rating, String coefficient, String birth_year, String sex) 
          throws IllegalArgumentException {
    this();        
    this.name         = name;
    this.b_year   = Short.parseShort(birth_year); //TODO: megírni a settert!!!
    this.sex          = sex.charAt(0); //TODO
    this.fide_number  = Long.parseLong(fide_number); //TODO: megírni a settert!!!    
    if (this.fide_number!=0) { //TODO: tudom h a CSV-ben 0 jelzi a hiányát; ez így csúnya
      this.has_fide_number=true;
      setFideRating(fide_rating);
      setK(coefficient);      
    } else
      this.has_fide_number=false;
  }  
  private void    setFideRating(String fide_rating) {
    try {
      this.rating = Short.parseShort(fide_rating);
    }
    catch (NumberFormatException e)
    {      
      this.rating = INVALID_VALUE;
    }
  }
  private void    setK(String coefficient) {
    try {
      this.K = Byte.parseByte(coefficient);
    }
    catch (NumberFormatException e)
    {
      this.K = INVALID_VALUE;
    }
  }  
  public short    getBirthYear() { //#TODO Lehetne esetleg egy getAge() is
    return this.b_year;
  }
  public String   getName() { 
    String[] tagok = this.name.split("\\ ");
    String ezkellneked = tagok[0].toUpperCase(); //tfh. OK, csak az első tag a vezetéknév
    for (int i = 1; i < tagok.length; i++) 
      ezkellneked += " " 
                  + (""+tagok[i].charAt(0)).toUpperCase()
                  + tagok[i].substring(1).toLowerCase();
    return ezkellneked; 
  }
  public boolean  hasFide() {
    return this.has_fide_number;
  }
  public long     getFideNumber() {
    if (this.has_fide_number)
      return this.fide_number;
    return INVALID_VALUE; //TODO így kell-e az ilyesmit?
  }
  public short    getFideRating() {
    if (this.has_fide_number)
      return this.rating;
    return INVALID_VALUE;
  }
/*
  //#TODO Szabad ilyet csinálni?
  public int      getPlayerID() {
    return this.id;
  }
*/
/*?: Ezt miért írtam bele?
  public Player   getPlayer() { 
    return (this);
  }
*/
  //Amiknek közül van más (saját) osztályhoz:
  public void     setTeam(Team t) {
    this.team = t;
  }
  public Team     getTeam() {
    return this.team;
  }
  @Override
  public boolean  equals(Object obj) {
    if (this == obj) 
      return true;
    if (obj == null) 
      return false;
    if (getClass() != obj.getClass()) 
      return false;
    final Player other = (Player) obj;
    return this.id == other.id;
  }
  @Override
  public String   toString() {
    String ezkellneked = 
            this.getClass().getSimpleName() + "{ ";
    ezkellneked += 
              extra.Format.right(this.id,3) + "| " //!!!
            + extra.Format.right(this.fide_number,8) + "| "
//            + extra.Format.left(this.name,30) + "| "
            + extra.Format.left(this.getName(),30) + "| "
            + extra.Format.right(this.rating,4) + "| "
            + extra.Format.right(this.K,2) + "| "
            + extra.Format.right(this.b_year,4) + "| "
            + this.sex + "| ";
    return ezkellneked + "}";
//    return super.toString(); //To change body of generated methods, choose Tools | Templates.
  }
}//class Player