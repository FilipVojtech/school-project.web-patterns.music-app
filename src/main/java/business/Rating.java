package business;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a rating given by a user to a song.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    /**
     *  unique identifier for the rating.
     */
    private int ratingId;

    /**
     *  ID of the user who gave the rating.
     */
    private int userId;

    /**
     *  song ID of the song being rated.
     */
    private int songId;

    /**
     *  rating value (1 to 5).
     */
    private int ratingValue;
}