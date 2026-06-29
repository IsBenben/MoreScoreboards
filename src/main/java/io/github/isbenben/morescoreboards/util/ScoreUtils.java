package io.github.isbenben.morescoreboards.util;

import io.github.isbenben.morescoreboards.criterion.StatByTag;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ScoreUtils {
    public static final int UPDATE_INTERVAL = 100;

    public static Optional<StatType<?>> findStatType(@Nullable Identifier id) {
        System.out.println(id);
        return Registries.STAT_TYPE
                .getOptionalValue(id);
//                .or(() -> {
//                    if (Identifier.of(MoreScoreboards.MODID, "consumed").equals(id)) {
//                        return Optional.of(Register.CONSUMED);
//                    }
//                    return Optional.empty();
//                });
    }

    public static <T> void updatePlayerScores(ServerPlayerEntity player) {
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

    public static <T> void updateAllScores(ServerWorld world) {
    }
}
