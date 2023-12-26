package org.pokesplash.rbt.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.rbt.util.LuckPermsUtils;
import org.pokesplash.rbt.util.Utils;

import java.lang.reflect.UndeclaredThrowableException;

public class BaseCommand {
	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> root = CommandManager
				.literal("rbt")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".base");
					} else {
						return true;
					}
				})
				.executes(this::run);

		LiteralCommandNode<ServerCommandSource> registeredCommand = dispatcher.register(root);

		registeredCommand.addChild(new ReloadCommand().build());
		registeredCommand.addChild(new GiveCommand().build());
		registeredCommand.addChild(new DebugCommand().build());
		registeredCommand.addChild(new RerollCommand().build());
		registeredCommand.addChild(new CreateCommand().build());
		registeredCommand.addChild(new JoinCommand().build());
		registeredCommand.addChild(new LeaveCommand().build());

	}

	public int run(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.literal(
				Utils.formatMessage("§2§lRBT", context.getSource().isExecutedByPlayer())
		));
		return 1;
	}
}
