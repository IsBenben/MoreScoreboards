package io.github.isbenben.morescoreboards;

import io.github.isbenben.morescoreboards.init.Register;
import net.fabricmc.api.ModInitializer;

public class MoreScoreboards implements ModInitializer {
    public static final String MODID = "morescoreboards";

    @Override
    public void onInitialize() {
        Register.initialize();
    }
}
