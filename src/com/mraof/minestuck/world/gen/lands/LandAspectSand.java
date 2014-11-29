
package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectSand extends LandAspect
{
	BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.sandstone)};
	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Blocks.sand)};
	static Vec3 skyColor = Vec3.createVectorHelper(0.99D, 0.8D, 0.05D);
	
	public LandAspectSand()
	{
		
		attributes.add(new LandAttribute(LandAttribute.EnumAttribute.DAY_CYCLE, 0, 1));
	}
	
	@Override
	public BlockWithMetadata[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}

	@Override
	public BlockWithMetadata[] getUpperBlocks() {
		return upperBlocks;
	}
	
	@Override
	public Block getOceanBlock() 
	{
		return Blocks.sand;
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
		return "Sand";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Sand"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.stonebrick, 10, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
	}
	
	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
	
}
