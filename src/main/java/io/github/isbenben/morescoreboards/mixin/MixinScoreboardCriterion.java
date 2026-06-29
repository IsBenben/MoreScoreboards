package io.github.isbenben.morescoreboards.mixin;

import io.github.isbenben.morescoreboards.criterion.StatByTag;
import io.github.isbenben.morescoreboards.criterion.StatReversed;
import io.github.isbenben.morescoreboards.util.ScoreUtils;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static io.github.isbenben.morescoreboards.criterion.StatByTag.BY_TAG_SYMBOL;
import static io.github.isbenben.morescoreboards.criterion.StatReversed.REVERSED_SYMBOL;

@Mixin(ScoreboardCriterion.class)
public abstract class MixinScoreboardCriterion {
    @Inject(at = @At("HEAD"),
            method = "getOrCreateStatCriterion(Ljava/lang/String;)Ljava/util/Optional;",
            cancellable = true)
    private static void getOrCreateStatCriterion(String name,
                                                 CallbackInfoReturnable<Optional<ScoreboardCriterion>> cir) {
        int i = name.indexOf(':');
        if (i < 0) {
            return;
        }
        String statType = name.substring(0, i);
        String statName = name.substring(i + 1);

        if (statType.endsWith(BY_TAG_SYMBOL)) {
            cir.setReturnValue(ScoreUtils.findStatType(Identifier.splitOn(
                            statType.substring(0, statType.length() - BY_TAG_SYMBOL.length()), '.'))
                    .flatMap(type -> StatByTag.create(type, Identifier.splitOn(statName, '.'))));
        } else if (statType.endsWith(REVERSED_SYMBOL)) {
            cir.setReturnValue(ScoreUtils.findStatType(Identifier.splitOn(
                            statType.substring(0, statType.length() - REVERSED_SYMBOL.length()), '.'))
                    .flatMap(type -> StatReversed.create(type, Identifier.splitOn(statName, '.'))));
        }
    }
}
