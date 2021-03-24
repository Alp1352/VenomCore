package com.Venom.VenomCore.Commands;

import com.Venom.VenomCore.Chat.Color;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class PluginCommand extends com.Venom.VenomCore.Commands.Command implements CommandExecutor, TabCompleter {
    private final Set<SubCommand> subCommands = new HashSet<>();
    private final JavaPlugin plugin;
    private final String command;
    private boolean allowConsole = false;
    private String noConsoleMessage = "&cBu komut konsoldan calistirilamaz.";

    public PluginCommand(@NotNull JavaPlugin plugin, @NotNull String command, String... aliases) {
        super(plugin.getDescription().getName() + "." + command);

        this.command = command;
        org.bukkit.command.PluginCommand pluginCommand = plugin.getCommand(command);

        Validate.notNull(pluginCommand, "Command cannot be null.");

        this.plugin = plugin;
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);

        pluginCommand.setAliases(Arrays.asList(aliases));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (!allowConsole() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Color.translate(noConsoleMessage));
            return false;
        }

        if (args.length == 0) {
            if (sender.hasPermission(getPermission())) {
                return execute(sender, command, args);
            } else {
                sender.sendMessage(Color.translate(getPermErrorMessage()));
                return false;
            }
        }

        StringBuilder argumentBuilder = new StringBuilder();
        for (String arg : args) {
            argumentBuilder.append(arg).append(" ");
        }

        String arguments = argumentBuilder.toString().trim();

        SubCommand subCommand = subCommands.stream()
                    .filter(subCmd -> Arrays.equals(Arrays.copyOf(args, subCmd.getSplit().length), subCmd.getSplit()))
                    .max(Comparator.comparing(subCmd -> subCmd.getSplit().length))
                    .orElse(null);

        if (subCommand == null) {
            catchUnknown(sender, arguments);
            return false;
        }

        if (sender.hasPermission(subCommand.getPermission())) {
            return subCommand.execute(sender, command, Arrays.copyOfRange(args, subCommand.getSplit().length, args.length));
        } else {
            sender.sendMessage(Color.translate(subCommand.getPermErrorMessage().replace("%plugin%", plugin.getName())));
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        if (!command.getLabel().equalsIgnoreCase(this.command)) {
            return null;
        }

        /*
        Set<String> possible = subCommands.stream()
                .filter(subCommand -> sender.hasPermission(subCommand.getPermission())) // check sender perms.
                .filter(subCommand -> subCommand.getSplit().length >= args.length) // check arg length.
                .filter(subCommand -> !subCommand.isCommandPrivate()) // check if the command is private.
                .filter(subCommand -> Arrays.equals(Arrays.copyOf(subCommand.getSplit(), args.length - 1), Arrays.copyOf(args, args.length - 1))) // check if nested args are equal.)
                .map(subCommand -> subCommand.getSplit()[args.length - 1]) // convert subcommands into string
                .collect(Collectors.toSet()); // get the set

        We stopped using streams as it is really slow. (Around 3-4 times)
        Below we do the same thing as above, just by using old fashion for loop.
         */

        Set<String> possible = new HashSet<>();
        int index = args.length - 1;

        for (SubCommand subCommand : subCommands) {
            String[] split = subCommand.getSplit();
            if (sender.hasPermission(subCommand.getPermission()) &&
                    split.length >= args.length &&
                    !subCommand.isCommandPrivate() &&
                    Arrays.equals(Arrays.copyOf(split, index), Arrays.copyOf(args, index))) {

                possible.add(split[index]);
            }
        }

        List<String> possibleCompletes = StringUtil.copyPartialMatches(args[index], possible, new ArrayList<>());
        Collections.sort(possibleCompletes);
        return possibleCompletes;
    }

    public abstract boolean execute(CommandSender sender, Command command, String[] args);

    public abstract void catchUnknown(CommandSender sender, String unknownArgs);

    public String getNoConsoleMessage() {
        return noConsoleMessage;
    }

    public void setNoConsoleMessage(String noConsoleMessage) {
        this.noConsoleMessage = noConsoleMessage;
    }

    public void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public void addSubCommand(SubCommand... commands) {
        subCommands.addAll(Arrays.asList(commands));
    }

    public void setAllowConsole(boolean allowConsole) {
        this.allowConsole = allowConsole;
    }

    public boolean allowConsole() {
        return allowConsole;
    }
}
