package dev.morazzer.cookiesmod.data.profile.mining;

import lombok.Getter;
import lombok.Setter;

/**
 * Various data related to the dwarven mines.
 */
@Getter
@Setter
public class DwarvenMinesData {

    /**
     * The last recorded time the user has finished the puzzler quest.
     */
    private long lastPuzzlerTime = -1;
    /**
     * The last recorded time the user has finished the fetchur quest.
     */
    private long lastFetchurTime = -1;
    /**
     * The last recorded time the user has bought the crystal hollows pass.
     */
    private long crystalHollowsPassBoughtTime = -1;

}
