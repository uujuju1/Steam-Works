package sw.content;

import mindustry.content.Blocks;
import mindustry.content.Bullets;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.abilities.SpawnDeathAbility;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

import static mindustry.world.blocks.units.UnitFactory.*;

public class SWUnitTypes {
  public static UnitType swarm, ambush, trap;

  public static void load() {
    swarm = new UnitType("swarm") {{
      health = 250;
      speed = 3f;
      flying = lowAltitude = true;
      rotateSpeed = 10f;
      engineOffset = 6.5f;
      range = maxRange = 120f;

      constructor = UnitEntity::create;

      weapons.add(
        new Weapon("sw-swarm-weapon") {{
          x = 3f;
          y = -2f;
          reload = 15f;
          bullet = new BasicBulletType(2f, 12) {{
            lifetime = 60f;
            width = 7f;
            height = 9f;
          }};
        }}
      );
    }};
    ambush = new UnitType("ambush") {{
      health = 560;
      speed = 2.5f;
      flying = lowAltitude = true;
      hitSize = 10f;
      rotateSpeed = 8f;
      engineSize = 3f;
      engineOffset = 9f;
      range = maxRange = 120f;

      constructor = UnitEntity::create;

      weapons.add(
        new Weapon("sw-ambush-weapon") {{
          x = 4.75f;
          y = -0.75f;
          reload = 60;
          bullet = new BasicBulletType(3f, 25) {{
            lifetime = 40f;
            width = 9f;
            height = 11f;
          }};
        }}
      );

      abilities.add(new SpawnDeathAbility(swarm, 1, 0));
    }};

    trap = new UnitType("trap") {{
      health = 1300;
      speed = 2f;
      flying = lowAltitude = true;
      hitSize = 14f;
      engineSize = 5.25f;
      engineOffset = 12.5f;
      range = maxRange = 144f;

      constructor = UnitEntity::create;

      weapons.add(
        new Weapon("sw-trap-cannon") {{
          x = y = 0f;
          reload = 60;
          mirror = false;
          shootSound = Sounds.shotgun;
          shake = 2f;
          bullet = new ArtilleryBulletType(4f, 30) {{
            width = height = 10f;
            lifetime = 36f;
            splashDamage = 30f;
            splashDamageRadius = 64f;
            collides = collidesAir = collidesGround = true;
            hitEffect = despawnEffect = Fx.hitBulletBig;
          }};
        }}
      );

      abilities.addAll(new SpawnDeathAbility(ambush, 1, 0));
    }};

    ((UnitFactory) Blocks.airFactory).plans.add(new UnitPlan(swarm, 60f * 10f, ItemStack.with(SWItems.nickel, 12, Items.silicon, 7)));
    ((Reconstructor) Blocks.additiveReconstructor).upgrades.add(new UnitType[]{swarm, ambush});
    ((Reconstructor) Blocks.multiplicativeReconstructor).upgrades.add(new UnitType[]{ambush, trap});
  }
}
