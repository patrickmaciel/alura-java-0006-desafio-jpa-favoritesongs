package info.patrickmaciel.favoriteSongs.favoriteSongs.app;

import info.patrickmaciel.favoriteSongs.favoriteSongs.model.Artist;
import info.patrickmaciel.favoriteSongs.favoriteSongs.model.ArtistType;
import info.patrickmaciel.favoriteSongs.favoriteSongs.model.Music;
import info.patrickmaciel.favoriteSongs.favoriteSongs.repository.ArtistRepository;
import info.patrickmaciel.favoriteSongs.favoriteSongs.service.ConsultaChatGpt;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

  private final Scanner scanner = new Scanner(System.in);
  private ConsultaChatGpt gpt;
  private ArtistRepository artistRepository;
  private List<Artist> artists = new ArrayList<>();
  private Optional<Artist> artistFound;

  public Main(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
    this.gpt = new ConsultaChatGpt();
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
          6 - Search for an artist using Chatgpt
          7 - Search for a song
          8 - List songs for an Artist using JPQL
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
          registerASongForAnArtist();
          break;
        case 4:
          listAllSongsForAnArtist();
          break;
        case 5:
          listAllSongs();
          break;
        case 6:
          searchForAnArtistUsingChatgpt();
          break;
        case 7:
          searchForASong();
          break;
        case 8:
          searchForAnExistingArtistUsingJPQL();
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

  private void searchForASong() {
    System.out.println("Type the song name you want to search for: ");
    String songName = scanner.nextLine();

    List<Music> musics = artistRepository.searchMusicsByName(songName);
    musics.forEach(music -> System.out.printf("Music %d - %s\n", music.getId(), music.getName()));
  }

  private void searchForAnExistingArtistUsingJPQL() {
    System.out.println("Type the artist name you want to search for: ");
    String artistName = scanner.nextLine();

    List<Music> musics = artistRepository.searchMusicsByArtistName(artistName);
    musics.forEach(music -> System.out.printf("Music %d - %s\n", music.getId(), music.getName()));
  }

  private void searchForAnArtistUsingChatgpt() {
    System.out.println("Type the artist name you want to search for: ");
    String artistName = scanner.nextLine();

    String gptResponse = gpt.sendMessage(
        String.format("Quem é o artista %s ? Obs.: é cristão/gospel.", artistName));

    System.out.println(gptResponse);
  }

  private void listAllSongs() {
    System.out.println("Listing all songs for every artist");
    artists = artistRepository.findAll();
    artists.forEach(artist -> {
      System.out.printf("Artist: %d - %s\n", artist.getId(), artist.getName());
      artist.getMusics().forEach(music -> System.out.printf("\tMusic: %d - %s\n", music.getId(), music.getName()));
      System.out.println("-------------------------------------------------");
    });
  }

  private void searchForAnExistingArtist() {
    System.out.println("Search for an existing artist on your database: ");
    String artistName = scanner.nextLine();

    artistFound = artistRepository.findByNameContainingIgnoreCase(artistName);
    if (artistFound.isEmpty()) {
      System.out.println("Artist not found");
      return;
    }
  }

  private void listAllSongsForAnArtist() {
    searchForAnExistingArtist();

    Artist artist = artistFound.get();

    System.out.printf("Listing all songs for artist %s\n", artist.getName());
    artist.getMusics().forEach(music -> System.out.printf("Music: %d - %s\n", music.getId(), music.getName()));
  }

  private void registerASongForAnArtist() {
    searchForAnExistingArtist();

    System.out.println("Type the music name for that artist: ");
    String musicName = scanner.nextLine();

    Artist artist = artistFound.get();
    Music music = Music.builder()
        .name(musicName.toUpperCase())
        .artist(artist)
        .build();

    artist.setMusics(List.of(music));
    artistRepository.save(artist);

    System.out.println("Music registered successfully");
  }

  private void listAllArtists() {
    System.out.println("Listing all artists");
    artists = artistRepository.findAll();
    artists.forEach(System.out::println);
  }

  private void registerNewArtist() {
    System.out.println("Enter the artist name:");
    String artistName = scanner.nextLine();

    Optional<Artist> artistExists = artistRepository.findByNameContainingIgnoreCase(artistName);
    if (artistExists.isPresent()) {
      System.out.println(".");
      return;
    }

    String gptResponse = gpt.sendMessage(
        String.format("Existe um artista cristão/gospel chamado %s? Responda sim ou não.", artistName));

    if (!gptResponse.equalsIgnoreCase("Sim.")) {
      System.out.println("Cê né crente não?");
      return;
    }

    gpt.sendMessage(
        String.format("O artista %s é um artista solo, dupla ou banda? Responda apenas solo, dupla ou banda.", artistName));

    gpt.sendMessage(
        String.format("Me retorne no seguinte formato as informações do Ano de formação, Gênero, Quantidade de Albuns e Quantidade músicas. Formato: ANO, GENERO, QTDE_ALBUNS, QTDE_MUSICAS.", artistName));

    List<String> chatResponseHistory = gpt.getConversationHistory();
    String[] extraDataParts = chatResponseHistory.get(5).split(",");
    artistFound = Optional.ofNullable(Artist.builder()
        .isGospelArtist(true)
        .name(artistName.toUpperCase())
        .type(ArtistType.fromString(chatResponseHistory.get(3).trim().replace(".", "")))
        .releaseDate(Integer.parseInt(extraDataParts[0].trim().replaceAll("\\D+", "")))
        .genre(extraDataParts[1].trim())
        .albumCount(Integer.parseInt(extraDataParts[2].trim().replaceAll("\\D+", "")))
        .songCount(Integer.parseInt(extraDataParts[3].trim().replaceAll("\\D+", "")))
        .build());

    if (artistFound.isEmpty()) {
      System.out.println("Error while creating artist");
      return;
    }

    Artist artist = artistFound.get();
    artistRepository.save(artist);
    System.out.println(artist);
  }
}
