package com.lyeeedar.Roguelike3D.Graphics.Screens;

import java.util.NavigableSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.lyeeedar.Roguelike3D.Roguelike3DGame;
import com.lyeeedar.Roguelike3D.Game.GameStats;
import com.lyeeedar.Roguelike3D.Game.GameStats.Equipment_Slot;
import com.lyeeedar.Roguelike3D.Game.Item.Equippable;
import com.lyeeedar.Roguelike3D.Game.Item.Item.Item_Type;

public class InventoryScreen extends UIScreen {

	public InventoryScreen(Roguelike3DGame game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	Table left;
	Table mid;
	Table right;
	
	public void createLeft()
	{
		left.clear();
		
		EquipButton head = new EquipButton(skin, Item_Type.ARMOUR_HEAD, Equipment_Slot.HEAD);
		EquipButton body = new EquipButton(skin, Item_Type.ARMOUR_BODY, Equipment_Slot.BODY);
		EquipButton legs = new EquipButton(skin, Item_Type.ARMOUR_LEGS, Equipment_Slot.LEGS);
		EquipButton boots = new EquipButton(skin, Item_Type.ARMOUR_BOOTS, Equipment_Slot.BOOTS);
		
		EquipButton l_hand = new EquipButton(skin, Item_Type.WEAPON, Equipment_Slot.L_HAND);
		EquipButton r_hand = new EquipButton(skin, Item_Type.WEAPON, Equipment_Slot.R_HAND);
		
		left.add(head);
		left.row();
		left.add(l_hand);
		left.add(body);
		left.add(r_hand);
		left.row();
		left.add(legs);
		left.row();
		left.add(boots);
		left.row();
	}
	
	Equippable selected = null;
	EquipButton equipped = null;
	
	public void createMid()
	{
		mid.clear();
		
		Table label = null;
		TextButton button = null;
		
		if (equipped == null) return;
		else if (selected == null)
		{
			if (equipped.item == null) return;
			
			label = equipped.item.getDescriptionWidget(skin);
		}
		else
		{
			if (equipped.item == null)
			{
				label = selected.getDescriptionWidget(skin);
			}
			else
			{
				label = equipped.item.getComparisonWidget(selected, skin);
				button = new TextButton("Equip", skin);
				button.addListener(new InputListener() {
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
						equipped.item = selected;
						
						createLeft();
						createMid();
						createRight(equipped.type);
						
						return false;
					}
				});
			}
		}
		
		mid.add(label);
		mid.row();
		if (button != null)
		{
			mid.add(button);
		}
	}
	
	public void createRight(Item_Type type)
	{
		right.clear();
		
		Table itemList = new Table();
		NavigableSet<Equippable> items = GameStats.equipment.get(type);
		ButtonGroup bg = new ButtonGroup();
		
		for (Equippable e : items)
		{
			if (equipped != null && equipped.item != null && e.UID.equals(equipped.item.UID)) continue;
			itemList.add(new EquippableButton(skin, e, bg));
		}
		
		ScrollPane scroll = new ScrollPane(itemList, skin);
		
		right.add(scroll);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createSuper() {
		Table table = new Table();
		left = new Table();
		mid = new Table();
		right = new Table();
		
		table.add(left);
		table.add(mid);
		table.add(right);
		
		stage.addActor(table);
		table.setFillParent(true);

	}

	@Override
	protected void superSuperDispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	class EquipButton extends Button
	{
		final Item_Type type;
		final Equipment_Slot slot;
		
		Equippable item;
		
		public EquipButton(Skin skin, final Item_Type type, Equipment_Slot slot) {
			super(skin);
			this.type = type;
			this.slot = slot;
			
			if (slot == Equipment_Slot.HEAD) item = GameStats.head;
			else if (slot == Equipment_Slot.BODY) item = GameStats.body;
			else if (slot == Equipment_Slot.LEGS) item = GameStats.legs;
			else if (slot == Equipment_Slot.BOOTS) item = GameStats.boots;
			else if (slot == Equipment_Slot.L_HAND) item = GameStats.l_hand;
			else if (slot == Equipment_Slot.R_HAND) item = GameStats.r_hand;
			
			final EquipButton parent = this;
			
			addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					equipped = parent;
					
					createMid();
					createRight(type);
					
					return true;
				}
			});
		}
	}
	
	class EquippableButton extends Button
	{
		final Equippable equipment;
		CheckBox cbox;
		
		boolean clicked = false;
		public EquippableButton(Skin skin, Equippable e, ButtonGroup bg) {
			super(skin);
			this.equipment = e;
			
			cbox = new CheckBox(e.name, skin);
			
			bg.add(cbox);
			
			addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					clicked = true;
					return true;
				}
				
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					if (clicked) {
						clicked = false;
						cbox.toggle();
						selected = equipment;
						
						createMid();
					}
				}
				
				public void touchDragged (InputEvent event, float x, float y, int pointer) {
					clicked = false;
				}
			});
		}
		
	}
}
