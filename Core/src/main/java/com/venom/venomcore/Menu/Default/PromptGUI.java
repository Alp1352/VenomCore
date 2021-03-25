package com.venom.venomcore.Menu.Default;

import com.venom.venomcore.Chat.Color;
import com.venom.venomcore.Compatibility.CompatibleMaterial;
import com.venom.venomcore.Compatibility.CompatibleSound;
import com.venom.venomcore.Item.ItemBuilder;
import com.venom.venomcore.Menu.Internal.Item.Action.ClickAction;
import com.venom.venomcore.Menu.Internal.Item.MenuItem;
import com.venom.venomcore.Menu.Types.AnvilGUI;
import com.venom.venomcore.Plugin.VenomPlugin;
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

        setSoundAfterRename(CompatibleSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound());
        if (prompt != null) {
            setActionAfterRename(prompt);
        }
    }

    public void setPrompt(ClickAction prompt) {
        this.prompt = prompt;
    }
}
