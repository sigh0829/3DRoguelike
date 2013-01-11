/*******************************************************************************
 * Copyright (c) 2012 Philip Collin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Philip Collin - initial API and implementation
 ******************************************************************************/
package com.lyeeedar.Roguelike3D.Game.Actor;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.lyeeedar.Roguelike3D.Game.GameData;
import com.lyeeedar.Roguelike3D.Game.GameObject;
import com.lyeeedar.Roguelike3D.Game.GameData.Damage_Type;
import com.lyeeedar.Roguelike3D.Game.GameData.Element;
import com.lyeeedar.Roguelike3D.Game.Item.MeleeWeapon;
import com.lyeeedar.Roguelike3D.Game.Item.MeleeWeapon.Weapon_Style;
import com.lyeeedar.Roguelike3D.Graphics.ParticleEffects.MotionTrail;

public class Player extends GameActor {

	public Player(String model, Color colour, String texture, float x, float y, float z, float scale)
	{
		super(model, colour, texture, x, y, z, scale);

		WEIGHT = 1;
		
		HashMap<Damage_Type, Integer> DAM_DEF = new HashMap<Damage_Type, Integer>();
		
		DAM_DEF.put(Damage_Type.PIERCE, 0);
		DAM_DEF.put(Damage_Type.IMPACT, 0);
		DAM_DEF.put(Damage_Type.TOUCH, 0);

		HashMap<Element, Integer> ELE_DEF = new HashMap<Element, Integer>();
		
		ELE_DEF.put(Element.FIRE, 0);
		ELE_DEF.put(Element.AIR, 0);
		ELE_DEF.put(Element.WATER, 0);
		ELE_DEF.put(Element.WOOD, 0);
		ELE_DEF.put(Element.METAL, 0);
		ELE_DEF.put(Element.AETHER, 0);
		ELE_DEF.put(Element.VOID, 0);
		
		R_HAND = new MeleeWeapon(this, Weapon_Style.SWING, 2, 10, ELE_DEF, DAM_DEF, 100);
		L_HAND = new MeleeWeapon(this, Weapon_Style.SWING, 1, 10, ELE_DEF, DAM_DEF, 100);
		
		ai = new AI_Player_Controlled(this);

	}
}
