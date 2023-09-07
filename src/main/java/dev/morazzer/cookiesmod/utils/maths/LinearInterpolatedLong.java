package dev.morazzer.cookiesmod.utils.maths;

import lombok.Getter;

@SuppressWarnings("unused")
public class LinearInterpolatedLong {

	private long timeToTarget;
	private long timeSpend;
	@Getter
	private long value;
	private long targetValue;
	private long startValue;
	private long lastMillis;
	private boolean hasReachedTarget;

	public LinearInterpolatedLong(long timeToTarget, long startValue) {
		this.timeToTarget = timeToTarget;
		this.startValue = startValue;
		this.hasReachedTarget = true;
	}

	public void setTimeToTarget(long timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	public void setTargetValue(long targetValue) {
		this.startValue = this.targetValue;
		this.targetValue = targetValue;
		this.timeSpend = 0;
		this.lastMillis = System.currentTimeMillis();
		this.hasReachedTarget = false;
	}

	public void tick() {
		if (this.hasReachedTarget) {
			return;
		}
		this.timeSpend += System.currentTimeMillis() - this.lastMillis;
		float time = (float) this.timeSpend / (float) this.timeToTarget;
		if (time > 1) {
			this.hasReachedTarget = true;
			this.value = this.targetValue;
			return;
		}

		this.value = (long) ((1 - time) * this.startValue + time * this.targetValue);
		this.lastMillis = System.currentTimeMillis();
	}

	public boolean hasReachedTarget() {
		return this.hasReachedTarget;
	}
}
