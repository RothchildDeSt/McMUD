package com.planet_ink.coffee_mud.Abilities.Properties;

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
public class Prop_TicketTaker extends Property
{
	public String ID() { return "Prop_TicketTaker"; }
	public String name(){ return "Ticket Taker";}
	public String displayText() {return "";}
	protected int canAffectCode(){return Ability.CAN_MOBS|Ability.CAN_ITEMS;}

	public String accountForYourself()
	{
		return "one who acts as a ticket taker";
	}

	private int cost(){
		int amount=Util.s_int(text());
		if(amount==0) amount=10;
		return amount;
	}

	private boolean isMine(Environmental host, Rideable R)
	{
		if(host instanceof Rider)
		{
			Rider mob=(Rider)host;
			if(R==mob) return true;
			if(mob.riding()==null) return false;
			if(mob.riding()==R) return true;
			if((R instanceof Rider)&&(((Rider)R).riding()==mob.riding()))
				return true;
			if((mob.riding() instanceof Rider)&&(((Rider)mob.riding()).riding()==R))
				return true;
		}
		else
		if(host instanceof Rideable)
			return host==R;
		return false;
	}

	public void executeMsg(Environmental myHost, CMMsg msg)
	{
		super.executeMsg(myHost,msg);
		if(((myHost instanceof Rider)&&(((Rider)myHost).riding()!=null))
		   ||(myHost instanceof Rideable))
		{
			MOB mob=msg.source();
			if((msg.target()!=null)
			&&(myHost!=mob)
			&&(!mob.isMonster())
			&&(msg.target() instanceof Rideable)
			&&(isMine(myHost,(Rideable)msg.target())))
			{
				switch(msg.sourceMinor())
				{
				case CMMsg.TYP_MOUNT:
				case CMMsg.TYP_SIT:
				case CMMsg.TYP_ENTER:
				case CMMsg.TYP_SLEEP:
					if(mob.getMoney()>=cost())
					{
						mob.location().show(mob,myHost,CMMsg.MSG_NOISYMOVEMENT,"<S-NAME> give(s) "+cost()+" gold to <T-NAME>.");
						mob.setMoney(mob.getMoney()-cost());
					}
				}
			}
		}
	}
	public boolean okMessage(Environmental myHost, CMMsg msg)
	{
		if(!super.okMessage(myHost,msg)) return false;
		if(((myHost instanceof Rider)&&(((Rider)myHost).riding()!=null))
		   ||(myHost instanceof Rideable))
		{
			MOB mob=msg.source();
			if((msg.target()!=null)
			&&(myHost!=mob)
			&&(!mob.isMonster())
			&&(msg.target() instanceof Rideable)
			&&(isMine(myHost,(Rideable)msg.target())))
				switch(msg.sourceMinor())
				{
				case CMMsg.TYP_MOUNT:
				case CMMsg.TYP_SIT:
				case CMMsg.TYP_ENTER:
				case CMMsg.TYP_SLEEP:
					if(mob.getMoney()<cost())
					{
						if(myHost instanceof MOB)
							CommonMsgs.say((MOB)myHost,mob,"You'll need "+cost()+" gold to board.",false,false);
						else
							mob.tell("You'll need "+cost()+" gold to board.");
						return false;
					}
					break;
				default:
					break;
				}
		}
		return true;
	}
}
