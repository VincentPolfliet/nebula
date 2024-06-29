import dayjs from "dayjs";

class CountdownTimer {
    private readonly targetDate: dayjs.Dayjs;
    private readonly onTick: () => void;
    private readonly onComplete: () => void;
    private intervalId: any;

    constructor(targetDate: Date, onTick: () => void, onComplete: () => void) {
        this.targetDate = dayjs(targetDate);
        this.onTick = onTick;
        this.onComplete = onComplete;
        this.intervalId = null;
    }

    start(): void {
        this.update();  // Initial call to update immediately
        this.intervalId = setInterval(() => this.update(), 1000);
    }

    stop(): void {
        if (this.intervalId) {
            clearInterval(this.intervalId);
            this.intervalId = null;
        }
    }

    private update(): void {
        const now = dayjs(Date.now());
        const difference = this.targetDate.diff(now);

        if (difference <= 0) {
            this.stop();
            this.onComplete();
        } else {
            this.onTick();
        }
    }
}

export default CountdownTimer;
