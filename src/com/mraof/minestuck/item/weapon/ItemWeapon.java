package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

public abstract class ItemWeapon extends Item
{
	protected int weaponDamage;
	
	public ItemWeapon() 
	{
		this.maxStackSize = 1;
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	protected abstract int getAttackDamage();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Multimap getItemAttributeModifiers()
	{
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(this.itemModifierUUID, "Tool Modifier", (double)this.getAttackDamage(), 0));
		return multimap;
	}
	
}
