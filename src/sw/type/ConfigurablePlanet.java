package sw.type;

import mindustry.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;

public class ConfigurablePlanet extends Planet {
	public ConfigurablePlanet(String name, Planet parent, float radius, int sectorSize, Schematic loadout) {
		super(name, parent, radius, sectorSize);

		generator = new BlankPlanetGenerator() {
			{
				defaultLoadout = loadout;
			}
		};
	}

	@Override
	public void applyRules(Rules rules, boolean customGame) {
		super.applyRules(rules, customGame);

		if (Vars.state.rules.sector != null && Vars.state.rules.sector.preset != null) {
			Vars.state.rules.sector.preset.rules.get(Vars.state.rules);
		}
	}
}
