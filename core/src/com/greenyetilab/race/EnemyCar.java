package com.greenyetilab.race;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * An enemy car
 */
public class EnemyCar extends Vehicle implements Collidable {
    private static final float ACTIVE_EXTRA_HEIGHT = Constants.VIEWPORT_WIDTH / 2;

    public EnemyCar(GameWorld world, Assets assets, float originX, float originY) {
        super(selectCarTextureRegion(assets), world, originX, originY);

        // Wheels
        TextureRegion wheelRegion = assets.wheel;
        final float REAR_WHEEL_Y = Constants.UNIT_FOR_PIXEL * 16f;
        final float WHEEL_BASE = Constants.UNIT_FOR_PIXEL * 46f;

        float wheelW = Constants.UNIT_FOR_PIXEL * wheelRegion.getRegionWidth();
        float rightX = getWidth() / 2 - wheelW / 2 + 0.05f;
        float leftX = -rightX;
        float rearY = -getHeight() / 2 + REAR_WHEEL_Y;
        float frontY = rearY + WHEEL_BASE;

        Vehicle.WheelInfo info;
        info = addWheel(wheelRegion, leftX, frontY);
        info.steeringFactor = 1;
        info = addWheel(wheelRegion, rightX, frontY);
        info.steeringFactor = 1;
        info = addWheel(wheelRegion, leftX, rearY);
        info.wheel.setCanDrift(true);
        info = addWheel(wheelRegion, rightX, rearY);
        info.wheel.setCanDrift(true);

        mBody.setAwake(false);
    }

    public void setDrivingAngle(float angle) {
        angle = (angle - 90) * MathUtils.degreesToRadians;
        mBody.setTransform(mBody.getPosition(), angle);
    }

    @Override
    public boolean act(float dt) {
        super.act(dt);
        float bottomY = mGameWorld.getBottomVisibleY() - ACTIVE_EXTRA_HEIGHT;
        float topY = mGameWorld.getTopVisibleY() + ACTIVE_EXTRA_HEIGHT;
        boolean isActive = bottomY <= getY() && getY() <= topY;
        if (isActive) {
            if (isDead()) {
                setAccelerating(false);
            } else {
                drive();
            }
        } else {
            mBody.setAwake(false);
        }
        boolean keep = getY() >= bottomY - Constants.VIEWPORT_POOL_RECYCLE_HEIGHT;
        if (!keep) {
            dispose();
        }
        return keep;
    }

    private void drive() {
        setAccelerating(true);

        float directionAngle = mGameWorld.getMapInfo().getDirectionAt(getX(), getY());
        float angle = getAngle();
        float delta = Math.abs(angle - directionAngle);
        if (delta < 2) {
            setDirection(0);
            return;
        }
        float correctionIntensity = Math.min(1, delta / 45f);
        if (directionAngle > angle) {
            setDirection(correctionIntensity);
        } else {
            setDirection(-correctionIntensity);
        }
    }

    private static TextureRegion selectCarTextureRegion(Assets assets) {
        return assets.cars.get(MathUtils.random(assets.cars.size - 1));
    }

    @Override
    public void beginContact(Contact contact, Fixture otherFixture) {
        Object other = otherFixture.getBody().getUserData();
        if (other instanceof Mine) {
            kill();
        }
    }

    @Override
    public void endContact(Contact contact, Fixture otherFixture) {

    }
}
