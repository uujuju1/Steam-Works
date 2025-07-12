package sw.world.blocks.production;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.io.*;
import mindustry.type.*;
import mindustry.ui.*;

public class SingleItemMultiCrafter extends SWGenericCrafter {
	public SingleItemMultiCrafter(String name) {
		super(name);
		
		configurable = true;
		clearOnDoubleTap = true;
		
		config(Integer.class, (SingleItemMultiCrafterBuild build, Integer value) -> {
			build.currentItem = value;
		});
		configClear((SingleItemMultiCrafterBuild build) -> {
			build.currentItem = -1;
		});
	}
	
	public class SingleItemMultiCrafterBuild extends SWGenericCrafterBuild {
		public int currentItem = -1;
		
		@Override
		public void buildConfiguration(Table table) {
			for (int i = 0; i < outputItems.length; i++) {
				ItemStack stack = outputItems[i];
				
				if (i % 4 == 0) table.row();
				
				Button button = new Button(Styles.clearTogglet);
				button.image(stack.item.uiIcon);
				
				int finalI = i;
				button.clicked(() -> configure(finalI));
				button.update(() -> button.setChecked(finalI == currentItem));
				
				table.add(button);
			}
		}
		
		@Override public Integer config() {
			return currentItem;
		}
		
		@Override
		public void craft() {
			consume();
			
			for(int i = 0; i < outputItems[currentItem].amount; i++) offload(outputItems[currentItem].item);
			
			if(wasVisible) craftEffect.at(x, y);
			
			progress %= 1f;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			
			currentItem = read.i();
		}
		
		@Override public boolean shouldConsume() {
			return super.shouldConsume() && currentItem != -1;
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			
			write.i(currentItem);
		}
	}
}
