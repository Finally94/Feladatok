import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class LezerLoveszet_stream {
    private static int szamlalo = 1;
    
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("lovesek.txt"));
        var elsoSor = lines.get(0).split(";");
        
        var kozeppontX = Float.parseFloat(elsoSor[0].replace(',', '.'));
        var kozeppontY = Float.parseFloat(elsoSor[1].replace(',', '.'));
        var jatekosok = lines.stream()
                             .skip(1)
                             .map(line -> new JatekosLovese(line, szamlalo++))
                             .toArray(JatekosLovese[]::new);
        
        System.out.println("5. Feladat");
        System.out.println("Lövések száma: " + jatekosok.length);
        System.out.println("7. Feladat");
        
        Arrays.stream(jatekosok)
              .min(Comparator.comparingDouble(jatekos -> jatekos.tavolsag(kozeppontX, kozeppontY)))
              .ifPresent(k -> System.out.println("Legpontosabb lövés: " + k.lovesSorszam + ", neve: " + k.jatekosNeve 
                                                                          + ", koordináták: x:" + k.lovesX + " y:" + k.lovesY
                                                                          + ", távolság: " + k.tavolsag(kozeppontX, kozeppontY)));
        var nullapontosok = Arrays.stream(jatekosok)
                                  .mapToDouble(k -> k.pontszam(kozeppontX, kozeppontY))
                                  .filter(k -> k == 0D)
                                  .count();
        
        System.out.println("9. Feladat");
        System.out.println("Nulla pontosok száma: " + nullapontosok);
        System.out.println("10. Feladat");
        System.out.println("Játékosok száma: " + Arrays.stream(jatekosok).map(k -> k.jatekosNeve).distinct().count());
        
        System.out.println("11. Feladat");
        
        Arrays.stream(jatekosok)
              .collect(Collectors.groupingBy(k -> k.jatekosNeve, Collectors.counting()))
              .forEach((nev, szam) -> System.out.println("Név: " + nev + ", lovesek szama: " + szam));
        
        System.out.println("12. Feladat");
        Arrays.stream(jatekosok)
              .collect(Collectors.groupingBy(k -> k.jatekosNeve, Collectors.averagingDouble(k -> k.pontszam(kozeppontX, kozeppontY))))
              .forEach((nev, atlag) -> System.out.println("Név: " + nev + ", atlag: " + atlag));
        
        System.out.println("13. Feladat");
        Arrays.stream(jatekosok)
              .max(Comparator.comparingDouble(k -> k.pontszam(kozeppontX, kozeppontY)))
              .ifPresent(jatekos -> System.out.println("A nyertes: " + jatekos.jatekosNeve));
    }
    
    public static class JatekosLovese{
        public final String jatekosNeve;
        public final float lovesX, lovesY;
        public final int lovesSorszam;

        public JatekosLovese(String sor, int sorszam) {
            var split = sor.split(";");
            
            jatekosNeve = split[0];
            lovesX = Float.parseFloat(split[1].replace(',', '.'));
            lovesY = Float.parseFloat(split[2].replace(',', '.'));
            lovesSorszam = sorszam;
        }
        
        public double tavolsag(float kozeppontX, float kozeppontY) {
            var dx = kozeppontX - lovesX;
            var dy = kozeppontY - lovesY;
            return Math.sqrt(dx * dx + dy * dy);
        }
        
        public double pontszam(float kozeppontX, float kozeppontY) {
            var local = 10D - tavolsag(kozeppontX, kozeppontY);
            return local < 0 ? 0D : Math.round(local * 100D) / 100D;
        }
    }
}