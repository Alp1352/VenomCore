package com.venom.venomcore.plugin.menu.premade;

import com.venom.venomcore.plugin.chat.Color;
import com.venom.venomcore.plugin.compatibility.CompatibleMaterial;
import com.venom.venomcore.plugin.compatibility.CompatibleSound;
import com.venom.venomcore.plugin.item.ItemBuilder;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import com.venom.venomcore.plugin.menu.internal.item.action.ClickAction;
import com.venom.venomcore.plugin.menu.types.AnvilGUI;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
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
