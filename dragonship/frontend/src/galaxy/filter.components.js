import {defineCustomElement} from 'vue'
import FilterPanel from './FilterPanel.vue'
import FilterPanelBlock from './FilterPanelBlock.vue'

const CustomFilterPanel = defineCustomElement(FilterPanel)
const CustomFilterPanelBlock = defineCustomElement(FilterPanelBlock)

export {CustomFilterPanel, CustomFilterPanelBlock}

export function registerFilterComponent() {
    customElements.define('filter-panel', CustomFilterPanel)
    customElements.define('filter-panel-block', CustomFilterPanelBlock)
}
