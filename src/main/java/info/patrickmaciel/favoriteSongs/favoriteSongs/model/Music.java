package info.patrickmaciel.favoriteSongs.favoriteSongs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "musics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Music {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @ManyToOne
  private Artist artist;

  @Override
  public String toString() {
    return "Music {" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", artist=" + artist +
        " }";
  }
}
