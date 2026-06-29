package io.github.isbenben.morescoreboards.init;

import io.github.isbenben.morescoreboards.MoreScoreboards;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatType;
import net.minecraft.text.Text;

public class Register {
//    public static final StatType<Item> CONSUMED = createStatType("consumed", Registries.ITEM);

    public static <T> StatType<T> createStatType(String id, Registry<T> registry) {
        Text name = Text.translatable("stat_type." + MoreScoreboards.MODID + "." + id);
        return new StatType<>(registry, name);
    }

    public static void initialize() {
    }
}
