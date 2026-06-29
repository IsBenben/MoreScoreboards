package io.github.isbenben.morescoreboards.criterion;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static io.github.isbenben.morescoreboards.MoreScoreboards.MODID;

public class StatReversed<T> extends ScoreboardCriterion {
    public static final String REVERSED_SYMBOL = "_reversed";

    private final StatType<T> statType;
    private final TagKey<T> tagKey;

    public StatReversed(StatType<T> statType, TagKey<T> tagKey) {
        super(getName(statType, tagKey), true, RenderType.INTEGER);
        this.statType = statType;
        this.tagKey = tagKey;
    }

    public static <T> Optional<ScoreboardCriterion> create(StatType<T> statType, @Nullable Identifier id) {
        Registry<T> registry = statType.getRegistry();
        TagKey<T> tagKey = TagKey.of(registry.getKey(), id);
        return Optional.of(new StatReversed<>(statType, tagKey));
    }

    public StatType<T> getStatType() {
        return statType;
    }

    public TagKey<T> getTagKey() {
        return tagKey;
    }

    public <K> boolean contains(RegistryEntry<K> entry) {
        if (Identifier.of(MODID, "total").equals(this.getTagKey().id())) {
            return true;
        }
        if (entry instanceof RegistryEntry.Reference<K> reference) {
            //noinspection unchecked
            return reference.isIn((TagKey<K>) this.getTagKey());
        }
        return false;
    }

    public static <T> String getName(StatType<T> type, TagKey<T> tagKey) {
//        if (type == Register.CONSUMED) {
//            return getName(Identifier.of(MODID, "consumed")) + REVERSED_SYMBOL + ":" + getName(tagKey.id());
//        }
        return getName(Registries.STAT_TYPE.getId(type)) + REVERSED_SYMBOL + ":" + getName(tagKey.id());
    }

    private static String getName(Identifier id) {
        return Objects.requireNonNull(id).toString().replace(':', '.');
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StatReversed<?> statReversed)) return false;
        return Objects.equals(getStatType(), statReversed.getStatType()) && Objects.equals(getTagKey(),
                statReversed.getTagKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatType(), getTagKey());
    }

    @Override
    public String toString() {
        return "StatReversed{" +
                "statType=" + statType +
                ", tagKey=" + tagKey +
                '}';
    }
}
