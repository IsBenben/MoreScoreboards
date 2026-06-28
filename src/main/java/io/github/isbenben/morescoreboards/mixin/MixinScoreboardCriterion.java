package io.github.isbenben.morescoreboards.mixin;

import io.github.isbenben.morescoreboards.criterion.StatByTag;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static io.github.isbenben.morescoreboards.criterion.StatByTag.BY_TAG_SYMBOL;

@Mixin(ScoreboardCriterion.class)
public abstract class MixinScoreboardCriterion {
    @Unique
    private static <T> Optional<ScoreboardCriterion> getModStatCriterion(StatType<T> statType,
                                                                         @Nullable Identifier id) {
        Registry<T> registry = statType.getRegistry();
        TagKey<T> tagKey = TagKey.of(registry.getKey(), id);
        return Optional.of(new StatByTag<>(statType, tagKey));
    }

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
            cir.setReturnValue(Registries.STAT_TYPE
                    .getOptionalValue(Identifier.splitOn(
                            statType.substring(0, statType.length() - BY_TAG_SYMBOL.length()), '.'))
                    .flatMap(type -> getModStatCriterion(type, Identifier.splitOn(statName, '.'))));
        }
    }
}
