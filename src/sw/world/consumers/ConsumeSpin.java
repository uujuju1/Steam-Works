package sw.world.consumers;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.ui.elements.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeSpin extends Consume {
	public float minSpeed = Float.NEGATIVE_INFINITY;
	public float maxSpeed = Float.POSITIVE_INFINITY;

	/**
	 * Extra low or high value that the consumer may be able to accept.
	 */
	public float tolerance = 0.001f;
	
	/**
	 * Interpolation curve between the speed and the efficiency multiplier.
	 */
	public Interp efficiencyScale = Interp.linear;

	public float minEfficiency = 0f;
	public float maxEfficiency = 1f;
	public boolean showGraph = false;

	public HasSpin cast(Building build) {
		try {
			return (HasSpin) build;
		} catch (ClassCastException e) {
			throw new RuntimeException("This consumer requires a block that supports the rotation system, use a different consumer", e);
		}
	}
	
	@Override
	public void display(Stats stats) {
		stats.add(SWStat.spinRequirement, table -> {
			if (showGraph) {
				Element stat = table.getCells().size != 0 ? table.getCells().first().get() : null;

				if (stat != null) {
					table.clear();

					table.table(base -> {
						base.add(stat);
						base.add(Core.bundle.format(
							"stat.sw-spin-requirement.format",
							minSpeed == Float.POSITIVE_INFINITY ? "∞" : (minSpeed == Float.NEGATIVE_INFINITY ? "-∞" : Strings.autoFixed(minSpeed * 10f, 2)),
							maxSpeed == Float.POSITIVE_INFINITY ? "∞" : (maxSpeed == Float.NEGATIVE_INFINITY ? "-∞" : Strings.autoFixed(maxSpeed * 10f, 2))
						));
					}).left();

					table.row();
					table.add(new GraphElement() {{
						background = Styles.grayPanel;
						innerBackground = Styles.grayPanelDark;
						horizontalPadding = verticalPadding = 10;
						horizontalTextPadding = 30;
						verticalTextPadding = 10;
						horizontalLineTextPadding = 30;
						verticalLineTextPadding = 15;
						stepMul = 4;
						lineColor = Color.darkGray;
						startLineColor = Pal.accent;
						lineStroke = 2.5f;
						progressStroke = 2.5f;
						rows = 4;
						columns = 5;
						rowTextProv = i -> Strings.autoFixed((minEfficiency + (maxEfficiency - minEfficiency) * i / rows) * 100f, 1) + "%";
						columnTextProv = i -> {
							float min = minSpeed == Float.NEGATIVE_INFINITY ? 0f : minSpeed;
							float max = maxSpeed == Float.POSITIVE_INFINITY ? 100f : maxSpeed;
							return Strings.autoFixed((min + (max - min) * i / columns) * 10, 1);
						};
						progressFunc = a -> {
							float min = minSpeed == Float.NEGATIVE_INFINITY ? 0f : minSpeed;
							float max = maxSpeed == Float.POSITIVE_INFINITY ? 100f : maxSpeed;
							return Mathf.map(efficiencyScale.apply(min + (max - min) * a), minEfficiency, maxEfficiency, 0f, 1f);
						};
						columnLabel = SWStat.spinMinute.localized();
						labelColor = Pal.accent.cpy();
						labelColor.a *= 0.25f;
						horizontalLabelPadding = 50;
						verticalLabelPadding = 0;
						textColor = Color.gray;
					}}).size(600, 300);
				}
			} else {
				table.add(Core.bundle.format(
					"stat.sw-spin-requirement.format",
					minSpeed == Float.POSITIVE_INFINITY ? "∞" : (minSpeed == Float.NEGATIVE_INFINITY ? "-∞" : Strings.autoFixed(minSpeed * 10f, 2)),
					maxSpeed == Float.POSITIVE_INFINITY ? "∞" : (maxSpeed == Float.NEGATIVE_INFINITY ? "-∞" : Strings.autoFixed(maxSpeed * 10f, 2))
				));
			}
		});
	}
	
	@Override
	public float efficiency(Building build) {
		return getSpeed(build) + tolerance >= minSpeed && getSpeed(build) - tolerance <= maxSpeed ? 1f : 0f;
	}
	@Override
	public float efficiencyMultiplier(Building build) {
		if (getSpeed(build) + tolerance >= minSpeed && getSpeed(build) - tolerance <= maxSpeed) {
			return efficiencyScale.apply(getSpeed(build));
		}
		return 0f;
	}
	
	public float getSpeed(Building build) {
		return cast(build).getSpeed();
	}
}
