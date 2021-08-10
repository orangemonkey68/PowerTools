package me.orangemonkey68.powertools.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import me.orangemonkey68.powertools.PowerTools;
import me.orangemonkey68.powertools.nbt.PowerToolData;
import net.minecraft.command.CommandException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PowerToolsCommandManager {
    //TODO: Implement subcommands, suggestions

    private static final Logger LOGGER = PowerTools.LOGGER;

    /**
     * Initializes and registers all PowerTools command trees.
     *
     * <p>
     *     <b>Implementation Notes: </b>
     *          <ul>
     *              <li>Each `.execute()` call is chained to the previous `.argument()` call, not to `.then()`</li>
     *              <li>Each `.then()` call is chained to the previous `.executes()` call</li>
     *          </ul>
     * </p>
     *
     * @param dispatcher The CommandDispatcher from the CommandRegistrationCallback
     * @see net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
     */
    public void initCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                //starts with "/powertools"
                literal("powertools")
                        //adds first argument
                        .then(argument("commands", StringArgumentType.string())
                                //option to execute with only one argument
                                .executes(ctx -> createPowerTool(ctx.getSource(), getString(ctx, "commands"), 0, -1, false))
                                //adds cooldown argument
                                .then(argument("cooldown", LongArgumentType.longArg())
                                        //option to execute and infer the "consume" argument
                                        .executes(ctx -> createPowerTool(ctx.getSource(), getString(ctx, "commands"), getLong(ctx, "cooldown"), -1, false))
                                        //specify if the item should be consumed
                                        .then(argument("maxUses", LongArgumentType.longArg())
                                                //final execute
                                                .executes(ctx -> createPowerTool(ctx.getSource(), getString(ctx, "commands"), getLong(ctx, "cooldown"), getLong(ctx, "maxUses"), false))
                                                .then(argument("consume", BoolArgumentType.bool())
                                                        .executes(ctx -> createPowerTool(ctx.getSource(), getString(ctx, "commands"), getLong(ctx, "cooldown"), getLong(ctx, "maxUses"), getBool(ctx, "consume")))))))
        );
    }

    private static int createPowerTool(ServerCommandSource source, String commandString, long cooldown, long maxUses, boolean consume) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer(); //exits if from console?
        List<String> commands = Arrays.asList(commandString.split(";;"));

        for (String command : commands) {
            ParseResults<ServerCommandSource> results = source.getServer().getCommandManager().getDispatcher().parse(command, source);
            if(results.getReader().canRead()){ //if this is true, it's not valid
                Map<CommandNode<ServerCommandSource>, CommandSyntaxException> exceptions = results.getExceptions();
                if(exceptions.isEmpty()){
                    throw new CommandException(new TranslatableText("text.powertools.unknown-error-powertool-creation").formatted(Formatting.RED));
                } else {
                    exceptions.forEach((node, e) -> {
                        throw new CommandException(new LiteralText(e.getLocalizedMessage()));
                    });
                }
            }
        }

        PowerToolData data = new PowerToolData(commands, cooldown, maxUses, consume, Collections.singletonList(player.getUuid()), 0, false);

        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        if (stack == ItemStack.EMPTY){
            throw new CommandException(new TranslatableText("text.powertools.must-hold-item"));
        }

        stack = PowerTools.NBT_HELPER.setGlintOverride(stack, true);

        stack = PowerTools.NBT_HELPER.getStackWithTag(stack, data);

        player.setStackInHand(Hand.MAIN_HAND, stack);

        StringBuilder prettyCommandString = new StringBuilder();
        for (int i = 0; i < commands.size(); i++) {
            String string = commands.get(i);
            prettyCommandString.append("\"").append(string).append("\"");
            if(!(i == commands.size() - 1)){
                prettyCommandString.append(", ");
            }
        }

        player.sendMessage(new TranslatableText(
                "text.powertools.tool-created",
                prettyCommandString.toString(),
                data.getCooldownTicks(),
                data.getMaxUses() == -1 ? "infinite" : String.valueOf(data.getMaxUses()),
                consume ? "is" : "isn't"
        ).formatted(Formatting.GREEN), false);
        return Command.SINGLE_SUCCESS;
    }
}
