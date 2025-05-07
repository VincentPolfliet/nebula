import 'vite/modulepreload-polyfill'

import "htmx.org"

import './icons.scss'

import './__bulma.extensions.scss'
import './index.scss'

import humanizeAll from "./lib/humanize/humanizeAll.ts";

document.addEventListener('DOMContentLoaded', humanizeAll);
document.addEventListener('htmx:afterSwap', humanizeAll);

humanizeAll();
