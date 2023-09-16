package dev.morazzer.cookiesmod.features.farming.jacob;

import dev.morazzer.cookiesmod.features.farming.Crop;
import dev.morazzer.cookiesmod.utils.general.SkyblockDateTime;

public record Contest(SkyblockDateTime time, Crop[] crops) {
}
