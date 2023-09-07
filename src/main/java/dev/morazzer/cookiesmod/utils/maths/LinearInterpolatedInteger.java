package dev.morazzer.cookiesmod.utils.maths;

import lombok.Getter;

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

	public LinearInterpolatedInteger(long timeToTarget, int startValue) {
		this.timeToTarget = timeToTarget;
		this.startValue = startValue;
		this.value = startValue;
		this.hasReachedTarget = true;
	}

	public void setTimeToTarget(long timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	public void setTargetValue(int targetValue) {
		this.setTargetValue(targetValue, false);
	}

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

	public void tick() {
		if (this.hasReachedTarget) {
			return;
		}
		this.tick(System.currentTimeMillis() - this.lastMillis);
	}

	public void tick(long deltaTime) {
		//noinspection DuplicatedCode
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

	public void setValue(int value) {
		this.value = value;
	}

	public boolean hasReachedTarget() {
		return this.hasReachedTarget;
	}

	public int getTarget() {
		return targetValue;
	}
}
