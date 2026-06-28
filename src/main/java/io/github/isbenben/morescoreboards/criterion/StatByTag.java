package io.github.isbenben.morescoreboards.criterion;

import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;

import java.util.Objects;

import static io.github.isbenben.morescoreboards.MoreScoreboards.MODID;

public class StatByTag<T> extends ScoreboardCriterion {
    public static final String BY_TAG_SYMBOL = "_by_tag";

    private final StatType<T> statType;
    private final TagKey<T> tagKey;

    public StatByTag(StatType<T> statType, TagKey<T> tagKey) {
        super(getName(statType, tagKey), true, RenderType.INTEGER);
        this.statType = statType;
        this.tagKey = tagKey;
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
        return getName(Registries.STAT_TYPE.getId(type)) + BY_TAG_SYMBOL + ":" + getName(tagKey.id());
    }

    private static String getName(Identifier id) {
        return Objects.requireNonNull(id).toString().replace(':', '.');
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StatByTag<?> statByTag)) return false;
        return Objects.equals(getStatType(), statByTag.getStatType()) && Objects.equals(getTagKey(),
                statByTag.getTagKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatType(), getTagKey());
    }

    @Override
    public String toString() {
        return "StatByTag{" +
                "statType=" + statType +
                ", tagKey=" + tagKey +
                '}';
    }
}
