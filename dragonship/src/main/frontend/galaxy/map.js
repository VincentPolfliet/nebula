import 'vite/modulepreload-polyfill';

import 'bulma'
import '../index.scss'
import '../icons.css'

import {createApp} from 'vue'
import Map from './Map.vue'

const galaxyDataElement = document.getElementById('galaxy-data');
const systems = JSON.parse(galaxyDataElement.innerText);

createApp(Map, {...systems}).mount('#app')
