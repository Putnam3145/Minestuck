package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

import com.mraof.minestuck.Minestuck;

public class LandAspectShade extends LandAspect 
{

	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Minestuck.coloredDirt, (byte) 0)};
	BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.stone)};
	static Vec3 skyColor = Vec3.createVectorHelper(0.16D, 0.38D, 0.54D);
	
	public LandAspectShade()
	{
		attributes.add(new LandAttribute(LandAttribute.EnumAttribute.SURFACE_BLOCK, new BlockWithMetadata(Minestuck.coloredDirt, (byte) 0), 4));
		attributes.add(new LandAttribute(LandAttribute.EnumAttribute.DAY_CYCLE, 2, 5));
	}
	
	@Override
	public BlockWithMetadata[] getSurfaceBlocks() {
		return surfaceBlocks;
	}
	
	@Override
	public BlockWithMetadata[] getUpperBlocks() {
		return upperBlocks;
	}
	
	@Override
	public Block getOceanBlock() 
	{
		return Minestuck.blockOil;
	}

	@Override
	public double[] generateTerrainMap() {
		return null;
	}

	@Override
	public float getRarity() {
		return 0.5F;
	}

	@Override
	public String getPrimaryName() {
		return "Shade";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Shade"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.brown_mushroom_block, 10, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
	}
	
	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public boolean canBeCombinedWith(LandAspect otherAspect)
	{
		if(super.canBeCombinedWith(otherAspect))
		{
			for(LandAttribute attribute : otherAspect.attributes)
				if(attribute.type == LandAttribute.EnumAttribute.DAY_CYCLE)
					return !(((Integer)attribute.value) == 1 && attribute.priority >= 3);
			return true;
		}
		else return false;
	}
	
}
