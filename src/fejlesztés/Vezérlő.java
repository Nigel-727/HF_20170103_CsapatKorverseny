package fejlesztés;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nigel-727
 */
//////////////////////////////
class Csapat 
        implements Kalapadat_Csapatok {
  public  static int csapatlétszám = 4; //#TODO ne itt legyen (aktuális versenytől függjön, az állítsa be)
  private static int nOfInstances = 0; //egyedi példányazonosítóhoz kell
  private static Set<String> felhasználhatóKépzeltNevek = new HashSet<>(Arrays.asList(TEAMNAMEs)); //#iLoveJava
  private static HashSet<String> felhasználtNevek = new HashSet<>(); //FONTOS h minden név egyedi legyen
  //
  private int id;
  private String név = null; //fontos a kezdeti érvénytelen érték
  private ArrayList<Játékos> tagok = new ArrayList<>(csapatlétszám);
  private boolean véglegesítve = false; //tagok és táblasorrend változhat-e
  //
  private Csapat() { //a konstruktorok ne legyenek public-ok, hogy csak a Csapatverseny.getCsapatInstance() tudja őket meghívni
    this.id=++nOfInstances;
  }
  public  Csapat(String név) {
    this();
    this.setNév(név); //#TODO vizsgálni hogy sikerült-e
  }
  public  Csapat(int mitakarsz) { //#TODO #béna máshogyan kell
    this();
    if (mitakarsz==KÉPZELT_CSAPAT) //felesleges vizsgálat, eleve csak akkor jutunk ide amikor int a paraméter
      this.setNév(this.getNév());
  }
  public boolean  addTag(Játékos újtag) {
    if (tagok.size()<csapatlétszám) {
      tagok.add(újtag); 
      return true;
    }
    return false;
  }
  public void     setNév(String név)  
          throws IllegalArgumentException { 
    String kisbetűsNév = név.toLowerCase();
    if (felhasználtNevek.contains(kisbetűsNév))
      throw new IllegalArgumentException("ezzel a névvel (\""+név+"\") már van csapat");
    this.név=név;
    felhasználtNevek.add(kisbetűsNév);
    felhasználhatóKépzeltNevek.remove(név); //#TODO kis-nagybetűfüggetlenül kell
  }
  public String   getNév() 
          throws IllegalArgumentException {
    if (this.név!=null)
      return this.név;
//    ezkellneked = TEAMNAMEs[(int)(Math.random()*TEAMNAMEs.length)]; //így volt eredetileg, de egyedi név kell neked
    if (felhasználhatóKépzeltNevek.isEmpty())
      throw new IllegalArgumentException("elfogytak a felhasználható képzeltcsapat-nevek");
    Object[] csapatnevek = felhasználhatóKépzeltNevek.toArray(); //#nemszép #hogykellmáshogy
    return csapatnevek[(int)(Math.random()*csapatnevek.length)].toString(); 
  }
  public double   getFideÉrtékszámÁtlag() {
    double sum = 0;
    int size = tagok.size();
    for (int i = 0; i < size; i++) 
      sum += tagok.get(i).getFideÉrtékszám();
    return sum / size;
  }
  public boolean  setTáblasorrend() {
    //#TODO vizsgálni h megvan-e  a létszám
    if (this.véglegesítve) 
      return false; //mivel nem szabad többször
    this.rendezÉrtékszámszerint(); //egyelőre csak így lehet a táblasorrendet véglegesíteni
    this.véglegesítve=true;
    return true;
  }
  private void    rendezÉrtékszámszerint() { //rendezés rating szerint csökkenőbe; könnyebbség kedvéért; #TODO: A felhasználó is megtehesse!;
    Collections.sort(tagok, new Comparator<Játékos>() { //(ezúttal) fide rating szerinti rendezést kérünk!
      @Override
      public int compare(Játékos p1, Játékos p2) {
        short rating1, rating2;
        rating1 = /*p1.hasFide() ?*/ p1.getFideÉrtékszám()/* : Player.TEAM_PLAYER_RATING_DEFAULT*/;
        rating2 = /*p2.hasFide() ?*/ p2.getFideÉrtékszám()/* : Player.TEAM_PLAYER_RATING_DEFAULT*/;
        return (int)Math.signum(rating2-rating1); //mivel _csökkenőt_ akarunk
      }
    });
  }
  public String   toString() {
    String ezkellneked = "csapatnév: \""+this.getNév()+"\""
            +"; értékszámátlag: "+extra.Format.left(this.getFideÉrtékszámÁtlag(), 0, 0)
            +"\njátékosai:\n";
    for (Játékos játékos : tagok) 
      ezkellneked += "\t"+játékos.getNév()+" "+játékos.getFideÉrtékszám()+"\n";
    return ezkellneked;
  }
}
//////////////////////////////
class Játékos implements Kalapadat_Játékosok {
  
  protected static int nOfInstances = 0;
  private static HashSet<String> felhasználtFideIDk = new HashSet<>(); //FONTOS h minden fideID egyedi legyen
  //
  protected int     id;
  protected String  fideID = null;
  protected String  név = null;  
  protected short   fideRating = FIDERATING_MIN-1;
  protected byte    fideK = (byte)(Collections.min(Arrays.asList(FIDEKs))-1); //#iLoveJava
  protected short   születésiÉv = SZÜLÉV_MIN-1;
  protected char    nem = HIBÁS_NEM;
  public Játékos() {
    id=++nOfInstances;
  }
  public Játékos(String... strArr) {
    this();
    this.fideID      = strArr[0];
    this.név         = strArr[1];
    this.fideRating  = Short.parseShort(strArr[2]);
    this.fideK       = Byte.parseByte(strArr[3]);
    this.születésiÉv = Short.parseShort(strArr[4]);
    this.nem         = strArr[5].charAt(0);
  }
//  /* //hívva: CsapatJátékos konstruktorában
  public Játékos(Játékos játékos) { //the "copy constructor"
    this();
    this.fideID=játékos.fideID;
    this.név=játékos.név;
    this.fideRating=játékos.fideRating;
    this.fideK=játékos.fideK;
    this.születésiÉv=játékos.születésiÉv;
    this.nem=játékos.nem;
  }
//  */
  public Játékos(int mitakarsz) { //#ezígycsúnya 
    this();
    if (mitakarsz==KÉPZELT_JÁTÉKOS) {
//      this.fideID=this.getFideID(); //ez volt eredetileg
      this.setFideID(this.getFideID()); 
      //#TODO a maradékhoz is megírni a settert:
      this.születésiÉv=this.getSzületésiÉv();
      this.nem=this.getNem();  //FONTOS a !sorrend! (előbb)
      this.név=this.getNév(); //FONTOS !sorrend! (utóbb)
      this.fideRating=this.getFideÉrtékszám();
      this.fideK=this.getFideSzorzó(); //#TODO értékszámtól+szülévtől is függjön
    }
  }
  public void       setFideID(String újID) 
          throws IllegalArgumentException {
    if (felhasználtFideIDk.contains(újID))
      throw new IllegalArgumentException("ezzel a FIDE azonosítóval (\""+újID+"\") már van játékos");
    this.fideID=újID;
    felhasználtFideIDk.add(újID);
  }
  public String     getFideID() {
    if (this.fideID!=null)
      return this.fideID;
    return Long.toString((long)(Math.random()*(FIDEID_MAX-FIDEID_MIN+1)+FIDEID_MIN));
  }
  public String     getNév() 
          throws IllegalArgumentException {
    if (this.név!=null)
      return this.név;
    EnSex mianeme;
    switch (this.nem) { 
      case 'm': //#csúnya így beleégetve; #TODO megoldani
        mianeme = EnSex.male;
        break;
      case 'f':
        mianeme = EnSex.female;
        break;
      default: //transzvesztita...?
        throw new IllegalArgumentException("nem szabadott volna idáig lesüllyedni"); // #hoppá
    }
    String[] keresztnevek = (mianeme == EnSex.male ? FIRSTNAMES_MALE : FIRSTNAMES_FEMALE);
    return LASTNAMEs[(int)(Math.random()*LASTNAMEs.length)] + " "
            + keresztnevek[(int)(Math.random()*keresztnevek.length)];
  }
  public short      getFideÉrtékszám() {
    if(FIDERATING_MIN<=this.fideRating && this.fideRating<=FIDERATING_MAX)
      return this.fideRating;
    return (short)(Math.random()*(FIDERATING_MAX-FIDERATING_MIN+1)+FIDERATING_MIN);
  }
  public byte       getFideSzorzó() {
    if (Arrays.asList(FIDEKs).contains(this.fideK)) {
//      System.out.println("van szorzója");
      return this.fideK;
    }
    return FIDEKs[(int)(Math.random()*FIDEKs.length)];
  }
  public short      getSzületésiÉv() {
    if (SZÜLÉV_MIN<=this.születésiÉv && this.születésiÉv<=SZÜLÉV_MAX)
      return this.születésiÉv;
    return (short)(Math.random()*(SZÜLÉV_MAX-SZÜLÉV_MIN+1)+SZÜLÉV_MIN);
  }
  public char       getNem() {
    if (Arrays.asList(SEXes).contains(Character.toString(this.nem))) { //!!!
//      System.out.println("van neme");
      return this.nem;
    }
//    System.out.println("újnem");
    return SEXes[(int)(Math.random()*SEXes.length)].charAt(0);
  }
  public String     toString() { // #nemámcsakúgynatúr hanem #getterekkel
    return "|"+extra.Format.right(
            this.getFideID(), 8)+", "
            +this.getNév()+", "
            +this.getFideÉrtékszám()+", "
            +this.getFideSzorzó()+", "
            +this.getSzületésiÉv()+", "
            +this.getNem()+"|";
  }
}
//////////////////////////////
class CsapatJátékos extends Játékos {
  private Csapat csapat;
  public CsapatJátékos(Játékos játékos, Csapat csapat) {
    super(játékos); //a Játékos(Játékos e)-t hívja, vagyis a copy konstruktort!?
    this.csapat=csapat;
  }  
  public String toString() {
    return super.toString() + " csapata: "+this.csapat.getNév();
  }
}
//////////////////////////////
class Játszma {
  private char eredmény; //'1' 'x' '2'
  private Játékos világos, sötét;
}
//////////////////////////////
class Csapatverseny {
  public static int SORTEDBY_NAME   = 1,
                    SORTEDBY_RATING = 2;
  public static int ORDER_ASCENDING   = 1,
                    ORDER_DESCENDING  = 2;
  private Csapatverseny() {
    ;
  }
  public static Csapatverseny getCsapatversenyInstance() { //#énistudokilyetírni #bibi
    Csapatverseny csv;
    csv = new Csapatverseny();
    return csv;
  }
  private ArrayList<CsapatJátékos> résztvevőJátékosok = new ArrayList<>();
  private HashSet<Csapat> csapathalmaz = new HashSet<>();
  private ArrayList<Csapat> résztvevőCsapatok = new ArrayList<>();
  public boolean addPár(Játékos újjátékos, Csapat újcsapat) {
    boolean siker=true; //!: kezdetben TRUE;
    CsapatJátékos újcsapatjátékos = new CsapatJátékos(újjátékos, újcsapat);
    résztvevőJátékosok.add(újcsapatjátékos);
    if (csapathalmaz.add(újcsapat)) //#TODO muszáj?
      résztvevőCsapatok.add(újcsapat); //
    siker &= újcsapat.addTag(újcsapatjátékos); 
    return siker; //#TODO
  }
  public void véglegesítCsapatok() { //erősorrendek beállítása
    for (Csapat csapat : résztvevőCsapatok) {
      csapat.setTáblasorrend(); //#TODO vizsgálni retvalt
    }
  }
  public void listJátékosok() {
    listJátékosok("", résztvevőJátékosok);
  }
  private void listJátékosok(String msg, ArrayList<CsapatJátékos> lista) {
    if (msg.equals(""))
      msg="alap";
    System.out.println("Listázási sorrend: "+msg);
    for (CsapatJátékos j : lista) 
      System.out.println(j);
  }
  public void listJátékosok(String msg, int by_what, int in_what_order) {
    ArrayList<CsapatJátékos> segédlista = new ArrayList<>();
    segédlista.addAll(résztvevőJátékosok);
    Collections.sort(segédlista,
      new Comparator<Játékos>() {
      @Override
      public int compare(Játékos j1, Játékos j2) {
        if (by_what==SORTEDBY_NAME && in_what_order==ORDER_ASCENDING)
          return j1.getNév().compareTo(j2.getNév());
        if (by_what==SORTEDBY_NAME && in_what_order==ORDER_DESCENDING)
          return j2.getNév().compareTo(j1.getNév());
        if (by_what==SORTEDBY_RATING && in_what_order==ORDER_DESCENDING)
          return Short.compare(j2.getFideÉrtékszám(), j1.getFideÉrtékszám());
        if (by_what==SORTEDBY_RATING && in_what_order==ORDER_ASCENDING)
          return Short.compare(j1.getFideÉrtékszám(), j2.getFideÉrtékszám());
        return 0; //#TODO
      }
    });
    listJátékosok(msg, segédlista); //a _segédlistát_ #iLoveJava
  }
  public void listCsapatok() { 
    listCsapatok("", résztvevőCsapatok);
  }
  private void listCsapatok(String msg, ArrayList<Csapat> lista) {
    if (msg.equals(""))
      msg="alap";
    System.out.println("Listázási sorrend: "+msg);
    for (Csapat cs : lista) 
      System.out.println(cs);
  }
  public void listCsapatok(String msg, int by_what, int in_what_order) {
    ArrayList<Csapat> segédlista = new ArrayList<>();
    segédlista.addAll(résztvevőCsapatok);
    Collections.sort(segédlista, (Csapat cs1, Csapat cs2) -> {
      if (by_what==SORTEDBY_RATING && in_what_order==ORDER_DESCENDING)
        return Double.compare(cs2.getFideÉrtékszámÁtlag(), cs1.getFideÉrtékszámÁtlag());
      if (by_what==SORTEDBY_NAME && in_what_order==ORDER_ASCENDING)
        return cs1.getNév().compareTo(cs2.getNév());
      if (by_what==SORTEDBY_NAME && in_what_order==ORDER_DESCENDING)
        return cs2.getNév().compareTo(cs1.getNév());
      return 0; //#TODO
    });
    listCsapatok(msg, segédlista); //a _segédlistát_ #iLoveJava
  }
}
//////////////////////////////
public class Vezérlő {

  private void createParticipants() {
    /*
    Csapat[] gépeltCsapatok = {
      new Csapat("Derecske"),
      new Csapat("Sakkbarátok"),
      new Csapat("Püspökladány"),
      new Csapat("Nagyrábé") 
    };
    Játékos[] gépeltJátékosok = {
      new Játékos("712272", "Bakk Zoltán", "1995", "20", "1975", "m"), //
      new Játékos("126712", "Képzelt Fiú", "2112", "20", "1975", "m"), //
      new Játékos("3674643", "Teszt Elek", "1912", "20", "1999", "m"),
      new Játékos("752365", "Gál Hanna", "2234", "20", "1998", "f"),
      new Játékos("716276", "Szilágyi Tibor", "2072", "20", "1972", "m"),
      new Játékos("712652", "Szente-Varga Fruzsina", "2172", "20", "1996", "f"),
      new Játékos("716273", "Polgár Judit", "2683", "10", "1976", "f"),
      new Játékos("723211", "Almási Zoltán", "2723", "10", "1976", "m"),
      new Játékos("703150", "Kis-Tojás Béla", "2294", "40", "2000", "m")
    };
    verseny.addPár(gépeltJátékosok[0], gépeltCsapatok[0]);
    verseny.addPár(gépeltJátékosok[1], gépeltCsapatok[1]);
    verseny.addPár(gépeltJátékosok[4], gépeltCsapatok[0]);
    verseny.addPár(gépeltJátékosok[2], gépeltCsapatok[1]);
    verseny.addPár(gépeltJátékosok[3], gépeltCsapatok[0]);
    verseny.addPár(gépeltJátékosok[5], gépeltCsapatok[1]);
    verseny.addPár(gépeltJátékosok[6], gépeltCsapatok[2]);
    verseny.addPár(gépeltJátékosok[7], gépeltCsapatok[2]);
    verseny.addPár(gépeltJátékosok[8], gépeltCsapatok[2]);
    */
    Csapat[] képzeltcsapatok = new Csapat[Kalapadat_Csapatok.TEAMNAMEs.length]; //most: legyen annyi ahány fiktív név van!
    int[] választhatóMégEnnyiszer = new int[képzeltcsapatok.length];
    for (int i = 0; i < képzeltcsapatok.length; i++) {
      képzeltcsapatok[i] = new Csapat(Csapat.KÉPZELT_CSAPAT); //#TODO nemoké, amikor azonos a nevük
      választhatóMégEnnyiszer[i] = Csapat.csapatlétszám;
    }
    Játékos[] képzeltjátékosok = new Játékos[képzeltcsapatok.length*Csapat.csapatlétszám];
    boolean[] választható = new boolean[képzeltjátékosok.length];
    for (int i = 0; i < képzeltjátékosok.length; i++) {
       képzeltjátékosok[i] =  new Játékos(Játékos.KÉPZELT_JÁTÉKOS); //#TODO nemoké, amikor azonos a FideID-jük (ritka eset)
       választható[i] = true;
    }
    //Sorsolunk először egy csapatot, majd hozzá egy játékost és hozzáadjuk a versenyhez.
    //addig csináljuk amíg tudjuk (normál esetben egyszerre fogynak el)
    boolean kész;
    int hányanmaradtak = képzeltjátékosok.length;  //#TODO bonyolultabb ellenőrzés kell
    do {
      int melyikcsapat;
      do {
        melyikcsapat = (int)(Math.random()*képzeltcsapatok.length);
      } while (választhatóMégEnnyiszer[melyikcsapat]==0);
      választhatóMégEnnyiszer[melyikcsapat]--;
      int melyikjátékos;
      do {
        melyikjátékos = (int)(Math.random()*képzeltjátékosok.length);
      } while (választható[melyikjátékos]==false);
      választható[melyikjátékos]=false;
      verseny.addPár(képzeltjátékosok[melyikjátékos], képzeltcsapatok[melyikcsapat]);      
      kész = --hányanmaradtak == 0;
    } while (!kész);
  }//createParticipants()
  private void showParticipants() {
    verseny.listCsapatok();
    verseny.listCsapatok("név szerint növekvő", Csapatverseny.SORTEDBY_NAME, Csapatverseny.ORDER_ASCENDING);
    verseny.listCsapatok("értékszámátlag szerint csökkenő", Csapatverseny.SORTEDBY_RATING, Csapatverseny.ORDER_DESCENDING);
    verseny.listJátékosok();
    verseny.listJátékosok("név szerint növekvő", Csapatverseny.SORTEDBY_NAME, Csapatverseny.ORDER_ASCENDING);
    verseny.listJátékosok("értékszám szerint csökkenő", Csapatverseny.SORTEDBY_RATING, Csapatverseny.ORDER_DESCENDING);    
    //
  }
  private Csapatverseny verseny = 
//          new Csapatverseny(); //#hibás #nemfutle #nincsneki #hihi #egyke
            Csapatverseny.getCsapatversenyInstance(); //#TODO paraméterben lehetne megadni a csapatlétszámot
  private void fuss() {
    createParticipants(); //#TODO createParticipants(verseny); 
    verseny.véglegesítCsapatok(); 
    showParticipants(); //#TODO showParticipants(verseny);
    
    //#TODO Kalapadat_Játékosok-ban megírni a nem-helyes változatot (külön: keresztnevek_férfi, keresztnevek_női)
  }
  public static void main(String[] args) {
    new Vezérlő().fuss();
  }//main()
}//class