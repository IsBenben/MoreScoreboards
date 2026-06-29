package io.github.isbenben.morescoreboards.mixin;

import com.google.common.collect.Lists;
import io.github.isbenben.morescoreboards.criterion.StatByTag;
import io.github.isbenben.morescoreboards.criterion.StatReversed;
import net.minecraft.command.argument.ScoreboardCriterionArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(ScoreboardCriterionArgumentType.class)
public abstract class MixinScoreboardCriterionArgumentType {
    @ModifyArg(method = "listSuggestions",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/command/CommandSource;suggestMatching(Ljava/lang/Iterable;" +
                            "Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)" +
                            "Ljava/util/concurrent/CompletableFuture;"),
            index = 0
    )
    private Iterable<String> onSuggestMatchingArg(Iterable<String> original) {
        List<String> newSuggestions = Lists.newArrayList(original);

        for (StatType<?> statType : Registries.STAT_TYPE) {
            Identifier statId = Registries.STAT_TYPE.getId(statType);
            if (statId != null) {
                newSuggestions.add(statId.toString().replace(':', '.')
                        + StatByTag.BY_TAG_SYMBOL + ":"
                        + "morescoreboards.total");
                newSuggestions.add(statId.toString().replace(':', '.')
                        + StatReversed.REVERSED_SYMBOL + ":"
                        + "morescoreboards.total");
            }
        }

        for (StatType<?> statType : Registries.STAT_TYPE) {
            Identifier statId = Registries.STAT_TYPE.getId(statType);
            Registry<?> registry = statType.getRegistry();
            registry.getTags()
                    .filter(tag -> statId != null)
                    .map(tag -> statId.toString().replace(':', '.')
                            + StatByTag.BY_TAG_SYMBOL + ":"
                            + tag.getTag().id().toString().replace(':', '.')).
                    forEach(newSuggestions::add);
            registry.getTags()
                    .filter(tag -> statId != null)
                    .map(tag -> statId.toString().replace(':', '.')
                            + StatReversed.REVERSED_SYMBOL + ":"
                            + tag.getTag().id().toString().replace(':', '.')).
                    forEach(newSuggestions::add);
        }
        return newSuggestions;
    }
}
