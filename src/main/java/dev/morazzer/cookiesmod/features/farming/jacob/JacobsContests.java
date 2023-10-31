package dev.morazzer.cookiesmod.features.farming.jacob;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.features.farming.Crop;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockDateTime;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

/**
 * The manager for jacobs contests to provide information about what crop is in what contest.
 */
@LoadModule("farming/contests")
@Slf4j
public class JacobsContests implements Module {

    @Getter
    private static JacobsContests instance;

    @Getter
    private LinkedList<Contest> contests = new LinkedList<>();

    /**
     * Gets the next n number of contests, including currently active ones.
     *
     * @param amount The amount to get.
     * @return The contests.
     */
    public List<Contest> getNextNContestsActiveOrFuture(int amount) {
        return this.contests.stream().filter(contest -> contest.time().isCurrentDay() || contest.time().isInFuture())
                .limit(amount).toList();
    }

    @Override
    public void load() {
        instance = this;
        SkyblockDateTime nextFarmingContest = SkyblockDateTime.now()
                .getNext(SkyblockDateTime.SkyblockEvents.FARMING_CONTEST);
        Instant instant = nextFarmingContest.getInstant();
        Instant now = Instant.now();
        Instant delta = instant.minus(Duration.ofSeconds(now.getEpochSecond()));

        log.info("Running contest refresh in {}s", delta.getEpochSecond());

        ConcurrentUtils.schedule(
                () -> ConcurrentUtils.scheduleAtFixedRate(this::updateContests, 1, TimeUnit.HOURS),
                delta.getEpochSecond(),
                TimeUnit.SECONDS
        );
        this.updateContests();
    }

    @Override
    public String getIdentifierPath() {
        return "farming/contests";
    }

    /**
     * Updates the contests to include newly added ones.
     */
    private void updateContests() {
        log.info("Refreshing jacob contests");
        if (!contests.isEmpty()) {
            this.contests.removeIf(contest -> contest.time().isInPast());
            return;
        }

        byte[] responseBody = HttpUtils.getResponseBody(URI.create("https://api.elitebot.dev/Contests/at/now"));
        String response = new String(responseBody, StandardCharsets.UTF_8);
        JsonObject responseObject = JsonUtils.CLEAN_GSON.fromJson(response, JsonObject.class);

        if (!responseObject.has("contests")) {
            return;
        }

        JsonObject contests = responseObject.getAsJsonObject("contests");
        for (String key : contests.keySet()) {
            Crop[] crops = StreamSupport.stream(contests.getAsJsonArray(key).spliterator(), false)
                    .filter(JsonElement::isJsonPrimitive)
                    .map(JsonElement::getAsJsonPrimitive)
                    .filter(JsonPrimitive::isString)
                    .map(JsonPrimitive::getAsString)
                    .map(Crop::byName)
                    .toArray(Crop[]::new);
            long time = Long.parseLong(key);
            if (time < System.currentTimeMillis() / 1000) {
                continue;
            }

            this.contests.add(new Contest(SkyblockDateTime.ofEpochSecond(time), crops));
        }

    }

}
