package sw.content;

import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.abilities.SpawnDeathAbility;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.SapBulletType;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;
import sw.entities.comp.SubmarineUnit;
import sw.type.SWUnitType;

import static mindustry.world.blocks.units.UnitFactory.UnitPlan;

public class SWUnitTypes {
  public static UnitType
  swarm, ambush, trap,
  recluse;

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


    recluse = new SWUnitType("recluse") {{
      speed = 1;
      health = 250;
      hitSize = 8f;
      accel = 0.4f;
      drag = 0.14f;
      rotateSpeed = 4f;
      range = maxRange = 40f;
      submerges = true;

      constructor = SubmarineUnit::new;

      weapons.add(new Weapon("sw-recluse-weapon") {{
        x = 3.75f;
        y = -1.25f;
        reload = 20f;
        shootSound = Sounds.sap;
        rotate = true;
        bullet = new SapBulletType() {{
          damage = 20f;
          length = 40f;
          color = Pal.sap;
        }};
      }});
    }};

    ((UnitFactory) Blocks.airFactory).plans.add(new UnitPlan(swarm, 60f * 10f, ItemStack.with(SWItems.nickel, 12, Items.silicon, 7)));
    ((Reconstructor) Blocks.additiveReconstructor).upgrades.add(new UnitType[]{swarm, ambush});
    ((Reconstructor) Blocks.multiplicativeReconstructor).upgrades.add(new UnitType[]{ambush, trap});
  }
}
