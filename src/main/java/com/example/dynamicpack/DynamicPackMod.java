package com.example.dynamicpack;

import net.fabricmc.api.ModInitializer;

public final class DynamicPackMod implements ModInitializer {
    @Override
    public void onInitialize() {
        DynamicPackConfig.load();
        System.out.println("[Dynamic Pack Mod] Loaded; hash endpoint is " + DynamicPackConfig.hashEndpoint());
    }
}
