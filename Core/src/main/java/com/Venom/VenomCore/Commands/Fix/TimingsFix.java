package com.Venom.VenomCore.Commands.Fix;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.plugin.SimplePluginManager;
import org.spigotmc.CustomTimingsHandler;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class TimingsFix extends TimingsCommand {

    public TimingsFix(String name) {
        super(name);
    }

    @Override
    public void executeSpigotTimings(@Nonnull final CommandSender sender, @Nonnull final String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Bir argüman girin! (on : off : paste : report : merge).");
            return;
        }

        if ("on".equals(args[0])) {
            ((SimplePluginManager)Bukkit.getPluginManager()).useTimings(true);

            CustomTimingsHandler.reload();

            sender.sendMessage("Timings aktif.");
            return;
        }

        if ("off".equals(args[0])) {
            ((SimplePluginManager)Bukkit.getPluginManager()).useTimings(false);

            sender.sendMessage("Timings kapandı.");
            return;
        }

        if (!Bukkit.getPluginManager().useTimings()) {
            sender.sendMessage("/timings on ile timingsı açmanız gerekiyor.");
            return;
        }


        if ("reset".equals(args[0])) {
            CustomTimingsHandler.reload();
            sender.sendMessage("Timings sıfırlandı.");
            return;
        }

        final boolean paste = args[0].equals("paste");
        final boolean merge = args[0].equals("merged");
        final boolean report = args[0].equals("report");

        if (merge || report || paste) {
            final long sampleTime = System.nanoTime() - TimingsCommand.timingStart;

            final File timingFolder = new File("timings");
            timingFolder.mkdirs();

            File timings = new File(timingFolder, "timings.txt");

            final ByteArrayOutputStream bout = paste ? new ByteArrayOutputStream() : null;

            int index = 0;
            while (timings.exists()) {
                timings = new File(timingFolder, "timings" + ++index + ".txt");
            }

            try (PrintStream fileTimings = paste ? new PrintStream(bout) : new PrintStream(timings)){

                CustomTimingsHandler.printTimings(fileTimings);
                fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1.0E9 + "s)");
                fileTimings.println("<spigotConfig>");
                fileTimings.println(Bukkit.spigot().getConfig().saveToString());
                fileTimings.println("</spigotConfig>");

                if (paste) {
                    new PasteThread(sender, bout).start();
                }

                sender.sendMessage("Timings " + timings.getPath() + " dosyasına yazıldı!");
                sender.sendMessage("http://www.spigotmc.org/go/timings linki ile bakabilirsiniz.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class PasteThread extends Thread {
        private final CommandSender sender;
        private final ByteArrayOutputStream bout;

        public PasteThread(final CommandSender sender, final ByteArrayOutputStream bout) {
            super("Timings paste thread");
            this.sender = sender;
            this.bout = bout;
        }

        @Override
        public synchronized void start() {
            if (this.sender instanceof RemoteConsoleCommandSender) {
                this.run();
            } else {
                super.start();
            }
        }

        @Override
        public void run() {
            try {
                final HttpURLConnection con = (HttpURLConnection) new URL("https://timings.spigotmc.org/paste").openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setInstanceFollowRedirects(false);

                final OutputStream out = con.getOutputStream();
                out.write(this.bout.toByteArray());
                out.close();

                final JsonObject location = new Gson().fromJson(new InputStreamReader(con.getInputStream()), JsonObject.class);
                con.getInputStream().close();

                final String pasteID = location.get("key").getAsString();
                this.sender.sendMessage(ChatColor.GREEN + "Timings sonuçlarına şuradan bakabilirsiniz: https://www.spigotmc.org/go/timings?url=" + pasteID);
            } catch (IOException ex) {
                this.sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
                Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ex);
            }
        }
    }

}
