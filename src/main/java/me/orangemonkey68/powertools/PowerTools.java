package me.orangemonkey68.powertools;

import me.orangemonkey68.powertools.commands.PowerToolsCommandManager;
import me.orangemonkey68.powertools.config.PowerToolsConfig;
import me.orangemonkey68.powertools.event.ItemUseEvent;
import me.orangemonkey68.powertools.nbt.NBTHelper;
import me.orangemonkey68.powertools.nbt.PowerToolData;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.Normalizer;

public class PowerTools implements ModInitializer {
	public static final String MODID = "powertools";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final NBTHelper NBT_HELPER = new NBTHelper();

	private final PowerToolsCommandManager commandManager = new PowerToolsCommandManager();

	@Override
	public void onInitialize() {
		AutoConfig.register(PowerToolsConfig.class, GsonConfigSerializer::new);

		initListeners();
		initCommands();
	}

	private void initListeners(){
		ItemUseEvent.EVENT.register((player, stack, hand) -> {
			NbtCompound tag = stack.getOrCreateNbt();
			if(tag.contains("powerTools")){
				PowerToolData data = NBT_HELPER.getData(stack);
				assert data != null;

				//exit if not allowed
				if (!data.getAllowedPlayers().contains(player.getUuid())) {
					player.sendMessage(new TranslatableText("text.powertools.thats-not-yours").formatted(Formatting.RED, Formatting.BOLD), true);
					return ActionResult.PASS;
				}

				long timeDiffMillis = System.currentTimeMillis() - data.getLastUsedTime();
				float timeRemainingSec = Math.round((System.currentTimeMillis() - data.getLastUsedTime()) / 1000f);

				if (!(timeDiffMillis >= data.getCooldownTicks() * 50)){
					player.sendMessage(new TranslatableText("text.powertools.must-wait", timeRemainingSec).formatted(Formatting.RED, Formatting.BOLD), true);
					return ActionResult.PASS;
				} else {
					data.setLastUsedTime(System.currentTimeMillis());
				}

				//check uses remaining, exit or clear data if needed
				if (data.getMaxUses() == 0) { //This shouldn't ever run, but just in case, we do the final checks
					outOfUses(stack, data, player, hand);
					player.sendMessage(new TranslatableText("text.powertools.out-of-uses").formatted(Formatting.RED, Formatting.BOLD), true);
					return ActionResult.PASS;
				} else if (data.getMaxUses() >= 0) { //check if it's greater than or equal to 0, since -1 will be infinite
					data.setMaxUses(data.getMaxUses() - 1);

					if(data.getMaxUses() > 0){
						player.sendMessage(new TranslatableText("text.powertools.remaining-uses", data.getMaxUses()).formatted(Formatting.GREEN, Formatting.BOLD), true);
					}
				}

				//exit if server is null
				if(player.getServer() == null)
					return ActionResult.PASS;

				ServerCommandSource source = new ServerCommandSource(
						player,
						player.getPos(),
						player.getRotationClient(),
						player.getServerWorld(),
						player.getServer().getPermissionLevel(player.getGameProfile()),
						player.getName().getString(),
						player.getDisplayName(),
						player.getServer(),
						player
				);

				data.getCommandList().forEach(command -> player.getServer().getCommandManager().execute(source, command));

				if(data.getMaxUses() == 0) {
					player.sendMessage(new TranslatableText("text.powertools.out-of-uses").formatted(Formatting.RED, Formatting.BOLD), true);
					outOfUses(stack, data, player, hand);
				}else {
					tag.put("powerTools", data.toTag());
					stack.setNbt(tag);
					player.setStackInHand(hand, stack);
				}
			}

			return ActionResult.PASS;
		});
	}

	private void outOfUses(ItemStack stack, PowerToolData data, ServerPlayerEntity player, Hand hand){
		if(data.isShouldConsume()){
			stack = ItemStack.EMPTY;
		} else {
			stack.getOrCreateNbt().remove("powerTools"); //remove powertools data
			stack = NBT_HELPER.setGlintOverride(stack, false); //clear glint override
		}
		player.setStackInHand(hand, stack);
	}

	public static PowerToolsConfig getConfig() {
		return AutoConfig.getConfigHolder(PowerToolsConfig.class).getConfig();
	}

	private void initCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			commandManager.initCommands(dispatcher);
		});
	}
}
