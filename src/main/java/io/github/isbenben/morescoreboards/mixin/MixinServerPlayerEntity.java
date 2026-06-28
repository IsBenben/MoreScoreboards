package io.github.isbenben.morescoreboards.mixin;

import io.github.isbenben.morescoreboards.criterion.StatByTag;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity {
    @Unique
    private int tickCount = 0;

    @Unique
    private static final int INTERVAL_TICKS = 100;

    @Unique
    private static <T> void updateAllScores(ServerPlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            ScoreboardCriterion criterion = objective.getCriterion();
            if (criterion instanceof StatByTag<?> statByTag) {
                //noinspection unchecked
                StatType<T> statType = (StatType<T>) statByTag.getStatType();
                int sumScore = 0;
                for (Stat<T> stat : statType) {
                    RegistryEntry<T> entry = statType.getRegistry().getEntry(stat.getValue());
                    if (statByTag.contains(entry)) {
                        sumScore += player.getStatHandler().getStat(stat);
                    }
                }
                ScoreAccess score = scoreboard.getOrCreateScore(player, objective, true);
                score.setScore(sumScore);
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (this.tickCount % INTERVAL_TICKS == 0) {
            updateAllScores(player);
        }
        ++tickCount;
    }

    @Inject(at = @At("TAIL"), method = "increaseStat")
    private <T> void increaseStat(Stat<T> stat, int amount, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        Scoreboard scoreboard = player.getScoreboard();
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            ScoreboardCriterion criterion = objective.getCriterion();
            StatType<T> statType = stat.getType();
            if (criterion instanceof StatByTag<?> statByTag && statByTag.getStatType() == statType) {
                RegistryEntry<T> entry = statType.getRegistry().getEntry(stat.getValue());
                if (statByTag.contains(entry)) {
                    ScoreAccess score = scoreboard.getOrCreateScore(player, objective, true);
                    score.incrementScore(amount);
                }
            }
        }
    }
}
