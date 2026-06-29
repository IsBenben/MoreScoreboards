package io.github.isbenben.morescoreboards.mixin;

import io.github.isbenben.morescoreboards.criterion.StatByTag;
import io.github.isbenben.morescoreboards.criterion.StatReversed;
import io.github.isbenben.morescoreboards.util.RegistryEntryScoreHolder;
import io.github.isbenben.morescoreboards.util.ScoreUtils;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.isbenben.morescoreboards.MoreScoreboards.MODID;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity {
    @Unique
    private int morescoreboards$tickCount = 0;

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (this.morescoreboards$tickCount % ScoreUtils.UPDATE_INTERVAL == 0) {
            ScoreUtils.updatePlayerScores(player);
        }
        ++morescoreboards$tickCount;
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
            } else if (criterion instanceof StatReversed<?> statReversed && statReversed.getStatType() == statType) {
                Registry<T> registry = statType.getRegistry();
                RegistryEntry<T> entry = registry.getEntry(stat.getValue());
                if (statReversed.contains(entry)) {
                    RegistryEntryScoreHolder scoreHolder = RegistryEntryScoreHolder.fromRegistryEntry(registry, entry);
                    ScoreAccess score = scoreboard.getOrCreateScore(scoreHolder, objective, true);
                    Team team = scoreboard.addTeam(MODID + scoreHolder.translationKey());
                    team.setPrefix(scoreHolder.getDisplayName());
                    scoreboard.addScoreHolderToTeam(scoreHolder.getNameForScoreboard(), team);
                    score.incrementScore(amount);
                }
            }
        }
    }
}
