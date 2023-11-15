package dev.morazzer.cookiesmod.features.repository.constants;

import com.google.gson.annotations.SerializedName;
import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.util.Map;

/**
 * Representation of the commissions.json file.
 *
 * @param commissions The commissions.
 * @param namesToKey  The names to key.
 */
public record Commissions(@SerializedName("values") Map<String, Number> commissions,
                          @SerializedName("name_to_key") Map<String, String> namesToKey) {


    private static Commissions instance;

    public static Commissions getInstance() {
        return instance;
    }

    public static void load() {
        instance = JsonUtils.GSON.fromJson(RepositoryFileAccessor.getInstance().getFile("constants/commissions"),
            Commissions.class);
    }

}
