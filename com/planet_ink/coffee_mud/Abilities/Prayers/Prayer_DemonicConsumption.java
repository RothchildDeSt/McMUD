package com.planet_ink.coffee_mud.Abilities.Prayers;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

/* 
   Copyright 2000-2005 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class Prayer_DemonicConsumption extends Prayer
{
	public String ID() { return "Prayer_DemonicConsumption"; }
	public String name(){return "Demonic Consumption";}
	public int quality(){return MALICIOUS;};
	protected int canTargetCode(){return CAN_ITEMS|CAN_MOBS;}
	public long flags(){return Ability.FLAG_UNHOLY;}

	public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel)
	{
		Environmental target=getAnyTarget(mob,commands,givenTarget,Item.WORN_REQ_ANY);
		if(target==null) return false;

		if(!super.invoke(mob,commands,givenTarget,auto,asLevel))
			return false;


		boolean success=false;
		int affectType=CMMsg.MSG_CAST_VERBAL_SPELL;
		if(!(target instanceof Item))
		{
			if(!auto)
				affectType=affectType|CMMsg.MASK_MALICIOUS;
		}
		int levelDiff=target.envStats().level()-mob.envStats().level();
		if(levelDiff<0) levelDiff=0;
		success=profficiencyCheck(mob,-(levelDiff*20),auto);

		if(auto)affectType=affectType|CMMsg.MASK_GENERAL;

		if(success)
		{
			FullMsg msg=new FullMsg(mob,target,this,affectType,auto?"":"^S<S-NAME> point(s) at <T-NAMESELF> and "+prayWord(mob)+" treacherously!^?");
			if(mob.location().okMessage(mob,msg))
			{
				mob.location().send(mob,msg);
				if(msg.value()<=0)
				{
					Hashtable V=new Hashtable();
					for(int i=0;i<mob.location().numItems();i++)
					{
						Item item=mob.location().fetchItem(i);
						if((item!=null)&&(item instanceof DeadBody))
							V.put(item,item);
					}

					if(target instanceof MOB)
					{
						if(((MOB)target).curState().getHitPoints()>0)
							MUDFight.postDamage(mob,(MOB)target,this,(((MOB)target).curState().getHitPoints()*10),CMMsg.MASK_GENERAL|CMMsg.TYP_CAST_SPELL,Weapon.TYPE_BURSTING,"^SThe evil <DAMAGE> <T-NAME>!^?");
						if(((MOB)target).amDead())
							mob.location().show(mob,target,CMMsg.MSG_OK_ACTION,"<T-NAME> <T-IS-ARE> consumed!");
						else
							return false;
					}
					else
						mob.location().show(mob,target,CMMsg.MSG_OK_ACTION,"<T-NAME> is consumed!");

					if(target instanceof Item)
						((Item)target).destroy();
					else
					{
						int i=0;
						while(i<mob.location().numItems())
						{
							int s=mob.location().numItems();
							Item item=mob.location().fetchItem(i);
							if((item!=null)&&(item instanceof DeadBody)&&(V.get(item)==null))
								item.destroy();
							if(s==mob.location().numItems())
								i++;
						}
					}
					mob.location().recoverRoomStats();
				}

			}

		}
		else
			maliciousFizzle(mob,target,"<S-NAME> point(s) at <T-NAMESELF> and "+prayWord(mob)+" treacherously, but fizzles the magic!");


		// return whether it worked
		return success;
	}
}
