package com.lyeeedar.Roguelike3D.Game.Level.XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.lyeeedar.Roguelike3D.Game.GameData.Damage_Type;
import com.lyeeedar.Roguelike3D.Game.GameData.Element;
import com.lyeeedar.Roguelike3D.Game.Item.Equippable;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * Class Used to evolve monsters to attempt to provide variety.
 * 
 * Also couples as the monsters.data xml reader.
 * @author Philip
 *
 */
public class MonsterEvolver extends XMLReader {
	
	public static final String ATTRIBUTES = "attributes";
	public static final String MONSTERS = "monsters";
	public static final String DEPTH_MIN = "depth_min";
	public static final String DEPTH_MAX = "depth_max";
	public static final String MONSTER_TYPE = "monster_type";
	public static final String CREATURES = "creatures";
	
	public static final String VISUAL = "visual";	
	public static final String DESCRIPTION = "description";
	public static final String MODEL = "model";
	public static final String MODEL_TYPE = "type";
	public static final String MODEL_NAME = "name";
	public static final String MODEL_SCALE = "scale";	
	public static final String TEXTURE = "texture";	
	public static final String COLOUR = "colour";
	public static final String RED = "red";
	public static final String GREEN = "green";
	public static final String BLUE = "blue";
	
	public static final String STATS = "stats";
	public static final String BASE_CALORIES = "base_calories";
	public static final String WEIGHT = "weight";
	public static final String HEALTH = "health";
	public static final String ELE_DEFENSES = "ele_defenses";
	public static final String FIRE = "FIRE";
	public static final String WATER = "WATER";
	public static final String AIR = "AIR";
	public static final String WOOD = "WOOD";
	public static final String METAL = "METAL";
	public static final String AETHER = "AETHER";
	public static final String VOID = "VOID";
	public static final String DEFENSES = "defenses";
	public static final String PIERCE = "PIERCE";
	public static final String IMPACT = "IMPACT";
	public static final String TOUCH = "TOUCH";
	public static final String STRENGTH = "strength";
	public static final String IQ = "iq";
	public static final String ATTACK_SPEED = "attack_speed";
	public static final String CAST_SPEED = "cast_speed";
	
	final Random ran = new Random();
	
	final String monster;
	final Node root;
	
	final HashMap<String, AbstractCreature_Evolver> creatures = new HashMap<String, AbstractCreature_Evolver>();

	public MonsterEvolver(String monster_type, int depth) {
		
		super("data/xml/monsters.data");
		
		SortedMap<Integer, ArrayList<Node>> valid = new TreeMap<Integer, ArrayList<Node>>();
		
		Node monsters = getNode(MONSTERS, root_node.getChildNodes());
		
		for (int i = 0; i < monsters.getChildNodes().getLength(); i++)
		{
			Node n = monsters.getFirstChild().getChildNodes().item(i);
			if (n.getNodeType() == Node.TEXT_NODE) continue;
			
			String mon_type = getNodeValue(MONSTER_TYPE, n.getChildNodes());
			
			if (mon_type.equalsIgnoreCase(monster_type))
			{
				int d_min = Integer.parseInt(getNodeValue(getNode(DEPTH_MIN, n.getChildNodes())));
				int d_max = Integer.parseInt(getNodeValue(getNode(DEPTH_MAX, n.getChildNodes())));
				
				if (depth < d_min)
				{
					if (valid.containsKey(0)) valid.get(d_min-depth).add(n);
					else
					{
						ArrayList<Node> nodes = new ArrayList<Node>();
						nodes.add(n);
						valid.put(d_min-depth, nodes);
					}
				}
				else if (depth > d_max)
				{
					if (valid.containsKey(0)) valid.get(depth-d_max).add(n);
					else
					{
						ArrayList<Node> nodes = new ArrayList<Node>();
						nodes.add(n);
						valid.put(depth-d_max, nodes);
					}
				}
				else
				{
					if (valid.containsKey(0)) valid.get(0).add(n);
					else
					{
						ArrayList<Node> nodes = new ArrayList<Node>();
						nodes.add(n);
						valid.put(0, nodes);
					}
				}
			}
		}
		
		if (valid.size() == 0)
		{
			root = null;
			monster = null;
			return;
		}
		
		ArrayList<Node> ns = valid.get(valid.firstKey());
		
		root = ns.get(ran.nextInt(ns.size()));
		monster = root.getNodeName();
		
		Node creatures = getNode(CREATURES, root.getChildNodes());
		
		for (int i = 0; i < creatures.getChildNodes().getLength(); i++)
		{
			Node n = creatures.getChildNodes().item(i);
			
			if (n.getNodeType() == Node.TEXT_NODE) continue;
			
			AbstractCreature_Evolver ac_e = new AbstractCreature_Evolver(n.getNodeName());
			
			// Add visual
			
			Node visual = getNode(VISUAL, n.getChildNodes());
			String description = getNodeValue(DESCRIPTION, visual.getChildNodes());
			
			Node model = getNode(MODEL, visual.getChildNodes());
			String model_type = getNodeValue(MODEL_TYPE, model.getChildNodes());
			String model_name = getNodeValue(MODEL_NAME, model.getChildNodes());
			String model_scale = getNodeValue(MODEL_SCALE, model.getChildNodes());
			
			String texture = getNodeValue(TEXTURE, visual.getChildNodes());
			
			Node colour = getNode(COLOUR, visual.getChildNodes());
			String red = getNodeValue(RED, colour.getChildNodes());
			String green = getNodeValue(GREEN, colour.getChildNodes());
			String blue = getNodeValue(BLUE, colour.getChildNodes());
			
			ac_e.addVisual(description, model_type, model_name, model_scale, texture, red, green, blue);
			
			// Add stats
			
			Node stats = getNode(STATS, n.getChildNodes());
			
			String base_calories = getNodeValue(BASE_CALORIES, stats.getChildNodes());
			String weight = getNodeValue(WEIGHT, stats.getChildNodes());
			String health = getNodeValue(HEALTH, stats.getChildNodes());
			String strength = getNodeValue(STRENGTH, stats.getChildNodes());
			String iq = getNodeValue(IQ, stats.getChildNodes());
			String att_speed = getNodeValue(ATTACK_SPEED, stats.getChildNodes());
			String cast_speed = getNodeValue(CAST_SPEED, stats.getChildNodes());
			
			ac_e.addStats(base_calories, weight, health, strength, iq, att_speed, cast_speed);
			
			// Add Elemental Defenses
			
			Node eleDef = getNode(ELE_DEFENSES, stats.getChildNodes());
			
			String f = getNodeValue(FIRE, eleDef.getChildNodes());
			String wa = getNodeValue(WATER, eleDef.getChildNodes());
			String ai = getNodeValue(AIR, eleDef.getChildNodes());
			String wo = getNodeValue(WOOD, eleDef.getChildNodes());
			String m = getNodeValue(METAL, eleDef.getChildNodes());
			String ae = getNodeValue(AETHER, eleDef.getChildNodes());
			String v = getNodeValue(VOID, eleDef.getChildNodes());
			
			ac_e.addEleDef(f, wa, ai, wo, m, ae, v);
			
			// Add Damage Defenses
			
			Node damDef = getNode(DEFENSES, stats.getChildNodes());
			
			String pierce = getNodeValue(PIERCE, damDef.getChildNodes());
			String impact = getNodeValue(IMPACT, damDef.getChildNodes());
			String touch = getNodeValue(TOUCH, damDef.getChildNodes());
			
			ac_e.addDamDef(pierce, impact, touch);
			
			this.creatures.put(ac_e.name, ac_e);
		}
	}
}

class EvolverTile
{
	public boolean food = false;
	
	public AbstractCreature_Evolver creature = null;
}

class AbstractCreature_Evolver
{
	public final String name;
	
	public AbstractCreature_Evolver(String name)
	{
		this.name = name;
	}
	
	String description; String model_type; String model_name; float model_scale; String texture; Color colour;
	public void addVisual(String description, String model_type, String model_name, String model_scale, String texture, String r, String g, String b)
	{
		this.description = description;
		this.model_type = model_type;
		this.model_name = model_name;
		this.model_scale = Float.parseFloat(model_scale);
		this.texture = texture;
		this.colour = new Color(Float.parseFloat(r), Float.parseFloat(g), Float.parseFloat(b), 1.0f);
	}
	
	int base_calories; int weight; int health; int strength; int IQ; int att_speed; int cast_speed;
	public void addStats(String base_calories, String weight, String health, String strength, String IQ, String att_speed, String cast_speed)
	{
		this.base_calories = Integer.parseInt(base_calories);
		this.weight = Integer.parseInt(weight);
		this.health = Integer.parseInt(health);
		this.strength = Integer.parseInt(strength);
		this.IQ = Integer.parseInt(IQ);
		this.att_speed = Integer.parseInt(att_speed);
		this.cast_speed = Integer.parseInt(cast_speed);
	}
	
	HashMap<Element, Integer> eleDef;
	public void addEleDef(String fire, String water, String air, String wood, String metal, String aether, String VOID)
	{
		eleDef = new HashMap<Element, Integer>();
		
		eleDef.put(Element.FIRE, Integer.parseInt(fire));
		eleDef.put(Element.WATER, Integer.parseInt(water));
		eleDef.put(Element.AIR, Integer.parseInt(air));
		eleDef.put(Element.WOOD, Integer.parseInt(wood));
		eleDef.put(Element.METAL, Integer.parseInt(metal));
		eleDef.put(Element.AETHER, Integer.parseInt(aether));
		eleDef.put(Element.VOID, Integer.parseInt(VOID));
	}
	
	HashMap<Damage_Type, Integer> damDef;
	public void addDamDef(String pierce, String impact, String touch)
	{
		damDef = new HashMap<Damage_Type, Integer>();
		
		damDef.put(Damage_Type.PIERCE, Integer.parseInt(pierce));
		damDef.put(Damage_Type.IMPACT, Integer.parseInt(impact));
		damDef.put(Damage_Type.TOUCH, Integer.parseInt(touch));
		
	}
}







