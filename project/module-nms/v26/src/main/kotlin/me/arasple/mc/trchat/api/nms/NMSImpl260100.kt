package me.arasple.mc.trchat.api.nms

import me.arasple.mc.trchat.util.ServerUtil
import net.minecraft.network.chat.Component
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.util.CraftChatMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.chat.ComponentText
import taboolib.module.nms.MinecraftLanguage
import taboolib.module.nms.getLanguageKey
import taboolib.platform.Folia
import taboolib.platform.util.hoverItem
import java.util.*

class NMSImpl260100 : NMS() {

    override fun craftChatMessageFromComponent(component: ComponentText): Any {
        return CraftChatMessage.fromJSON(component.toRawMessage())
    }

    override fun rawMessageFromCraftChatMessage(component: Any): String {
        return CraftChatMessage.toJSON(component as Component)
    }

    override fun sendMessage(receiver: Player, component: ComponentText, sender: UUID?, usePacket: Boolean) {
        if (Folia.isFolia) {
            (receiver as net.kyori.adventure.audience.Audience).sendMessage(component.toAdventureObject())
            return
        }
        if (!usePacket || ServerUtil.isModdedServer) {
            component.sendTo(adaptPlayer(receiver))
            return
        }
        val player = (receiver as CraftPlayer).handle
        player.sendSystemMessage(craftChatMessageFromComponent(component) as Component)
    }

    override fun hoverItem(component: ComponentText, itemStack: ItemStack): ComponentText {
        return component.hoverItem(itemStack)
    }

    override fun optimizeNBT(itemStack: ItemStack, nbtWhitelist: Array<String>): ItemStack {
        return itemStack
    }

    override fun getLocaleKey(itemStack: ItemStack): MinecraftLanguage.LanguageKey {
        return itemStack.getLanguageKey()
    }
}