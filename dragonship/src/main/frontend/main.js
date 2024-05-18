import 'vite/modulepreload-polyfill'

import "htmx.org"

import './icons.css'
import './index.scss'

import humanizeAll from "./utils/humanizeAll.js";

document.addEventListener('DOMContentLoaded', humanizeAll);
document.addEventListener('htmx:afterSwap', humanizeAll);

humanizeAll();
