package alap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author Nigel-727
 */
public class MainHF //TODO rename to Vezérlő #vagyvalamiilyesmi
{
  private static String my_table3DtoString(int[][][] pairingtable) { //#teszt
    String ezkellneked = "";
    for (int i = 0; i < pairingtable.length; i++) {
      for (int j = 0; j < pairingtable[i].length; j++) {
        ezkellneked+="{";
        for (int k = 0; k < pairingtable[i][j].length; k++) {
          ezkellneked+=pairingtable[i][j][k]+" ";
        }
        ezkellneked+="} ";
      }
      ezkellneked+="\n";
    }//külső ciklus
    return ezkellneked+"";
  }//my_table3DtoString(); 
  //#TODO sajnos Jelenleg az aktuális versenyen nem résztvevők is szennyezik a névteret 
  //és többek között egyedi ID-t kapnak az osztály példányszámlálójától:
  private static ArrayList<Team>    allTeams = new ArrayList<>();  //mindegyik amit a "fájlból" beolvastunk
  private static ArrayList<Player>  allPlayers = new ArrayList<>(); //mindenki akit a "fájlból" beolvastunk
  private static HashSet<Player>    kiválaszthatatlanok = new HashSet<>(); //aki vmelyik fix csapat-játékos páros tagja, másik csapatba nem kerülhet
  private static int                résztvevőCsapatokSzáma; //input a felhasználótól; statikus metódus is felhasználja;  #TODO Vagy inkább lokáis legyen?
  private static ArrayList<Team>    résztvevőCsapatok = new ArrayList<>(); //csak a résztvevők
  private static ArrayList<Player>  résztvevőJátékosok = new ArrayList<>(); //csak a résztvevők
  private static int[][][]          párosításiTáblánk; //#TODO csúnya
  private static boolean            vanPihenőCsapat;
  private static int                fordulókSzáma; //#TODO csúnya; számított érték (résztvevő csapatok száma + táblázat alapján); 
  private static int                fordulóSzámláló=0; //az aktuális forduló száma; #TODO ez sem túl szép;
  private static void beolvasJátékosokCsapatok() {
    try {
      String[] sorok, tagok; //több helyen használjuk őket      
      sorok = Data_PlayersAndTeams.CSV_FIDERATINGLIST.split("\n"); //"\\."
      for (String sor : sorok) {
        tagok = sor.split(",");
        allPlayers.add( new Player(tagok[0], tagok[1], tagok[2], tagok[3], tagok[4], tagok[5]) );
      }
/*      
      System.out.println("Íme, a játékosok:\n"+allPlayers);
      System.out.println("Számuk: "+allPlayers.size());
*/      
/*      
      for (Player player : allPlayers) 
        System.out.println("."+player+"."); //#teszt
*/      
      sorok = Data_PlayersAndTeams.CSV_TEAMLIST.split("\n"); 
      for (String sor : sorok) {
        tagok = sor.split(",");
        { //#teszt blokk; így nem szennyezzük a névteret segédváltozókkal
          boolean vanÉshelyesAmásodikparaméter=false;
          if (1<tagok.length) { //ie. van-e fix játékos
            //MEGKERESNI a játékosok közt by FIDE_azonosító; ha megvan, a kétparaméteres konstruktort (playerID-vel) hívni
            Player fixjátékos=null;
            int i=0;
            while (i<allPlayers.size() && 
                    !(allPlayers.get(i).hasFide() 
                      && (vanÉshelyesAmásodikparaméter=
                        allPlayers.get(i).getFideNumber()==Long.parseLong(tagok[1]))
                    )) //#dobhatkivételt #elkapjuk
              i++;
            if (vanÉshelyesAmásodikparaméter) {
              fixjátékos=allPlayers.get(i)/*.getPlayer()*/; //?: miért írtam a getPlayer() -t?
              kiválaszthatatlanok.add(fixjátékos); //őt nem lehet "csak úgy" felvenni akármelyik csapatba
              allTeams.add( new Team(tagok[0], fixjátékos) );               
              //#TODO: A játékoshoz is hozzá kéne adni a csapatát!!!!!!!!!!!!
              //...itt....
            }
          }
          if (!vanÉshelyesAmásodikparaméter)
            allTeams.add( new Team(tagok[0]) ); //különben az egyparamétereset hívjuk meg
        }
      }//for
/*      
      System.out.println("Íme, a csapatok:\n"+allTeams);
      System.out.println("Számuk: "+allTeams.size());
*/      
/*      
      for (Team team : allTeams) 
        System.out.println("."+team+"."); //#teszt
*/
      System.out.println();
    }//try
    catch (IllegalArgumentException e) {
      System.out.println("Most megvagy, te kis hibás argumentum!!! ("+e.getMessage()+")");
    }
    catch (Exception E) {
      System.out.println("E kivétel erősíti a szabályt.");
    }
  }//beolvasJátékosokCsapatok()
  private static void kiválasztRésztvevők() { //résztvevő csapatok kiválasztása, játékosokkal feltöltése
    //alábbit nem biztos h ezen az adathalmazon kéne
    Collections.shuffle(allTeams); //EMIATT lesz "random" a résztvevő csapatok kiválasztása illetve...
    Collections.shuffle(allPlayers); //....a játékosok kiválasztása.
    //most, az egyszerűség kedvéért, tekintsünk el specifikációban megadott 150 pontos szabálytól
    //most egyedül a fix csapat-alapítójátékos párokra (amikor vannak) figyelünk; #TODO Később midnenre figyelni!
    //?: class-okból álló adatszerkezetek helyett egyszerűbb volna-e az ID-ket (mindnek van) felhasználva műveleteket végezni?
    HashSet<Player> válaszhatóJátékosok = new HashSet<>(allPlayers); //!: arraylist to hashset
    HashSet<Team> választhatóCsapatok = new HashSet<>(allTeams);
    //két dolgot akarok: minden csapatba felvenni a játékosid-ket (+ esetleg játékosokba felvenni a csapatID-t)
    //először is kiválasztok résztvevőcsapatokszáma csapatot és közben kiveszem a választható csapatok közül
    
    for (int i = 0; i < résztvevőCsapatokSzáma; i++) {
      Team kiválasztottCsapat;
      résztvevőCsapatok.add(  kiválasztottCsapat=(Team)választhatóCsapatok.toArray()[i]  );
      if (!választhatóCsapatok.remove(kiválasztottCsapat)) 
        throw new IllegalArgumentException("nem sikerül a csapatot eltávolítani"); //#TODO felhasználóbarátabban #végredobtamvalamit
      //közben a fix játékossal (ha van) ezt teszem:
      //a játékos objektumához rendelem a csapatát, kb: játékosobjektum.setcsapat(csapat));
      //kiveszem a választhatójátékosok közül 
      for (int j = 0; j < kiválasztottCsapat.getNumberOfPlayers(); j++) {
        Player kiválasztottJátékos;
        kiválasztottJátékos=kiválasztottCsapat.getPlayer(j);
        kiválasztottJátékos.setTeam(kiválasztottCsapat); //#TODO Szabad-e ilyet?
//System.out.println("KIVÁLASZTÁS történt: "+kiválasztottCsapat.getName()+"->"+kiválasztottJátékos.getName()); //#teszt       
        résztvevőJátékosok.add(kiválasztottJátékos);
        if (!válaszhatóJátékosok.remove(kiválasztottJátékos))
          throw new IllegalArgumentException("nem sikerül a játékost (fixpárban lévőt: " + kiválasztottJátékos.getName()
                  + ") eltávolítani"); //#TODO felhasználóbarátabban
      }//ciklus (kiválasztott csapat már meglévő játékosain)
      //Amíg fér bele játékos, választunk: //#TODO Lehetne kézi bevitellel is. //tfh. van elég játékos      
      while (kiválasztottCsapat.getNumberOfPlayers() < Team.PLAYERS_ON_A_TEAM) {
        Player kiválasztottJátékos;
        Object[] választhatóJátékosokTömbje = válaszhatóJátékosok.toArray();
        int j = 0;
        //A jelenleg választható játékosok közül megkeressük az első olyat aki nincs a kiválaszthatatlanok között:
        while (kiválaszthatatlanok.contains(kiválasztottJátékos = (Player)választhatóJátékosokTömbje[j]))
          j++; //#TODO Kivételt dobni ha a ciklusváltozó végig tud menni a tömbön!
        kiválasztottJátékos.setTeam(kiválasztottCsapat);
//System.out.println("KIVÁLASZTÁS történt: "+kiválasztottCsapat.getName()+"->"+kiválasztottJátékos.getName()); //#teszt
        résztvevőJátékosok.add(kiválasztottJátékos);
        kiválasztottCsapat.addPlayer(kiválasztottJátékos);
        if (!válaszhatóJátékosok.remove(kiválasztottJátékos))
          throw new IllegalArgumentException("nem sikerül a játékost (" + kiválasztottJátékos.getName()
                  + ") eltávolítani"); //#TODO felhasználóbarátabban
      }//ciklus (amíg a csapat fel nincs töltve)
      if (!kiválasztottCsapat.sortPlayersByRating())
        throw new IllegalArgumentException("nem sikerült a csapatot rendezni (értékszám szerint)");
    }//ciklus (amíg a résztvevőcsapatok nincsenek feltöltve)
    System.out.println("A csapatösszeállítások:");
    for (int i = 0; i < résztvevőCsapatok.size(); i++) 
      System.out.print(résztvevőCsapatok.get(i)+"\n"); //#teszt
    System.out.println();
  }//kiválasztRésztvevők()
  private static void doPárosítás() { //valójában: doAndShow...
    System.out.println("Az aktuális ("+fordulóSzámláló+".) forduló párosításai:");
    //Ha nem játszik mindenki az fordulókban (itt: mert páratlan számú csapat van), 
    //akkor máshogyan kell használni a táblázatot. Mivel a táblázat következetes, a fenti szerint indexelhető.
    //#TODO az alábbi nem szép, mivel hibásan működne másféle rendezésű párosításitáblák esetén:
    //#NEMSZÉP eleje:
    int kezdőindex = vanPihenőCsapat ? 1 : 0;
    int[][] párokAFordulóban 
            = new int [párosításiTáblánk[fordulóSzámláló-1].length - kezdőindex][2]; 
//    párokAFordulóban = párosításiTáblánk[fordulóSzámláló-1]; //az alábbi ciklusok helyett ez elég volna, ha sose volna pihenő csapat
    for (int i = 0; i < párokAFordulóban.length; i++)  //ezt állítottuk be az előbb; ?: létezik iesmire külön fv.?
      for (int j = 0; j < 2; j++)  //a fenti beállítás miatt párokAFordulóban[i].length==2
        párokAFordulóban[i][j] = párosításiTáblánk[fordulóSzámláló-1][kezdőindex+i][j];
    String pihenőcsapatneve = "";
    if (vanPihenőCsapat) {
      int[] kihagyottpárosítás = párosításiTáblánk[fordulóSzámláló-1][0];
      pihenőcsapatneve = résztvevőCsapatok.get(
              Math.min(kihagyottpárosítás[0], kihagyottpárosítás[1]) -1
      ).getName();
    }
    //#NEMSZÉP vége.
    //Megjelenítjük a forduló párosítását (csapatnevekel + játékosnevekkel):    
    for (int[] pár : párokAFordulóban) {
      String eztírdki = "hazai: "+"\""+résztvevőCsapatok.get(pár[0]-1).getName()+"\""
                      + " vs. vendég: "+"\""+résztvevőCsapatok.get(pár[1]-1).getName()+"\"\n";
      String filler = "..........................................."; //#eztmeghogy
      for (int i = 0; i < Team.PLAYERS_ON_A_TEAM; i++) { //#TODO Lehetne összetettebb az ellenőrzés
        Player  hazai  = résztvevőCsapatok.get(pár[0]-1).getPlayer(i), //végiglépkedünk a csapat játékosain
                vendég = résztvevőCsapatok.get(pár[1]-1).getPlayer(i);
        eztírdki  += "tábla#"+(i+1)+": ";
        String str  = hazai.getName()+" "+hazai.getFideRating()+" ("
                +((i+1)%2!=0 ? "világos" : "sötét")+")";
        eztírdki  += str + filler.substring(str.length())
                  + vendég.getName()+" "+vendég.getFideRating()+(" ("
                  + ((i+1)%2!=0 ? "sötét" : "világos")+")")
                  + "\n";
      }//for i
      System.out.println(eztírdki);
    }//"foreach": pár
    System.out.println( !pihenőcsapatneve.equals("")
            ?"A fordulóban pihenő csapat: \""+pihenőcsapatneve+"\"\n":"");
    //EZMÉGNINCSKÉSZ: #ittjárok; #TODO Tároljuk is el az objektumokban, stb, 
    //hogy a párosítás megtörtént!! #különbensemmitnemérazegész
  }
  public static void  main(String[] args) {
    System.out.println("Egyszerű párosítóprogram\n"
            + "\tsakkversenyekhez (jelleg: rapidtempójú csapatverseny round-robin párosítással)\n");
    beolvasJátékosokCsapatok();
    {  //#TODO Összetettebb feltétel kell, mert az imént beolvasott adatoktól (pl. beolvasott játékosok száma) is függ!
      final int LEGALÁBB = Data_PairingTables.MIN_PARTICIPANTS, 
              LEGFELJEBB = Data_PairingTables.MAX_PARTICIPANTS; //a párosítási táblázatok méretei
      System.out.println("Kérem, adja meg a versenyen résztvevő csapatok ("
              + LEGALÁBB + " és " + LEGFELJEBB + " közötti érték) "
              + "számát!");      
      while ( !(LEGALÁBB<=(résztvevőCsapatokSzáma = extra.Console.readInt(": ")) 
              && résztvevőCsapatokSzáma<=LEGFELJEBB) ) 
        System.out.println("\tCsak "+LEGALÁBB+" és "+LEGFELJEBB+" közötti érték lehetséges!");
    }       
    System.out.println("A csapatösszeállítások elkészítése (összes csapatnév és összes játékosnév alapján)...");
    kiválasztRésztvevők(); //#improvable Lehessen user által bevinni
    //A táblázatból kiválasztani (a határok alapján) hogy az adott résztvevőszám esetén hány db forduló szükséges!
    //(természetesen, ezúttal nem volna szükség kiválasztásra, mivel egyszerű képlet is van rá)
    int i=0;
    while (!( Data_PairingTables.TABLE_BOUNDS[i][0]<=résztvevőCsapatokSzáma
              && //vagyis, amikor a besorolandó résztvevőszám a táblázatban két fix határ közé esik
              résztvevőCsapatokSzáma<=Data_PairingTables.TABLE_BOUNDS[i][1] )
    )
      i++;
    párosításiTáblánk = Data_PairingTables.PAIRINGTABLES[i];
    fordulókSzáma = párosításiTáblánk.length; //#ezértcsináltuk
    System.out.println("A (kör)versenyen lejátszandó fordulók száma: "+fordulókSzáma);
    vanPihenőCsapat //az egyes fordulókban párosítani kell-e minden csapatot
            = résztvevőCsapatokSzáma%2 != 0; //#TODO Ez a vizsgálat ezúttal helyes eredményhez vezet, de nem következetes!!!
    boolean ezennyivolt=false;
    do {
      fordulóSzámláló++;
      String választék = "+-/123456v";
      System.out.println("Válasszon billentyűleütéssel!\n"
              + "\t/: következő ("+fordulóSzámláló+"/"+fordulókSzáma+".) forduló " //#TODO csak ha már megvannak az eredmények
                      + "párosítása\n" //#TODO Ha van következő forduló              
              + "\t+: [eredmények bevitele]\n" //#TODO csak ha már megvan a párosítás
              + "\t-: [eredmények javítása]\n" //#TODO csak ha már megvannak az eredmények
              + "\t1: [listáz: csapatverseny állása]\n"
              + "\t2: [listáz: játékosok sorrendje táblánként kategorizálva]\n"
              + "\t3: [listáz: egy korábbi forduló összes eredménye]\n"
              + "\t4: [listáz: játékosok pontszámai és értékszámváltozások (csökkenő játékosértékszám szerint)]\n"
              + "\t5: [...]\n"
              + "\t6: [...]\n"
              + "\tv: vége, kilépés a programból");
      char választás;
      while ( választék.indexOf(választás=extra.Console.readLine("? ").toLowerCase().charAt(0)) < 0 ) {
        System.out.print("\tCsak a felsoroltak közül választhat! (");
        for (int j = 0; j < választék.length()-1; j++) 
          System.out.print("'"+választék.charAt(j)+"', ");
        System.out.println("'"+választék.charAt(választék.length()-1)+"')");                  
      }
      switch (választás) {
        case '/':
          System.out.println("Forduló párosítása indul...");
          doPárosítás(); //#TODO Ez sem szép így
          break;    
        case 'v':
          System.out.println("Vége, akkor hát.");
          ezennyivolt=true;
          break;
        default:
          System.out.println("Ez a funkció még nincs kész.");
          ezennyivolt=true;        
      }
      ezennyivolt |= (fordulóSzámláló==fordulókSzáma); //#TODO Ennél bonyolultabb feltétel kell
    } while(!ezennyivolt);
//    System.out.println("Vége a programnak."); //#egyelőre
  }//main()
}//class MainHF

/*  //#teszt; az alábbiak main()-ben lefutnak
    System.out.println(CSV_FIDERATINGLIST);
    System.out.println(""+my_table3DtoString(PAIRINGTABLES[4]));
    System.out.println(""+MIN_PLAYERS+" to "+MAX_PLAYERS);
*/  //