package dev.morazzer.cookiesmod.features.farming.jacob;

import dev.morazzer.cookiesmod.features.farming.Crop;
import dev.morazzer.cookiesmod.utils.general.SkyblockDateTime;

/**
 * A jacobs contest instance to represent the time and the crops.
 *
 * @param time  The time the contest happens.
 * @param crops The crops in the contest.
 */
public record Contest(
        SkyblockDateTime time,
        Crop[] crops
) {}
