package com.planet_ink.coffee_mud.Races;

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
public class Toadstool extends StdRace
{
	public String ID(){	return "Toadstool"; }
	public String name(){ return "Toadstool"; }
	public int shortestMale(){return 1;}
	public int shortestFemale(){return 1;}
	public int heightVariance(){return 1;}
	public int lightestWeight(){return 1;}
	public int weightVariance(){return 1;}
	public long forbiddenWornBits(){return Integer.MAX_VALUE;}
	public String racialCategory(){return "Vegetation";}
	public int availabilityCode(){return 0;}

	//                                an ey ea he ne ar ha to le fo no gi mo wa ta wi
	private static final int[] parts={0 ,0 ,0 ,0 ,0 ,0 ,0 ,1 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 };
	public int[] bodyMask(){return parts;}

	private int[] agingChart={0,0,0,0,0,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE};
	public int[] getAgingChart(){return agingChart;}
	
	protected static Vector resources=new Vector();
	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		affectableStats.setWeight(1);
		affectableStats.setHeight(1);
		affectableStats.setAttackAdjustment(0);
		affectableStats.setArmor(0);
		affectableStats.setSensesMask(affectableStats.sensesMask()
			|EnvStats.CAN_NOT_MOVE|EnvStats.CAN_NOT_SPEAK|EnvStats.CAN_NOT_TASTE);
		affectableStats.setDamage(0);
		affectableStats.setDisposition(affectableStats.disposition()|EnvStats.IS_GOLEM);
	}
	public void affectCharStats(MOB affectedMOB, CharStats affectableStats)
	{
		super.affectCharStats(affectedMOB, affectableStats);
		affectableStats.setPermaStat(CharStats.STRENGTH,1);
		affectableStats.setPermaStat(CharStats.DEXTERITY,1);
		affectableStats.setPermaStat(CharStats.INTELLIGENCE,1);
	}
	public Weapon myNaturalWeapon()
	{
		if(naturalWeapon==null)
		{
			naturalWeapon=CMClass.getWeapon("StdWeapon");
			naturalWeapon.setName("the toadstool shuffle");
			naturalWeapon.setWeaponType(Weapon.TYPE_NATURAL);
		}
		return naturalWeapon;
	}

	public String healthText(MOB mob)
	{
		double pct=(Util.div(mob.curState().getHitPoints(),mob.maxState().getHitPoints()));

		if(pct<.10)
			return "^r" + mob.name() + "^r is almost squashed!^N";
		else
		if(pct<.25)
			return "^y" + mob.name() + "^y is severely gashed and bruised.^N";
		else
		if(pct<.40)
			return "^p" + mob.name() + "^p has lots of gashes and bruises.^N";
		else
		if(pct<.55)
			return "^p" + mob.name() + "^p has some serious bruises.^N";
		else
		if(pct<.70)
			return "^g" + mob.name() + "^g has some bruises.^N";
		else
		if(pct<.85)
			return "^g" + mob.name() + "^g has a few small bruises.^N";
		else
		if(pct<.95)
			return "^g" + mob.name() + "^g is barely bruised.^N";
		else
			return "^c" + mob.name() + "^c is in perfect condition^N";
	}
	public Vector myResources()
	{
		synchronized(resources)
		{
			if(resources.size()==0)
			{
				resources.addElement(makeResource
				("a "+name().toLowerCase()+" voice box",EnvResource.RESOURCE_MEAT));
				resources.addElement(makeResource
				("some "+name().toLowerCase()+" feathers",EnvResource.RESOURCE_FEATHERS));
				resources.addElement(makeResource
				("some "+name().toLowerCase()+" meat",EnvResource.RESOURCE_POULTRY));
				resources.addElement(makeResource
				("some "+name().toLowerCase()+" blood",EnvResource.RESOURCE_BLOOD));
				resources.addElement(makeResource
				("a pile of "+name().toLowerCase()+" bones",EnvResource.RESOURCE_BONE));
			}
		}
		return resources;
	}
}
