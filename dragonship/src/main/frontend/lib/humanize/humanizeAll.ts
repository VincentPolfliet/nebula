import dayjs from 'dayjs';
import duration, {DurationUnitType} from 'dayjs/plugin/duration';
import relativeTime from 'dayjs/plugin/relativeTime';
import CountdownTimer from "../../utils/Timer";

dayjs.extend(duration);
dayjs.extend(relativeTime);

/**
 * Convert a number of time units, specified by a type into a human-readable duration.
 */
export function humanizeDuration(timeUnits: number, type: DurationUnitType, withPrefix: boolean = false): string {
    return dayjs.duration(timeUnits, type).humanize(withPrefix);
}

/**
 * Parse an ISO datetime string into a Dayjs object.
 */
export function parseDatetime(datetime: string): dayjs.Dayjs {
    return dayjs(datetime);
}

/**
 * Handles humanizing and optional countdown for a single element.
 */
export class HumanizeElement {
    private element: HTMLElement;
    private timer?: CountdownTimer;

    private withPrefix: boolean = false;
    private durationUnitType: DurationUnitType = 'seconds';
    private initialTime: number = 0;


    constructor(element: HTMLElement) {
        this.element = element;
    }

    /**
     * Initialize humanization and countdown if applicable.
     */
    init(): void {
        if (!this.element.classList.contains('humanize-relativetime')) {
            return;
        }

        this.withPrefix = (this.element.dataset.humanizePrefix === 'true' || false);
        this.durationUnitType = this.element.dataset.humanizeUnits as DurationUnitType ?? 'seconds';
        this.initialTime = Number(this.element.innerText);

        this.updateText(this.initialTime);

        if (this.element.classList.contains('humanize-update')) {
            this.setupCountdown();
        }

        this.element.classList.add('is-humanized');
    }

    /**
     * Update the element's text to the humanized duration.
     */
    private updateText(timeUnits: number): void {
        this.element.innerText = humanizeDuration(timeUnits, this.durationUnitType, this.withPrefix);
    }

    /**
     * Set up a countdown timer to refresh the humanized text until complete.
     */
    private setupCountdown(): void {
        const datetimeAttr = this.element.getAttribute('datetime');
        if (!datetimeAttr) {
            console.warn('Missing datetime attribute on humanize-update element.');
            return;
        }

        const targetDate = parseDatetime(datetimeAttr);

        this.timer = new CountdownTimer(
            targetDate.toDate(),
            () => {
                const now = dayjs();
                const diffSeconds = targetDate.diff(now, this.durationUnitType);
                this.updateText(diffSeconds);
            },
            () => {
                this.element.classList.remove('humanize-update');
                this.element.classList.add('is-hidden');
            }
        );
        this.timer.start();
    }
}

/**
 * Find and humanize all matching elements on the page.
 */
const humanizeAll = (): void => {
    const selector = '.humanize:not(.is-humanized)';
    const elements = document.querySelectorAll<HTMLElement>(selector);

    elements.forEach(el => {
        new HumanizeElement(el).init();
    });
};

export default humanizeAll;
