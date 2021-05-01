package com.venom.venomcore.plugin.chat.conversations.conv;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface ConversationGetter<T> {
    T get(String string);

    static ConversationGetter<Number> number() {
        return string -> NumberUtils.isNumber(string) ?
                NumberUtils.createNumber(string) :
                null;
    }

    static ConversationGetter<String> string() {
        return String::toString; // :P
    }

    static ConversationGetter<Player> player() {
        return Bukkit::getPlayer;
    }

    static ConversationGetter<Boolean> bool() {
        return string -> string == null || string.isEmpty() ? null :
                string.equalsIgnoreCase("true") ? Boolean.TRUE : string.equalsIgnoreCase("false") ? Boolean.FALSE : null;
    }


}
