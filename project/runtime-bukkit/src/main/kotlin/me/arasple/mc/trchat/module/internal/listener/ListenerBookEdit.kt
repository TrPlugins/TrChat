package me.arasple.mc.trchat.module.internal.listener

import me.arasple.mc.trchat.util.color.MessageColors
import org.bukkit.event.player.PlayerEditBookEvent
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.configuration.ConfigNode
import me.arasple.mc.trchat.module.internal.TrChatBukkit
import me.arasple.mc.trchat.util.data
import me.arasple.mc.trchat.util.session
import org.bukkit.entity.Player
import taboolib.platform.util.sendLang


/**
 * @author ItsFlicker
 * @date 2019/8/15 21:18
 */
@PlatformSide(Platform.BUKKIT)
object ListenerBookEdit {

    @ConfigNode("Color.Book", "settings.yml")
    var color = true
        private set

    @ConfigNode("Chat.Book-Edit-Permission-Check", "settings.yml")
    var bookEditPermissionCheck = false

    @Suppress("Deprecation")
    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBookEdit(e: PlayerEditBookEvent) {
        val player = e.player
        if (bookEditPermissionCheck) {
            if (!player.hasPermission("trchat.bypass.bookedit") && !canSpeak(player)) {
                e.isCancelled = true
                player.sendLang("Book-Edit-No-Permission")
                return
            }
        }

        val p = e.player
        val meta = e.newBookMeta
        if (color) {
            meta.pages = MessageColors.replaceWithPermission(p, meta.pages, MessageColors.Type.BOOK)
        }
        e.newBookMeta = meta
    }

    @SubscribeEvent(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onBookEditCheck(e: PlayerEditBookEvent) {
        if (!bookEditPermissionCheck) return
        val player = e.player
        if (!player.hasPermission("trchat.bypass.bookedit") && !canSpeak(player)) {
            e.isCancelled = true
            player.sendLang("Book-Edit-No-Permission")
        }
    }

    private fun canSpeak(player: Player): Boolean {
        if (TrChatBukkit.isGlobalMuting && !player.hasPermission("trchat.bypass.globalmute")) return false
        if (player.data.isMuted) return false
        val channel = player.session.getChannel()
        return channel == null || channel.canSpeak(player)
    }
}