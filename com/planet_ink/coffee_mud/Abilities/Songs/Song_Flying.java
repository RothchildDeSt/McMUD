package com.planet_ink.coffee_mud.Abilities.Songs;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;


public class Song_Flying extends Song
{
	public String ID() { return "Song_Flying"; }
	public String name(){ return "Flying";}
	public String displayText(){ return "(Song of Flying)";}
	public int quality(){ return BENEFICIAL_OTHERS;}
	public Environmental newInstance(){	return new Song_Flying();}
	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		if(invoker==null) return;

		affectableStats.setDisposition(affectableStats.disposition()|EnvStats.IS_FLYING);
	}
}
