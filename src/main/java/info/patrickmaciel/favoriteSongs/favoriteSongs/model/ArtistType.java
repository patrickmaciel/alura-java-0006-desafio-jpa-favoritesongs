package info.patrickmaciel.favoriteSongs.favoriteSongs.model;

public enum ArtistType {
  BAND("Banda"),
  SOLO("Solo"),
  DUO("Dupla");

  private String description;

  ArtistType(String description) {
    this.description = description;
  }

  public static ArtistType fromString(String description) {
    for (ArtistType type : ArtistType.values()) {
      if (type.description.equalsIgnoreCase(description)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Type not found");
  }
}
