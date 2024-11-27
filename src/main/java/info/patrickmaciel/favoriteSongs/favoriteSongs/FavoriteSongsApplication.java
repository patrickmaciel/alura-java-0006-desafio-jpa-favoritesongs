package info.patrickmaciel.favoriteSongs.favoriteSongs;

import info.patrickmaciel.favoriteSongs.favoriteSongs.app.Main;
import info.patrickmaciel.favoriteSongs.favoriteSongs.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FavoriteSongsApplication implements CommandLineRunner {
	@Autowired
	private ArtistRepository artistRepository;

	public static void main(String[] args) {
		SpringApplication.run(FavoriteSongsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(artistRepository);
		main.showMenu();
	}
}
