package dev.morazzer.cookiesmod.utils.maths;

import lombok.Getter;

/**
 * Linear interpolated integer.
 */
@SuppressWarnings("unused")
public class LinearInterpolatedInteger {

    private long timeToTarget;
    private long timeSpend;
    @Getter
    private int value;
    private int targetValue;
    private int startValue;
    private long lastMillis;
    private boolean hasReachedTarget;

    /**
     * Create a linear interpolated integer.
     *
     * @param timeToTarget The time to reach the target value.
     * @param startValue   The start value.
     */
    public LinearInterpolatedInteger(long timeToTarget, int startValue) {
        this.timeToTarget = timeToTarget;
        this.startValue = startValue;
        this.value = startValue;
        this.hasReachedTarget = true;
    }

    /**
     * Set the time to reach the target value.
     *
     * @param timeToTarget The time.
     */
    public void setTimeToTarget(long timeToTarget) {
        this.timeToTarget = timeToTarget;
    }

    /**
     * Set the target value.
     *
     * @param targetValue The target value.
     */
    public void setTargetValue(int targetValue) {
        this.setTargetValue(targetValue, false);
    }

    /**
     * Set the target value.
     *
     * @param targetValue The new target.
     * @param force       If the target should be set even if its same.
     */
    public void setTargetValue(int targetValue, boolean force) {
        if (!force && targetValue == this.targetValue) {
            return;
        }
        this.startValue = this.value;
        this.targetValue = targetValue;
        this.timeSpend = 0;
        this.lastMillis = System.currentTimeMillis();
        this.hasReachedTarget = false;
    }

    /**
     * Tick the value.
     */
    public void tick() {
        if (this.hasReachedTarget) {
            return;
        }
        this.tick(System.currentTimeMillis() - this.lastMillis);
    }

    /**
     * Tick the value with a custom time difference.
     *
     * @param deltaTime The difference in time.
     */
    public void tick(long deltaTime) {
        if (this.hasReachedTarget) {
            return;
        }
        this.timeSpend += deltaTime;
        float time = (float) this.timeSpend / (float) this.timeToTarget;
        if (time > 1) {
            this.hasReachedTarget = true;
            this.value = this.targetValue;
            return;
        }

        this.value = (int) ((1 - time) * this.startValue + time * this.targetValue);
        this.lastMillis = System.currentTimeMillis();
    }

    /**
     * Set the current value.
     *
     * @param value The value.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * If the integer has reached its target.
     *
     * @return If it is done.
     */
    public boolean hasReachedTarget() {
        return this.hasReachedTarget;
    }

    /**
     * The current target.
     *
     * @return The target.
     */
    public int getTarget() {
        return targetValue;
    }

}
