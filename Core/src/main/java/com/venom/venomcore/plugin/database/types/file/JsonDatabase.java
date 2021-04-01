package com.venom.venomcore.plugin.database.types.file;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;

import java.io.File;

public class JsonDatabase extends FileDatabase {

    public JsonDatabase(File file) {
        super(LightningBuilder.fromFile(file)
                .setConfigSettings(ConfigSettings.SKIP_COMMENTS)
                .setDataType(DataType.UNSORTED)
                .setReloadSettings(ReloadSettings.MANUALLY)
                .createJson());
    }
}
