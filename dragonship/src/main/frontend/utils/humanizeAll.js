import dayjs from "dayjs";

import duration from "dayjs/plugin/duration";
import relativeTime from "dayjs/plugin/relativeTime";
import CountdownTimer from "./Timer.ts";

dayjs.extend(duration)
dayjs.extend(relativeTime)

const humanizeClass = "humanize";
const humanizeUpdateClass = "humanize-update";
const isHumanizedClass = "is-humanized";

const humanizeAll = () => {
    const elements = document.querySelectorAll(`.${humanizeClass}:not(.${isHumanizedClass})`);

    elements.forEach(element => {
        if (element.classList.contains("humanize-relativetime")) {
            const update = (durationInSeconds) => {
                const target = dayjs.duration(durationInSeconds, "seconds");
                element.innerText = target.humanize();
            };

            // setting the initiale value from the html
            update(Number(element.innerText))

            if (element.classList.contains(humanizeUpdateClass)) {
                const targetDate = dayjs(element.getAttribute("datetime"));

                const timer = new CountdownTimer(
                    targetDate,
                    () => {
                        const now = dayjs();
                        const currentDifference = targetDate.diff(now, "seconds");
                        update(currentDifference);
                    },
                    () => {
                        element.classList.remove(humanizeUpdateClass);
                        element.classList.add("is-hidden")
                    });

                timer.start();
            }
        }

        element.classList.add(isHumanizedClass);
    });
}

export default humanizeAll
