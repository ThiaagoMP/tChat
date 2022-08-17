package br.com.thiago.tchat.spigot.utils

import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.potion.PotionEffect
import java.util.function.Consumer

class ItemBuilder : ItemStack {
    constructor(itemStack: ItemStack) : super(itemStack)
    constructor(material: Material) : super(material)
    constructor(material: Material, quantity: Int, durability: Short) : super(material, quantity, durability)

    fun setAmounts(amount: Int): ItemBuilder {
        setAmount(amount)
        return this
    }

    fun setDurabilitys(durability: Short): ItemBuilder {
        setDurability(durability)
        return this
    }

    fun toItemStack(): ItemStack {
        return ItemStack(this)
    }

    fun setDisplayName(name: String): ItemBuilder {
        val itemMeta = itemMeta
        itemMeta!!.displayName = name.replace('&', '§')
        setItemMeta(itemMeta)
        return this
    }

    fun setBaseColor(color: DyeColor): ItemBuilder {
        val bannerMeta = itemMeta as BannerMeta
        bannerMeta.baseColor = color
        itemMeta = bannerMeta
        return this
    }

    fun setLore(lore: MutableList<String>): ItemBuilder {
        val itemMeta = itemMeta
        val loreMutable: MutableList<String> = lore
        loreMutable.replaceAll { s: String -> s.replace("&", "§") }
        itemMeta!!.lore = loreMutable
        setItemMeta(itemMeta)
        return this
    }

    fun setLore(vararg lore: String): ItemBuilder {
        val itemMeta = itemMeta
        val loreMutable: MutableList<String> = lore.toMutableList()
        loreMutable.replaceAll { s: String -> s.replace("&", "§") }
        itemMeta!!.lore = loreMutable
        setItemMeta(itemMeta)
        return this
    }

    fun addLore(vararg lore: String): ItemBuilder {
        lore.forEach { addLineLore(it) }
        return this
    }

    fun replaceLore(stringBefore: String, stringAfter: String): ItemBuilder {
        val itemMeta = itemMeta
        val lore = itemMeta!!.lore
        lore!!.replaceAll { string: String -> string.replace(stringBefore, stringAfter) }
        itemMeta.lore = lore
        setItemMeta(itemMeta)
        return this
    }

    fun addLineLore(line: String): ItemBuilder {
        val lore: MutableList<String> = ArrayList()
        if (itemMeta!!.hasLore()) {
            itemMeta!!.lore?.let { lore.addAll(it) }
        }
        lore.add(line)
        val itemMeta = itemMeta
        itemMeta!!.lore = lore
        setItemMeta(itemMeta)
        return this
    }

    fun clearLore(): ItemBuilder {
        val itemMeta = itemMeta
        itemMeta!!.lore = ArrayList()
        setItemMeta(itemMeta)
        return this
    }

    fun addFlag(flag: ItemFlag): ItemBuilder {
        val itemMeta = itemMeta
        itemMeta!!.addItemFlags(flag)
        setItemMeta(itemMeta)
        return this
    }

    fun addFlag(vararg flag: ItemFlag): ItemBuilder {
        val itemMeta = itemMeta
        itemMeta!!.addItemFlags(*flag)
        setItemMeta(itemMeta)
        return this
    }

    fun addEnchant(enchantment: Enchantment, level: Int): ItemBuilder {
        addUnsafeEnchantment(enchantment, level)
        return this
    }

    fun addEnchant(enchantments: List<Enchantment>, level: Int): ItemBuilder {
        enchantments.forEach(Consumer { enchantment: Enchantment -> addUnsafeEnchantment(enchantment, level) })
        return this
    }

    fun setUnbreakable(b: Boolean): ItemBuilder {
        val itemMeta = itemMeta
        itemMeta!!.isUnbreakable = b
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        setItemMeta(itemMeta)
        return this
    }

    fun setOwner(owner: String): ItemBuilder {
        val skullMeta = itemMeta as SkullMeta
        skullMeta.owner = owner
        itemMeta = skullMeta
        return this
    }

    fun setColor(color: Color): ItemBuilder {
        val itemMeta = itemMeta as LeatherArmorMeta
        itemMeta.color = color
        setItemMeta(itemMeta)
        return this
    }

    fun setColor(color: DyeColor): ItemBuilder {
        durability = color.dyeData.toShort()
        return this
    }

    fun setFireworkColor(color: Color): ItemBuilder {
        val itemMeta = itemMeta
        val fireworkMeta = itemMeta as FireworkEffectMeta
        val fireworkEffect = FireworkEffect.builder().withColor(color).build()
        fireworkMeta.effect = fireworkEffect
        setItemMeta(fireworkMeta)
        return this
    }

    fun setFireworkColor(blue: Int, green: Int, red: Int): ItemBuilder {
        val itemMeta = itemMeta
        val fireworkMeta = itemMeta as FireworkEffectMeta
        val fireworkEffect = FireworkEffect.builder().withColor(Color.fromBGR(blue, green, red)).build()
        fireworkMeta.effect = fireworkEffect
        setItemMeta(fireworkMeta)
        return this
    }

    fun addPotionEffect(potionEffect: PotionEffect): ItemBuilder {
        val itemMeta = itemMeta as PotionMeta
        itemMeta.addCustomEffect(potionEffect, true)
        setItemMeta(itemMeta)
        return this
    }
}