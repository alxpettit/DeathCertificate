package xyz.achu.deathcert.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.text.LiteralText;

import static java.lang.Math.ceil;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity {

	@Shadow public abstract ServerWorld getServerWorld();

	public ServerPlayerEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("RETURN"))
	public void onDeathInject(DamageSource source, CallbackInfo ci) {
		LiteralText deathPosMsg =
				new LiteralText("Player " +
						this.getEntityName() + " has been issued a death certificate! Coords: " +
						(int)ceil(this.getX()) + ", " +
						(int)ceil(this.getY()) + ", " +
						(int)ceil(this.getZ())
				);
		this.getServerWorld().getServer().getPlayerManager()
						.broadcastChatMessage(deathPosMsg, MessageType.SYSTEM, Util.NIL_UUID);

	}
}
