package alap;

/**
 *
 * @author Nigel-727
 */
public class Game {
  //OSZTÁLY adattagok
  public static final byte  INVALID_VALUE = Byte.MIN_VALUE; //mire kell itt?
  public static final int   WHITEWINS=1, BLACKWINS=3, DRAW=2; //hogy ne gépelhessük el máshol
  private static int        nOfInstances = 0; //példányszámláló; #TODO Hol használjuk?
  //PÉLDÁNY adattagok:
  private int     id; 
  private int     result; //1:világos nyert;2:döntetlen;3:sötét nyert;stb  
  private boolean has_result=false; //ha már beállítottuk (=van végeredménye); TODO mire kell?
  //amiknek közük van más (saját) osztályhoz:
  private Player  white_player;
  private Player  black_player;
  //OSZTÁLY metódusok:
  public static int ratingChange(int my_rating, int my_coefficient, int your_rating) {
    return 0; //#TODO Képlettel kiszámolni a saját értékszámváltozást; ?: ezt a metódust hovakénerakni?
  }
  //PÉLDÁNY metódusok:
  public String   getResult() {    
    return this.result==DRAW ? "½-½"
            : (this.result==WHITEWINS ? "1:0" : "0:1");
  }
  public boolean  setResult(int res) {
    boolean siker=false;
    switch (res) {
      case WHITEWINS: case DRAW: case BLACKWINS:
        this.result=res;
        siker=true;
      break;      
    }
    return this.has_result=siker;
  }
  public boolean  setResult(char res) { //'w','x','b'; TODO Hibás értékeket kiszűrni!!!
    boolean siker=false;
    switch (res) {
      case 'w': case 'x': case 'b':
        this.result = res=='x' ? DRAW : (res=='w' ? WHITEWINS : BLACKWINS);
        siker=true;
      break;
    }
    return this.has_result=siker;
  }
  public double   getWhitePoint() {
    return this.result==DRAW ? 0.5
            : (this.result==WHITEWINS ? 1 : 0);
  }
  public double   getBlackPoint() {
    return this.result==DRAW ? 0.5
            : (this.result==WHITEWINS ? 0 : 1);
  }
  public boolean  hasResult() {
    return this.has_result;
  }
  //amiknek közük van más (saját) osztályhoz:
  public Player   getWhite() { //TODO Valszeg nem így kéne
    return this.white_player;
  }
  public Player   getBlack() {
    return this.black_player;
  }
  public void     setWhite(Player white) {
    this.white_player=white; //#TODO Hibaellenőrzéssel!
  }
  public void     setBlack(Player black) {
    this.black_player=black; //#TODO (itt is)
  }
  Game() {
    id = ++nOfInstances;
    has_result=false;
  }
  Game(Player white, Player black) {
    this();
    setWhite(white);
    setBlack(black);
  }  
  Game(Player white, Player black, char result) {
    this(white, black);
    setResult(result);
  }          
}//class Game