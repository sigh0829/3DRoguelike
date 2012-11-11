package com.lyeeedar.Roguelike3D.Game;

import com.badlogic.gdx.math.Vector3;

public class CollisionBox {
	public Vector3 position;
	public Vector3 dimensions;
	
	public CollisionBox(Vector3 dimensions)
	{
		position = new Vector3();
		this.dimensions = dimensions;
	}
	
	public CollisionBox(Vector3 position, Vector3 dimensions)
	{
		this.position = position;
		this.dimensions = dimensions;
	}
	
	public void translate(float x, float y, float z)
	{
		translate(new Vector3(x, y, z));
	}
	
	public void translate(Vector3 movement)
	{
		position.add(movement);
	}
	
	public boolean intersect(CollisionBox box)
	{
		if (
			position.x < box.position.x+box.dimensions.x &&
			position.y < box.position.y+box.dimensions.y &&
			position.z < box.position.z+box.dimensions.z &&
			position.x+dimensions.x > box.position.x &&
			position.y+dimensions.y > box.position.y &&
			position.z+dimensions.z > box.position.z
			) return true;
		else return false;
	}
	
	public CollisionBox cpy()
	{
		return new CollisionBox(position.cpy(), dimensions.cpy());
	}

}