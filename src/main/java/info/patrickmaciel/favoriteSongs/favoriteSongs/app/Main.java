package info.patrickmaciel.favoriteSongs.favoriteSongs.app;

import java.util.Scanner;

public class Main {
  private final Scanner scanner = new Scanner(System.in);

  public void showMenu() {
    var option = -1;

    while (option != 0) {
      String menu = """
          1 - Register new Artist
          2 - Lista Artits
          3 - Register a song for an Artist
          4 - List songs for an Artist
          5 - List all songs
          0 - Exit
          """;

      System.out.println(menu);
      option = scanner.nextInt();
      scanner.nextLine();

      switch (option) {
        case 1:
          System.out.println("Register new Artist");
          break;
        case 2:
          System.out.println("List Artists");
          break;
        case 3:
          System.out.println("Register a song for an Artist");
          break;
        case 4:
          System.out.println("List songs for an Artist");
          break;
        case 5:
          System.out.println("List all songs");
          break;
        case 0:
          System.out.println("Exiting...");
          break;
        default:
          System.out.println("Invalid option");
          break;
      }
    }
  }

}
