package com.planet_ink.coffee_mud.Items.Weapons;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;


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
public class DaggerOfVenom extends Dagger
{
	public String ID(){	return "DaggerOfVenom";}
	public DaggerOfVenom()
	{
		super();

		setName("a small dagger");
		setDisplayText("a sharp little dagger lies here.");
		setDescription("It has a wooden handle and a metal blade.");
        secretIdentity="A Dagger of Venom (Periodically injects poison on a successful hit.)";
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(1);
		baseEnvStats.setWeight(1);
		baseGoldValue=1500;
		baseEnvStats().setAttackAdjustment(0);
		baseEnvStats().setDamage(4);
		material=EnvResource.RESOURCE_STEEL;
        baseEnvStats().setDisposition(baseEnvStats().disposition()|EnvStats.IS_BONUS);
		weaponType=Weapon.TYPE_PIERCING;
		weaponClassification=Weapon.CLASS_DAGGER;
		recoverEnvStats();
	}


	public void executeMsg(Environmental myHost, CMMsg msg)
	{
		super.executeMsg(myHost,msg);
		if((msg.source().location()!=null)
		   &&(msg.targetMinor()==CMMsg.TYP_DAMAGE)
		   &&((msg.value())>0)
		   &&(msg.tool()==this)
		   &&(msg.target() instanceof MOB))
		{
            int chance = ((int) Math.random() * 20);
            if(chance == 10)
            {
                Ability poison = CMClass.getAbility("Poison");
	            if(poison!=null) poison.invoke(msg.source(),msg.target(), true,envStats().level());
            }
		}
	}

}
