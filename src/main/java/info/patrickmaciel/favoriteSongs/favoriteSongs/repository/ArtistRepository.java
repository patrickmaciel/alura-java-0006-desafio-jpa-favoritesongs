package info.patrickmaciel.favoriteSongs.favoriteSongs.repository;

import info.patrickmaciel.favoriteSongs.favoriteSongs.model.Artist;
import info.patrickmaciel.favoriteSongs.favoriteSongs.model.Music;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

  Optional<Artist> findByNameContainingIgnoreCase(String artistName);

  @Query("SELECT m FROM Artist a JOIN a.musics m WHERE a.name ILIKE %:artistName%")
  List<Music> searchMusicsByArtistName(String artistName);

  @Query("SELECT m FROM Artist a JOIN a.musics m WHERE m.name ILIKE %:songName%")
  List<Music> searchMusicsByName(String songName);
}
