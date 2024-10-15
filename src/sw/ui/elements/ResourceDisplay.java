package sw.ui.elements;

import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.core.*;
import mindustry.type.*;
import mindustry.ui.*;

/**
 * table that displays how many items are in the cores of a planet
 * @see mindustry.ui.ItemsDisplay this but with extra steps
 */
public class ResourceDisplay extends Table {
	public Planet currentPlanet;
	public ItemSeq items;

	public ResourceDisplay(Planet planet) {
		currentPlanet = planet;
		setBackground(Styles.black6);
		rebuild();
	}

	public void setCurrentPlanet(Planet planet) {
		currentPlanet = planet;
		rebuild();
	}

	public void rebuild() {
		clear();
		items = new ItemSeq(){
			//store sector item amounts for modifications
			ObjectMap<Sector, ItemSeq> cache = new ObjectMap<>();

			{
				for(Sector sector : currentPlanet.sectors){
					if(sector.hasBase()){
						ItemSeq cached = sector.items();
						cache.put(sector, cached);
						cached.each((item, amount) -> {
							values[item.id] += Math.max(amount, 0);
							total += Math.max(amount, 0);
						});
					}
				}
			}

			//this is the only method that actually modifies the sequence itself.
			@Override
			public void add(Item item, int amount){
				//only have custom removal logic for when the sequence gets items taken out of it (e.g. research)
				if(amount < 0){
					//remove items from each sector's storage, one by one

					//negate amount since it's being *removed* - this makes it positive
					amount = -amount;

					//% that gets removed from each sector
					double percentage = (double)amount / get(item);
					int[] counter = {amount};
					cache.each((sector, seq) -> {
						if(counter[0] == 0) return;

						//amount that will be removed
						int toRemove = Math.min((int)Math.ceil(percentage * seq.get(item)), counter[0]);

						//actually remove it from the sector
						sector.removeItem(item, toRemove);
						seq.remove(item, toRemove);

						counter[0] -= toRemove;
					});

					//negate again to display correct number
					amount = -amount;
				}

				super.add(item, amount);
			}
		};

		items.each((item, amount) -> {
			image(item.uiIcon).padRight(5);
			add(UI.formatAmount(amount)).row();
		});
		if (items.toSeq().isEmpty()) {
			setBackground(null);
		} else {
			setBackground(Styles.black6);
		}
		margin(10f);
	}
}
