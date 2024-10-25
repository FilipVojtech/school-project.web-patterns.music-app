package business;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Song {
    private String title;
    private int artist_id;
    private int albumId;
    private int rating;

}
