package info.patrickmaciel.favoriteSongs.favoriteSongs.repository;

import info.patrickmaciel.favoriteSongs.favoriteSongs.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
