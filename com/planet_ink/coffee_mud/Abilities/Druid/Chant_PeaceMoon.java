package com.planet_ink.coffee_mud.Abilities.Druid;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;


public class Chant_PeaceMoon extends Chant
{
	public String ID() { return "Chant_PeaceMoon"; }
	public String name(){ return "Peace Moon";}
	public String displayText(){return "(Peace Moon)";}
	public int quality(){return Ability.INDIFFERENT;}
	protected int canAffectCode(){return CAN_MOBS|CAN_ROOMS;}
	protected int canTargetCode(){return 0;}
	public Environmental newInstance(){	return new Chant_PeaceMoon();}

	public void unInvoke()
	{
		// undo the affects of this spell
		if((affected==null)||(!(affected instanceof MOB)))
			return;
		MOB mob=(MOB)affected;
		if(canBeUninvoked())
			mob.tell("You are no longer under the peace moon.");

		super.unInvoke();

	}

	public boolean okAffect(Environmental myHost, Affect affect)
	{
		if(affected instanceof Room)
		if((Util.bset(affect.sourceCode(),affect.MASK_MALICIOUS))
		||(Util.bset(affect.targetCode(),affect.MASK_MALICIOUS))
		||(Util.bset(affect.othersCode(),affect.MASK_MALICIOUS)))
		{
			if((affect.source()!=null)
			   &&(affect.target()!=null)
			   &&(affect.source()!=affect.target()))
			{
				affect.source().tell("Nah, you feel too peaceful here.");
				if(affect.source().getVictim()!=null)
					affect.source().getVictim().makePeace();
				affect.source().makePeace();
			}
			affect.modify(affect.source(),affect.target(),affect.tool(),Affect.NO_EFFECT,"",Affect.NO_EFFECT,"",Affect.NO_EFFECT,"");
			return false;
		}
		return super.okAffect(myHost,affect);
	}

	public boolean tick(Tickable ticking, int tickID)
	{
		if(!super.tick(ticking,tickID)) return false;
		if(affected==null) return false;
		if(affected instanceof MOB)
		{
			MOB mob=(MOB)affected;
			if(mob.location().fetchAffect(ID())==null)
				unInvoke();
			if(mob.isInCombat()) mob.makePeace();
		}
		else
		if(affected instanceof Room)
		{
			Room room=(Room)affected;
			for(int i=0;i<room.numInhabitants();i++)
			{
				MOB M=room.fetchInhabitant(i);
				if((M!=null)&&(M.fetchAffect(ID())==null))
				{
					Ability A=(Ability)copyOf();
					M.addAffect(A);
				}
			}
		}
		return true;
	}

	public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto)
	{
		Room target=mob.location();
		if(target==null) return false;
		if((target.domainType()&Room.INDOORS)>0)
		{
			mob.tell("You cannot summon the peace moon here.");
			return false;
		}
		if(target.fetchAffect(ID())!=null)
		{
			mob.tell("This place is already under the peace moon.");
			return false;
		}

		// the invoke method for spells receives as
		// parameters the invoker, and the REMAINING
		// command line parameters, divided into words,
		// and added as String objects to a vector.
		if(!super.invoke(mob,commands,givenTarget,auto))
			return false;
		boolean success=profficiencyCheck(0,auto);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.
			invoker=mob;
			FullMsg msg=new FullMsg(mob,target,this,affectType(auto),auto?"":"^S<S-NAME> chant(s) to the sky.^?");
			if(mob.location().okAffect(mob,msg))
			{
				mob.location().send(mob,msg);
				if(!msg.wasModified())
				{
					mob.location().showHappens(Affect.MSG_OK_VISUAL,"The Peace Moon Rises!");
					beneficialAffect(mob,target,0);
				}
			}
		}
		else
			return maliciousFizzle(mob,target,"<S-NAME> chant(s) to the sky, but the magic fades.");
		// return whether it worked
		return success;
	}
}
