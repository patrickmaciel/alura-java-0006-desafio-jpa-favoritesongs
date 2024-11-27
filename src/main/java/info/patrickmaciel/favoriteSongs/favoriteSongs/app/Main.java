package info.patrickmaciel.favoriteSongs.favoriteSongs.app;

import info.patrickmaciel.favoriteSongs.favoriteSongs.model.Artist;
import info.patrickmaciel.favoriteSongs.favoriteSongs.model.ArtistType;
import info.patrickmaciel.favoriteSongs.favoriteSongs.repository.ArtistRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
  private final Scanner scanner = new Scanner(System.in);
  private ArtistRepository artistRepository;
  private List<Artist> artists = new ArrayList<>();
  private Optional<Artist> artistFound;

  public Main(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  public void showMenu() {
    var option = -1;

    while (option != 0) {
      String menu = """
          1 - Register new Artist
          2 - List all artists
          3 - Register a song for an Artist
          4 - List songs for an Artist
          5 - List all songs
          0 - Exit
          """;

      System.out.println();
      System.out.println(menu);
      option = scanner.nextInt();
      scanner.nextLine();

      System.out.println();

      switch (option) {
        case 1:
          registerNewArtist();
          break;
        case 2:
          listAllArtists();
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

  private void listAllArtists() {
    System.out.println("Listing all artists");
    artists.forEach(artist -> System.out.printf("Artist: %d - %s\n", artist.getId(), artist.getName()));
  }

  private void registerNewArtist() {
    System.out.println("Enter the artist name:");
    String artistName = scanner.nextLine();

    System.out.println("Enter the artist type:");
    String artistType = scanner.nextLine();

    Artist artist = new Artist();
    artist.setName(artistName);
    artist.setType(artistType);
    artists.add(artist);

    artistRepository.save(artist);
    System.out.println(artist);
  }

}
