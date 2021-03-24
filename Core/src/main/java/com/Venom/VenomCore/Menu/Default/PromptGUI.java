package com.Venom.VenomCore.Menu.Default;

import com.Venom.VenomCore.Chat.Color;
import com.Venom.VenomCore.Compatibility.CompatibleMaterial;
import com.Venom.VenomCore.Compatibility.CompatibleSound;
import com.Venom.VenomCore.Item.ItemBuilder;
import com.Venom.VenomCore.Menu.Internal.Item.Action.ClickAction;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;
import com.Venom.VenomCore.Menu.Types.AnvilGUI;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import org.jetbrains.annotations.Nullable;

public class PromptGUI extends AnvilGUI {
    private ClickAction prompt;
    private final String promptText;
    public PromptGUI(VenomPlugin plugin, String title, @Nullable String promptText, ClickAction onPrompt) {
        super(plugin, null, title);
        this.prompt = onPrompt;
        this.promptText = promptText;
        setup();
    }

    public void setup() {
        ItemBuilder paper = new ItemBuilder(CompatibleMaterial.PAPER.parseItem());
        paper.setName(Color.translate(promptText));
        MenuItem item = new MenuItem(paper);
        getUpperContainer().set(item, 0);

        setSoundAfterRename(CompatibleSound.ENTITY_EXPERIENCE_ORB_PICKUP);
        if (prompt != null) {
            setActionAfterRename(prompt);
        }
    }

    public void setPrompt(ClickAction prompt) {
        this.prompt = prompt;
    }
}
