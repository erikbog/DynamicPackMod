package com.example.dynamicpack.mixin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.example.dynamicpack.HashEndpoint;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.server.network.config.ServerResourcePackConfigurationTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ServerResourcePackConfigurationTask.class)
abstract class ServerResourcePackConfigurationTaskMixin {
    @ModifyArgs(
            method = "start",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/common/ClientboundResourcePackPushPacket;<init>(Ljava/util/UUID;Ljava/lang/String;Ljava/lang/String;ZLjava/util/Optional;)V"))
    private void dynamicPack$replaceHash(Args args) {
        UUID id = args.get(0);
        String url = args.get(1);
        System.out.println("[Dynamic Pack Mod] Fetching SHA-1 for " + url);
        try {
            String hash = HashEndpoint.fetchHash().get(5, TimeUnit.SECONDS);
            args.set(2, hash);
            System.out.println("[Dynamic Pack Mod] Using resource-pack ID " + id);
            System.out.println("[Dynamic Pack Mod] Using SHA-1 " + hash + " for " + url);
        } catch (Exception exception) {
            System.err.println("[Dynamic Pack Mod] Hash request failed for " + url + ": " + exception);
            args.set(2, "");
        }
    }
}
