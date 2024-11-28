package info.patrickmaciel.favoriteSongs.favoriteSongs.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artists")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = true)
  private Boolean isGospelArtist;
  @Column(unique = true)
  private String name;
  @Enumerated(EnumType.STRING)
  private ArtistType type;
  private int releaseDate;
  private String genre;
  private int albumCount;
  private int songCount;

  @Getter
  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Music> musics = new ArrayList<>();

  public void setType(String type) {
    this.type = ArtistType.fromString(type);
  }

  public void setMusics(List<Music> musics) {
    musics.forEach(music -> music.setArtist(this));
    this.musics = musics;
  }

  @Override
  public String toString() {
    return "Artist {" +
        "id=" + id +
        ", isGospelArtist=" + isGospelArtist +
        ", name='" + name + '\'' +
        ", type=" + type +
        ", releaseDate=" + releaseDate +
        ", genre='" + genre + '\'' +
        ", albumCount=" + albumCount +
        ", songCount=" + songCount +
        " }";
  }
}