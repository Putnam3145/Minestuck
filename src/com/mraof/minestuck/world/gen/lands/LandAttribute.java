package com.mraof.minestuck.world.gen.lands;

public class LandAttribute
{
	
	public static enum EnumAttribute
	{
		/**
		 * Value is an instance of "BlockWithMetadata".
		 */
		SURFACE_BLOCK(false),
		/**
		 * Value is an instance of "BlockWithMetadata".
		 */
		GROUND_BLOCK(false),
		/**
		 * Value is an integer between 0 (inclusive) to 3 (exclusive).
		 */
		DAY_CYCLE(false),
		/**
		 * Value is an instance of "ILandDecorator"
		 */
		DECORATOR(true);
		
		protected boolean multiValued;
		
		private EnumAttribute(boolean multiValued)
		{
			this.multiValued = multiValued;
		}
		
	}
	
	public final EnumAttribute type;
	public final Object value;
	public final int priority;
	
	/**
	 * 
	 * @param attr The attribute type
	 * @param value The attribute value
	 * @param priority The importance of the attribute, ranged from 1 to 5 (inclusive)
	 */
	public LandAttribute(EnumAttribute attr, Object value, int priority)
	{
		this.type = attr;
		this.value = value;
		this.priority = Math.max(1, Math.min(5, priority));
	}
	
}
