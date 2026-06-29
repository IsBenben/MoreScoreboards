package io.github.isbenben.morescoreboards.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public record RegistryEntryScoreHolder(String translationKey) implements ScoreHolder {
    public static <T> RegistryEntryScoreHolder fromRegistryEntry(Registry<T> registry, RegistryEntry<T> entry) {
        if (entry instanceof RegistryEntry.Reference<?> reference) {
            Object value = reference.value();
            String translationKey;
            if (registry == Registries.ATTRIBUTE) {
                translationKey = ((EntityAttribute) value).getTranslationKey();
            } else if (registry == Registries.ENTITY_TYPE) {
                translationKey = ((EntityType<?>) value).getTranslationKey();
            } else if (registry == Registries.STATUS_EFFECT) {
                translationKey = ((StatusEffect) value).getTranslationKey();
            } else if (registry == Registries.ITEM) {
                translationKey = ((Item) value).getTranslationKey();
            } else if (registry == Registries.BLOCK) {
                translationKey = ((Block) value).getTranslationKey();
            } else if (registry == Registries.CUSTOM_STAT) {
                translationKey = "stat." + value.toString().replace(':', '.');
            } else {
                return null;
            }
            return new RegistryEntryScoreHolder(translationKey);
        }
        return null;
    }

    public static String toColorCode(String input) {
        try {
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            String hexString = hex.toString();
            String last8 = hexString.substring(hexString.length() - 8);
            StringBuilder result = new StringBuilder();
            for (char c : last8.toCharArray()) {
                result.append('§').append(c);
            }
            return result.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    @Override
    public String getNameForScoreboard() {
        return toColorCode(translationKey);
    }

    @Override
    public @NotNull Text getDisplayName() {
        return Text.translatable(translationKey);
    }
}
