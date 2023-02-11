package sw.content;

import mindustry.content.*;
import mindustry.entities.abilities.SpawnDeathAbility;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.SapBulletType;
import mindustry.entities.bullet.ShrapnelBulletType;
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
  recluse, retreat, evade;

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
          color = Pal.sapBullet;
        }};
      }});
    }};
    retreat = new SWUnitType("retreat") {{
      health = 580;
      speed = 0.9f;
      hitSize = 11f;
      accel = 0.4f;
      drag = 0.14f;
      rotateSpeed = 3.5f;
      range = maxRange = 80f;
      targetAir = false;
      submerges = true;
      vulnerabilityTime = 150f;

      constructor = SubmarineUnit::new;

      weapons.add(new Weapon("sw-retreat-weapon") {{
        x = y = 0f;
        reload = 60f;
        mirror = false;
        rotate = true;
        shootSound = Sounds.artillery;
        bullet = new ArtilleryBulletType(3f, 40) {{
          width = height = 10f;
          lifetime = 26.75f;
          collidesGround = true;
          splashDamage = 40f;
          splashDamageRadius = 24f;
          hitEffect = despawnEffect = Fx.hitBulletColor;
          frontColor = hitColor = trailColor = Pal.sapBullet;
          backColor = Pal.sapBulletBack;
          trailLength = 40;
          status = StatusEffects.sapped;
          statusDuration = 120f;
        }};
      }});
    }};
    evade = new SWUnitType("evade") {{
      health = 1200;
      speed = 0.8f;
      accel = 0.4f;
      drag = 0.14f;
      rotateSpeed = 3f;
      range = maxRange = 160f;
      submerges = true;
      vulnerabilityTime = 240f;

      constructor = SubmarineUnit::new;

      weapons.add(
              new Weapon() {{
                x = y = 0f;
                reload = 240f;
                rotateSpeed = 360f;
                mirror = false;
                rotate = true;
                shootSound = Sounds.missileLaunch;
                bullet = new BasicBulletType(4f, 200f) {{
                  width = height = 15f;
                  lifetime = 40f;
                  drag = -0.01f;
                  collideFloor = true;
                  splashDamage = 120f;
                  splashDamageRadius = 24f;
                  frontColor = trailColor = hitColor = Pal.sapBullet;
                  backColor = Pal.sapBulletBack;
                  status = StatusEffects.sapped;
                  statusDuration = 300f;
                  trailLength = 80;
                }};
              }},
              new Weapon("sw-evade-cannon") {{
                x = 0f;
                y = -7.25f;
                reload = 120f;
                targetAir = false;
                mirror = false;
                rotate = true;
                shootSound = Sounds.artillery;
                bullet = new ArtilleryBulletType(4f, 120) {{
                  width = height = 12f;
                  lifetime = 25f;
                  range = 100f;
                  collidesGround = true;
                  splashDamage = 120f;
                  splashDamageRadius = 24f;
                  hitEffect = despawnEffect = Fx.hitBulletColor;
                  frontColor = hitColor = trailColor = Pal.sapBullet;
                  backColor = Pal.sapBulletBack;
                  status = StatusEffects.sapped;
                  statusDuration = 180f;
                  trailLength = 40;
                }};
              }},
              new Weapon("sw-evade-mount") {{
                x = 8.75f;
                y = 4f;
                reload = 60f;
                rotate = true;
                shootSound = Sounds.shotgun;
                bullet = new ShrapnelBulletType() {{
                  damage = 80f;
                  length = 12f;
                  range = length;
                  toColor = Pal.sapBullet;
                  serrations = 4;
                }};
              }}
      );
    }};

    ((UnitFactory) Blocks.airFactory).plans.add(new UnitPlan(swarm, 60f * 10f, ItemStack.with(SWItems.compound, 12, Items.silicon, 7)));
    ((Reconstructor) Blocks.additiveReconstructor).upgrades.add(new UnitType[]{swarm, ambush}, new UnitType[]{recluse, retreat});
    ((Reconstructor) Blocks.multiplicativeReconstructor).upgrades.add(new UnitType[]{ambush, trap}, new UnitType[]{retreat, evade});
  }
}
