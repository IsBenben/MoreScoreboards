package io.github.isbenben.morescoreboards.mixin;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConsumableComponent.class)
public abstract class MixinConsumableComponent {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;incrementStat" +
            "(Lnet/minecraft/stat/Stat;)V"), method = "finishConsumption")
    private void incrementStat(World world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
//        ((ServerPlayerEntity) user).incrementStat(Register.CONSUMED.getOrCreateStat(stack.getItem()));
    }
}
