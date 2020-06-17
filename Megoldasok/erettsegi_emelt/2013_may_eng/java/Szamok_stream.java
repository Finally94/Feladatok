import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Szamok_stream {
    
    public static void main(String[] args) throws IOException {
        var rand = new Random();
        var feladatok = new ArrayList<Feladat>();
        var file = Files.readAllLines(Paths.get("felszam.txt"), StandardCharsets.ISO_8859_1);
        
        IntStream.iterate(0, k -> k < file.size(), k -> k + 2)
                 .forEach(k -> {
                     var split = file.get(k + 1).split(" ");
                     feladatok.add(new Feladat(file.get(k), Integer.parseInt(split[0]), Integer.parseInt(split[1]), split[2]));
                 });
        
        System.out.println(feladatok.size() + " Feladat van a fájlban!");
        
        System.out.println("Matekfeladatok száma: " + feladatok.stream()
                  .filter(k -> k.temakor.equals("matematika"))
                  .count());
        
        System.out.println("1 pontot érő feladatok száma: " + feladatok.stream()
                  .filter(k -> k.temakor.equals("matematika"))
                  .filter(k -> k.pont == 1)
                  .count());
        
        System.out.println("2 pontot érő feladatok száma: " + feladatok.stream()
              .filter(k -> k.temakor.equals("matematika"))
              .filter(k -> k.pont == 2)
              .count());
        
        System.out.println("3 pontot érő feladatok száma: " + feladatok.stream()
              .filter(k -> k.temakor.equals("matematika"))
              .filter(k -> k.pont == 3)
              .count());
        
        System.out.println("A legkisebb válaszú feladat: " + 
                feladatok.stream().min(Comparator.comparingInt(k -> k.valasz)).get().valasz + ", a legnagyobb: " + 
                feladatok.stream().max(Comparator.comparingInt(k -> k.valasz)).get().valasz);
        
        System.out.println("Előforduló témakörök: " + feladatok.stream()
                                        .map(k -> k.temakor)
                                        .distinct()
                                        .collect(Collectors.joining(", ")));
        
        try(var input = new Scanner(System.in)){
            System.out.println("Írj be 1 témakört!");
            String readKor = input.nextLine();
            
            var categorizalt = feladatok.stream()
                                         .filter(k -> k.temakor.equals(readKor))
                                         .toArray(Feladat[]::new);
    
            var chosen = categorizalt[rand.nextInt(categorizalt.length)];
            System.out.println(chosen.kerdes);
            
            if(input.nextInt() == chosen.valasz) {
                System.out.println("Kapott pontszám: " + chosen.pont);
            }else{
                System.out.println("Rossz válasz, 0 pont...\nA helyes válasz: " + chosen.valasz);
            }
        }
        
        var generalt = rand.ints(0, feladatok.size())
                           .mapToObj(feladatok::get)
                           .distinct()
                           .limit(10)
                           .toArray(Feladat[]::new);
        
        var infoSorok = Arrays.stream(generalt)
                              .map(k -> k.pont + " " + k.kerdes)
                              .collect(Collectors.joining("\n"));
        
        var pontSum = Arrays.stream(generalt)
                            .mapToInt(k -> k.pont)
                            .sum();
        
        Files.writeString(Path.of("tesztfel.txt"), infoSorok + "\n" + pontSum);
    }
}