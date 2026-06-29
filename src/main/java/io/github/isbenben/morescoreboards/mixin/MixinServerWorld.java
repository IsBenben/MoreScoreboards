package io.github.isbenben.morescoreboards.mixin;

import io.github.isbenben.morescoreboards.util.ScoreUtils;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {
    @Unique
    private int morescoreboards$tickCount = 0;

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        ServerWorld serverWorld = (ServerWorld) (Object) this;
        if (this.morescoreboards$tickCount % ScoreUtils.UPDATE_INTERVAL == 0) {
            ScoreUtils.updateAllScores(serverWorld);
        }
        ++morescoreboards$tickCount;
    }
}
