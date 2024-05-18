import dayjs from "dayjs";

import duration from "dayjs/plugin/duration";
import relativeTime from "dayjs/plugin/relativeTime";

dayjs.extend(duration)
dayjs.extend(relativeTime)

const humanizeClass = "humanize";

const isHumanizedClass = "is-humanized";

const humanizeAll = () => {
    const elements = document.querySelectorAll(`.${humanizeClass}:not(.${isHumanizedClass})`);

    elements.forEach(element => {
        if (element.classList.contains("humanize-relativetime")) {
            const date = dayjs.duration(Number(element.innerText), "seconds");
            element.innerText = date.humanize();
        }

        element.classList.add(isHumanizedClass);
    });
}

export default humanizeAll
