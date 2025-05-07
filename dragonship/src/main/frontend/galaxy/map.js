import 'vite/modulepreload-polyfill';

import 'bulma'
import '../index.scss'
import '../icons.scss'

import {createApp} from 'vue'
import Map from './Map.vue'

const galaxyDataElement = document.getElementById('galaxy-data');

const data = JSON.parse(galaxyDataElement.innerText);
console.log(data);

createApp(Map, {...data}).mount('#galaxy-app')
