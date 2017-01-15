package fejlesztés;

/**
 *
 * @author Nigel-727
 */
public interface Kalapadat_Játékosok {
  int   KÉPZELT_JÁTÉKOS = 0;
  int   HIBÁS_SZÁM = -1;
  char  HIBÁS_NEM = '\0';
  String[] LASTNAMEs={
    "Szabó", "Kovács", "Kis", "Nagy", "Bokros", "Gyurcsány", "Orbán", "Kiss", 
    "Kis-Nagy"
  };
  String[] FIRSTNAMES_MALE={ 
    "Attila", "Balázs", "Béla", "Sándor", "Ferenc", "Lajos", "Borisz"
  };
  String[] FIRSTNAMES_FEMALE={
    "Hedvig", "Éva", "Hanna", "Fruzsina", "Andrea", "Gyöngyvér"
  };
  enum EnSex { male, female }; //#redundáns
  String[]  SEXes={"m", "f"};  //#csúnya
  Byte[]    FIDEKs={(byte)40, (byte)20, (byte)10};  
  short     FIDERATING_MIN=1100,    FIDERATING_MAX=2870;
  short     SZÜLÉV_MIN=1920,        SZÜLÉV_MAX=2012;  //#TODO akt.év-5
  long      FIDEID_MIN=700000L,     FIDEID_MAX=3000000L; 
  //
  String  getFideID(); //vél: egész; EGYEDI;
  String  getNév(); //vél: sztring tömbökből;
  short   getFideÉrtékszám(); //vél: egész;
  byte    getFideSzorzó(); //vél: tömbből;
  short   getSzületésiÉv(); //vél: egész;
  char    getNem(); //vél: tömbből;
}