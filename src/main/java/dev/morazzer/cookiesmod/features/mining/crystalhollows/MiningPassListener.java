package dev.morazzer.cookiesmod.features.mining.crystalhollows;

import dev.morazzer.cookiesmod.data.profile.ProfileData;
import dev.morazzer.cookiesmod.data.profile.ProfileStorage;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;

/**
 * Listener for the message that is sent when you buy the pass.
 */
@LoadModule("mining/crystalhollows/pass_listener")
public class MiningPassListener implements Module {
    @Override
    public void load() {
        ClientReceiveMessageEvents.GAME.register(this::onMessage);
    }

    private void onMessage(Text text, boolean b) {
        if (LocationUtils.getCurrentIsland() != LocationUtils.Islands.DWARVEN_MINES
            && LocationUtils.getCurrentIsland() != LocationUtils.Islands.CRYSTAL_HOLLOWS) {
            return;
        }

        if (text.getString().endsWith("Great! Now hop on into the Minecart and I'll get you on your way!")) {
            ProfileStorage.getCurrentProfile().map(ProfileData::getDwarvenMinesData)
                .ifPresent(dwarvenMinesData -> dwarvenMinesData
                    .setCrystalHollowsPassBoughtTime(System.currentTimeMillis()));
            DevUtils.log("pass-listener", "Set pass bought time to " + System.currentTimeMillis());
        }
    }

    @Override
    public String getIdentifierPath() {
        return "mining/crystalhollows/pass_listener";
    }
}
