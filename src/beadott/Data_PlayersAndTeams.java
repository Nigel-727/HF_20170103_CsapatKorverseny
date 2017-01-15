package beadott;

/**
 *
 * @author Nigel-727
 */
public interface Data_PlayersAndTeams {
  String CSV_FIDERATINGLIST = //#improvable Fájlból olvasással
    "704008,Mester Gyula,2381,10,1974,m\n" + //0
    "705101,donka pETER,2122,20,1963,m\n" + //1; szándékosan hibás a kis/nagybetűsség, mert ahol kell a #programjavítja
    "702838,Deak Sandor,2315,10,1958,m\n" + //
    "711861,Gal Hanna Krisztina,2175,40,1998,f\n" +
    "707503,Kanyadi Zoltan,2108,20,1968,m\n" +
    "726443,Ats Laszlo Gyorgy,1975,20,1953,m\n" +
    "735817,Rakaczki Csaba,2091,20,1976,m\n" +
    "720828,Horvath Karoly,2137,20,1981,m\n" +
    "724149,Kelemen Imre,2091,20,1975,m\n" +
    "717304,Dani Peter,2025,20,1967,m\n" +
    "711020,Vitai Tamas,1866,20,1987,m\n" +
    "722618,Pozsonyi Istvan,1995,20,1975,m\n" +
    "726486,Lengyel Laszlo,2053,20,1982,m\n" +
    "735841,Somorai Zsolt,2055,20,1985,m\n" +
    "711152,Fekete Istvan,1506,20,1947,m\n" +
    "759538,Debreceni Mate,1949,20,1993,m\n" +
    "747602,Kiss Gergely,2059,20,1994,m\n" +
    "711993,Nemeti Kornel,2065,20,1997,m\n" +
    "710105,Szilagyi Tibor,2058,20,1973,m\n" +
    "711187,Hercz Richard,1869,20,1996,m\n" +
    "0,Varga Istvan,,,1948,m\n" +
    "731463,Kovalcsik Zoltan,1815,20,1964,m\n" +
    "0,Salamon Gergely,,,2000,m\n" +
    "729884,Vida Jozsef,1510,20,1949,m\n" +
    "716430,Saller Imre,1814,20,1943,m\n" +
    "0,Csehely Bence,,,2002,m\n" +
    "761842,Gyorffy Peter Pal,2011,20,1996,m\n" +
    "733989,Vass Laszlo,1773,20,1952,m\n" +
    "705390,Karikas Marianna,1836,20,1995,f\n" +
    "755001,Toldy Emil,1770,20,1993,m\n" +
    "0,Balega Norbert,,,2004,m\n" +
    "0,Garami Gyula,,,1933,m\n" +
    "0,Gulacsi Mihaly,,,1940,m\n" +
    "0,Jobbagy Zsolt,,,1942,m\n" +
    "0,Sandor Krisztian,,,2006,m\n" +
    "1232002,Jakab Kincso,2019,40,2000,f\n" +
    "759309,Sos Barnabas,1930,40,2002,m\n" +
    "0,Kekesi Balazs,,,1999,m\n" +
    "714186,Mile Kalmanne,1635,20,1946,f\n" +
    "1204831,Rat Dan-Ovidiu,2264,20,1972,m\n" +
    "725013,Zoltan Lajos,1987,20,1984,m\n" +
    "728110,Magi Tibor,1864,20,1962,m\n" +
    "711357,Miko Valer,2097,20,1977,m\n" +
    "0,Kanyadi Attila,,,2005,m\n" +
    "1215477,Dombi-Marias Rudolf,1992,20,1976,m\n" +
    "711870,Daroczy Sandor,1822,20,1963,m\n" +
    "706574,Emodi Barnabas,2354,10,1973,m\n" +
    "721247,Leviczky Tibor,2332,10,1984,m\n" +
    "703621,Laszlo Janos,2216,20,1966,m\n" +
    "723746,Varro Miklos,1659,20,1969,m\n" +
    "772534,Katona Tamas,1116,40,1999,m\n" +
    "784320,Torok Laszlo,1471,40,1955,m\n" +
    "789909,Harsfalvi Gergely,1561,40,1992,m\n" +
    "703940,Czibulka Zoltan Dr.,2068,20,1951,m\n" +
    "742937,Czigler Dezso Zoltan,1737,20,1979,m\n" +
    "751138,Kassay Lilla,2001,40,1999,f\n" +
    "720470,Cselenyi Balazs,2214,20,1983,m\n" +
    "733466,Ag Norbert,2328,20,1987,m\n" +
    "729973,Bekesi Janos,1946,20,1966,m\n" +
    "721883,Balogh David,2168,20,1993,m\n" +
    "729671,Mizik Tamas,2019,20,1975,m\n" +
    "736767,Mizik Zsolt,2005,20,1977,m\n" +
    "703699,Fekete Albert,2233,20,1966,m\n" +
    "743828,Koncz Istvan,1851,20,1980,m\n" +  
    "752037,Kovacs Apollon,2120,20,1994,m\n" +
    "729434,Farkas Adam,2149,20,1986,m\n" +
    "701939,Borsavolgyi Tamas,2254,20,1964,m\n" +
    "728772,Baranyai Antal,1416,20,1935,m\n" +
    "710245,Szava Miklos,2095,20,1971,m\n" +
    "736538,Erdei Lajos sr.,1868,20,1955,m\n" +
    "742783,Bobak Sandor,1823,20,1946,m";
  String CSV_TEAMLIST= //#improvable Fájlból olvasással
    "Miskolci KisBocsok,752037\n" + //Kovacs Apollon
    "He1!,724149\n" +
    "Üvöltő halak,\n" +
    "Kisujjszállás,721883\n" + //Balogh David
    "DSBK Tanárok,\n" +
    "DSBK-Kairos,726443\n" + //Ats Laszlo
    "Dögevők,\n" +
    "Trilazo,\n" +
    "DSBK2,\n" +
    "Sakkhuligánok,\n" +
    "Debreceni Mesterek,704008\n" + //Mester Gyula
    "vegyes bazár,\n" +
    "Huszár-rock,\n" +
    "csipet csapat,\n" +
    "DSBK örök ifjak,\n" +
    "N.Pattriots,\n" +
    "Érmihályfalva,\n" +
    "fiatal csikók,\n" +
    "Fisher utódai,\n" +
    "Tarsolybácsi madarai,\n" +
    "Sakkcimborák,\n" +
    "Marica és a vadkanok,705390\n" + //Karikas Marianna
    "Kánya serege,707503\n" + //Kanyadi Zoltan
    "Balkáni amatőrök,\n" +
    "Luppy és a hiénák,729434\n" + //Farkas Adam
    "Menő manók,\n" +
    "Tufis,\n" +
    "Borsa és tsa,701939\n" + //Borsavolgyi Tamas
    "Nádudvar,728772\n" + //Baranyai Antal
    "Derecske,710105\n" + //Szilagyi Tibor
    "várjanak,\n" +
    "Barátok határok nélkül,\n" +
    "Merittunk,\n" +
    "Sakkamatőrök,\n" +
    "TUFIS,\n" +
    "Új erők,\n" +
    "Sportszelet,710245\n" + //Szava Miklos
    "Agria Park,\n" +
    "DSBK ifi,\n" +
    "Idősek Klubja,\n" +
    "Lókolbász,\n" +
    "Nyíracsád ifi,\n" +
    "Barcika,\n" +
    "Nagyrábé,736538\n" + //Erdei Lajos id.
    "Hóhányók,\n" +
    "Debreceni Betű,742783"; //Bobak Sandor
}//interface