package com.sloverse.extension.zone.util.math;

public class Vec2  extends Vec
{
	public float x;
	public float y;
	
	public Vec2()
	{
		this(0, 0);
	}
	
	public Vec2(Vec2 another)
	{
		this(another.x, another.y);
	}
	
	public Vec2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public void set(Vec2 another)
	{
		this.x = another.x;
		this.y = another.y;
	}
	
	@Override
	public float getMagnitude() 
	{
		return (float) Math.sqrt(getMagnitudeSquared());
	}
	
	@Override
	public float getMagnitudeSquared()
	{
		return x * x + y * y;
	}
	
	public Vec2 normalize()
	{
		float magnitude = getMagnitude();
		this.x /= magnitude;
		this.y /= magnitude;
		return this;
	}
	
	public Vec2 scale(float scaleFactor)
	{
		this.x *= scaleFactor;
		this.y *= scaleFactor;
		return this;
	}
	
	public Vec2 add(Vec2 vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}
	
	public static Vec2 normalize(Vec2 vec)
	{
		float mag = vec.getMagnitude();
		return new Vec2(vec.x / mag, vec.y / mag);
	}
	
	public static Vec2 scale(Vec2 vec, float scaleFactor)
	{
		return new Vec2(vec.x * scaleFactor, vec.y * scaleFactor);
	}
	
	public static Vec2 add(Vec2 first, Vec2 second)
	{
		return new Vec2(first.x + second.x, first.y + second.y);
	}
	
	public static Vec2 subtract(Vec2 first, Vec2 second)
	{
		return new Vec2(first.x - second.x, first.y - second.y);
	}
	
	public static float distanceBetween(Vec2 first, Vec2 second) 
	{
		return (float) Math.sqrt((second.x - first.x) * (second.x - first.x) + (second.y - first.y) * (second.y - first.y));
	}
}
