package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.LandAttribute.EnumAttribute;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class LandHelper
{
	
	private static ArrayList<LandAspect> landAspects = new ArrayList<LandAspect>();
	private static Hashtable<String, LandAspect> landNames = new Hashtable<String,LandAspect>();
	private static HashMap<EnumAttribute, Object> defaultAttributes = new HashMap<EnumAttribute, Object>();
	public static int features = 4;
	private Random random;
	
	public LandHelper(long seed)
	{
		random = new Random(seed);
		defaultAttributes.put(EnumAttribute.SURFACE_BLOCK, new BlockWithMetadata(Blocks.stone));
		defaultAttributes.put(EnumAttribute.GROUND_BLOCK, new BlockWithMetadata(Blocks.stone));
		defaultAttributes.put(EnumAttribute.DAY_CYCLE, 0);
	}
	
	/**
	 * Adds a new Land aspect to the table of random aspects to generate.
	 * @param newAspect
	 */
	public static void registerLandAspect(LandAspect newAspect) {
		landAspects.add(newAspect);
		landNames.put(newAspect.getPrimaryName(),newAspect);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @param playerTitle
	 * @return
	 */
	public LandAspect getLandAspect() {
		while (true) {
			LandAspect newAspect = (LandAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getRarity() < random.nextFloat()) {
				return newAspect;
			}
		}
	}
	
	/**
	 * Generates a random land aspect. Used for getting a second aspect, as it will make sure not to repeat the aspect given.
	 * @param playerTitle
	 * @param firstAspect
	 * @return
	 */
	public LandAspect getLandAspect(LandAspect firstAspect) {
		while (true) {
			LandAspect newAspect = (LandAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getRarity() < random.nextLong() && newAspect != firstAspect) {
				return newAspect;
			}
		}
	}
	
	/**
	 * Given two aspects, pick one ot random. Used in finding which aspect conrols what part of world gen.
	 */
	public LandAspect pickOne(LandAspect aspect1,LandAspect aspect2) {
		if (random.nextBoolean()) {
			return aspect1;
		} else {
			return aspect2;
		}
	}
	
	/**
	 * Returns a ArrayList that is a random combination of the two input ArrayLists.
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList pickSubset(ArrayList list1, ArrayList list2) {
		ArrayList<Object> result = new ArrayList<Object>();
		for (Object obj : list1) {
			if (random.nextBoolean())
				result.add(obj);
		}
		for (Object obj : list2) {
			if (random.nextBoolean())
				result.add(obj);
		}
		return result;
	}
	
	/**
	 * Converts aspect data to NBT tags for saving/loading.
	 */
	public static NBTTagCompound toNBT(LandAspect aspect1,LandAspect aspect2) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("aspect1",aspect1.getPrimaryName());
		tag.setString("aspect2",aspect2.getPrimaryName());
		return tag;
	}
	
	/**
	 * Gets a land aspect from it's primary name. Used in loading from NBT.
	 */
	public static LandAspect fromName(String name) {
		return (LandAspect)landNames.get(name);
		
	}
	
	/**
	 * Registers a new dimension for a land. Returns the ID of the nearest open land ID.
	 * @param player 
	 * 
	 */
	public static int createLand(EntityPlayer player) {

		int newLandId = Minestuck.landDimensionIdStart;
		
		while (true) {
			if (DimensionManager.getWorld(newLandId) == null && !MinestuckSaveHandler.lands.contains((byte)newLandId)) {
				break;
			} else {
				newLandId++;
			}
		}
		DimensionManager.registerDimension(newLandId, Minestuck.landProviderTypeId);
		Debug.print("Creating land with id of: " + newLandId);
		if(!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("LandId", newLandId);
		MinestuckSaveHandler.lands.add((byte) newLandId);
		MinestuckPlayerTracker.updateLands();
		
		return newLandId;
	}
	
	/**
	 * Returns one random element from a list.
	 */
	public Object pickElement(Object[] list) {
		return list[random.nextInt(list.length)];
	}
	
	public void generateData(ChunkProviderLands chunkprovider)
	{
		EnumMap<EnumAttribute, ArrayList<LandAttribute>> attributes = new EnumMap<EnumAttribute, ArrayList<LandAttribute>>(EnumAttribute.class);
		EnumMap<EnumAttribute, Object> result = new EnumMap<EnumAttribute, Object>(EnumAttribute.class);
		
		for(LandAttribute attribute1 : chunkprovider.aspect1.getAttributes())
		{
			if(!attributes.containsKey(attribute1.type))
				attributes.put(attribute1.type, new ArrayList<LandAttribute>());
				attributes.get(attribute1.type).add(attribute1);
		}
		for(LandAttribute attribute2 : chunkprovider.aspect2.getAttributes())
		{
			if(!attributes.containsKey(attribute2.type))
				attributes.put(attribute2.type, new ArrayList<LandAttribute>());
				attributes.get(attribute2.type).add(attribute2);
		}
		
		for(EnumAttribute type : EnumAttribute.values())
		{
			double random = this.random.nextDouble();
			if(!type.multiValued)
			{
				ArrayList<LandAttribute> lowPriority = new ArrayList<LandAttribute>();
				ArrayList<LandAttribute> highPriority = new ArrayList<LandAttribute>();
				Iterator<LandAttribute> iter = attributes.get(type).iterator();
				for(LandAttribute attribute : attributes.get(type))
				{
					if(attribute.priority == 5)
					{
						highPriority.clear();
						highPriority.add(attribute);
						lowPriority.clear();
						break;
					}
					if(highPriority.isEmpty())
					{
						highPriority.add(attribute);
						continue;
					}
					int prevPriority = highPriority.get(0).priority;
					if(prevPriority < attribute.priority)
					{
						if(prevPriority + 1 == attribute.priority)
						{
							lowPriority = highPriority;
							highPriority = new ArrayList<LandAttribute>();
						}
						else
						{
							lowPriority.clear();
							highPriority.clear();
						}
					}
					if(prevPriority <= attribute.priority)
						highPriority.add(attribute);
					else if(prevPriority - 1 == attribute.priority)
						lowPriority.add(attribute);
				}
				if(highPriority.isEmpty())
					result.put(type, defaultAttributes.get(type));
				else
				{
					int total = lowPriority.size() + (highPriority.size()*3);
					int resultID = (int) random*total;
					if(resultID < lowPriority.size())
						result.put(type, lowPriority.get(resultID));
					else result.put(type, highPriority.get((resultID - lowPriority.size())/3));
				}
			}
			else
			{
				ArrayList<LandAttribute> attributeList = attributes.get(type);
				if(attributeList.isEmpty())
					continue;
				if(attributeList.size() <= features)
					result.put(type, attributeList);
				else
				{
					ArrayList<LandAttribute> results = new ArrayList<LandAttribute>();
					ArrayList<LandAttribute> prevPriority = new ArrayList<LandAttribute>();
					priorityLoop: for(int priority = 5; priority > 0 && !attributeList.isEmpty() && results.size() < features; priority++)
					{
						Iterator<LandAttribute> iter = attributeList.iterator();
						ArrayList<LandAttribute> list = new ArrayList<LandAttribute>();
						while(iter.hasNext())
						{
							LandAttribute attribute = iter.next();
							if(attribute.priority == 5)
							{
								results.add(attribute);
								iter.remove();
								if(results.size() >= features)
									break priorityLoop;
							}
							else if(attribute.priority == priority)
								list.add(attribute);
						}
						//Randomize out some stuff here
						
					}
				}
			}
		}
		
	}
	
}
