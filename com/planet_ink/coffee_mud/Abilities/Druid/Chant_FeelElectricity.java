package com.planet_ink.coffee_mud.Abilities.Druid;
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

public class Chant_FeelElectricity extends Chant
{
	public String ID() { return "Chant_FeelElectricity"; }
	public String name(){ return "Feel Electricity";}
	public String displayText(){return "(Feel Electricity)";}
	public int quality(){return Ability.MALICIOUS;}
	protected int canAffectCode(){return CAN_MOBS;}
	protected int canTargetCode(){return CAN_MOBS;}

	public void affectCharStats(MOB affectedMOB, CharStats affectedStats)
	{
		super.affectCharStats(affectedMOB,affectedStats);
		affectedStats.setStat(CharStats.SAVE_ELECTRIC,affectedStats.getStat(CharStats.SAVE_ELECTRIC)-100);
	}

	public boolean tick(Tickable ticking, int tickID)
	{
		if(!super.tick(ticking,tickID)) return false;
		if(tickID!=MudHost.TICK_MOB) return false;
		if((affecting()!=null)&&(affecting() instanceof MOB))
		{
			MOB dummy=(MOB)affecting();
			Room room=dummy.location();
			if((room!=null)
			&&(room.getArea().getClimateObj().weatherType(room)==Climate.WEATHER_THUNDERSTORM)
			&&(Dice.rollPercentage()>dummy.charStats().getSave(CharStats.SAVE_ELECTRIC)))
			{
				int damage=Dice.roll(1,5,0);
				MUDFight.postDamage(invoker,dummy,null,damage,CMMsg.MASK_GENERAL|CMMsg.TYP_ELECTRIC,Weapon.TYPE_STRIKING,"The electricity in the air <DAMAGE> <T-NAME>!");
			}
		}
		return true;
	}

	public void unInvoke()
	{
		// undo the affects of this spell
		if((affected==null)||(!(affected instanceof MOB)))
			return;
		MOB mob=(MOB)affected;
		if(canBeUninvoked())
			mob.tell("Your charged feeling is gone.");

		super.unInvoke();

	}

	public boolean okMessage(Environmental myHost, CMMsg msg)
	{
		if(!super.okMessage(myHost,msg))
			return false;

		if((affected==null)||(!(affected instanceof MOB)))
			return true;

		MOB mob=(MOB)affected;
		if((msg.amITarget(mob))&&(msg.targetMinor()==CMMsg.TYP_DAMAGE)
		   &&(msg.sourceMinor()==CMMsg.TYP_ELECTRIC))
		{
			int recovery=(int)Math.round(Util.mul((msg.value()),2.0));
			msg.setValue(msg.value()+recovery);
		}
		return true;
	}


	public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel)
	{
		MOB target=this.getTarget(mob,commands,givenTarget);
		if(target==null) return false;

		// the invoke method for spells receives as
		// parameters the invoker, and the REMAINING
		// command line parameters, divided into words,
		// and added as String objects to a vector.
		if(!super.invoke(mob,commands,givenTarget,auto,asLevel))
			return false;
		boolean success=profficiencyCheck(mob,0,auto);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.
			invoker=mob;
			FullMsg msg=new FullMsg(mob,target,this,affectType(auto),auto?"":"^S<S-NAME> chant(s) to <T-NAMESELF>.^?");
			if(mob.location().okMessage(mob,msg))
			{
				mob.location().send(mob,msg);
				if(msg.value()<=0)
				{
					mob.location().show(target,null,CMMsg.MSG_OK_VISUAL,"<S-NAME> feel(s) very charged!");
					maliciousAffect(mob,target,asLevel,0,-1);
				}
			}
		}
		else
			return maliciousFizzle(mob,target,"<S-NAME> chant(s) to <T-NAMESELF>, but the magic fades.");
		// return whether it worked
		return success;
	}
}
