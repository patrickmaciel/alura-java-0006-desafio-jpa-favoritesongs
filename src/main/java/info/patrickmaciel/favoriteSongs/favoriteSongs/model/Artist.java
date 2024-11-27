package info.patrickmaciel.favoriteSongs.favoriteSongs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="artists")
public class Artist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Enumerated(EnumType.STRING)
  private ArtistType type;

  public Artist() {}

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArtistType getType() {
    return type;
  }

  public void setType(String type) {
    this.type = ArtistType.fromString(type);
  }

  @Override
  public String toString() {
    return "Artist {" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", type=" + type +
        " }";
  }
}
