package me.arasple.mc.trchat.module.display.function

import me.arasple.mc.trchat.module.display.format.JsonComponent
import me.arasple.mc.trchat.module.internal.script.Condition
import me.arasple.mc.trchat.module.internal.script.Reaction
import me.arasple.mc.trchat.util.getCooldownLeft
import me.arasple.mc.trchat.util.isInCooldown
import me.arasple.mc.trchat.util.pass
import me.arasple.mc.trchat.util.updateCooldown
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.util.replaceWithOrder
import taboolib.module.chat.ComponentText

/**
 * @author ItsFlicker
 * @since 2021/12/12 11:41
 */
class CustomFunction(
    id: String,
    val condition: Condition?,
    val priority: Int,
    val regex: Regex,
    val filterTextRegex: Regex?,
    val whitelist: List<String>? = null,
    val cooldownMillis: Long?,
    val cooldownMessage: String?,
    val displayJson: JsonComponent,
    override val reaction: Reaction?
) : Function(id) {

    override fun createVariable(sender: Player, message: String): String {
        return message.replaceRegex(regex, filterTextRegex) { matched ->
            if (whitelist != null && whitelist.any { it.equals(matched, ignoreCase = true) }) {
                matched
            } else {
                "{{$id:${push(matched)}}}"
            }
        }
    }

    override fun parseVariable(sender: Player, arg: String): ComponentText {
        if (cooldownMessage == null && cooldownMillis != null && !sender.hasPermission("trchat.bypass.customcd")) {
            sender.updateCooldown(id, cooldownMillis)
        }
        return displayJson.toTextComponent(sender, arg)
    }

    override fun canUse(sender: Player): Boolean {
        return condition.pass(sender) && (cooldownMillis == null || !sender.isInCooldown(id))
    }

    override fun checkCooldown(sender: Player, message: String): Boolean {
        if (cooldownMessage == null || cooldownMillis == null) {
            return true
        }
        if (message.contains(regex) && !sender.hasPermission("trchat.bypass.customcd")) {
            val cooldown = sender.getCooldownLeft(id)
            if (cooldown > 0) {
                adaptPlayer(sender).sendActionBar(cooldownMessage.replaceWithOrder(cooldown / 1000))
                return false
            } else {
                sender.updateCooldown(id, cooldownMillis)
            }
        }
        return true
    }

    companion object {

        fun String.replaceRegex(regex: Regex, replaceRegex: Regex?, replacement: (String) -> String): String {
            return replace(regex) {
                val str = it.value
                val result = replaceRegex?.find(str)?.value ?: str
                replacement(result)
            }
        }
    }
}