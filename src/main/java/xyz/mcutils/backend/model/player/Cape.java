package xyz.mcutils.backend.model.player;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter @EqualsAndHashCode
public class Cape {

    /**
     * The URL of the cape
     */
    private final String url;

    /**
     * Gets the cape from a {@link JsonObject}.
     *
     * @param json the JSON object
     * @return the cape
     */
    public static Cape fromJson(JsonObject json) {
        if (json == null) {
            return null;
        }
        return new Cape(json.get("url").getAsString());
    }
}
