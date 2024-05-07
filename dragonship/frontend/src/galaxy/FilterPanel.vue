<script setup>
import FilterPanelBlock from "./FilterPanelBlock.vue";
import { onUpdated, toRefs} from "vue";

const props = defineProps({
    types: Array
});

const {types} = toRefs(props)

function resetAllFilters() {
    types.value.forEach(t => t.active = false);
}

onUpdated(() => {
    console.log("updated")
})

</script>

<template>
    <div class="panel">
        <p class="panel-heading">Filter</p>
        <FilterPanelBlock v-for="(item, i) in types" :key="item.title"
                          v-model:active="types[i].active"
                          :icon-class="item.iconClass"
                          :title="item.title"
        ></FilterPanelBlock>
        <div class="panel-block">
            <button class="button is-link is-outlined is-fullwidth" @click="resetAllFilters">
                Reset all filters
            </button>
        </div>
    </div>
</template>

<style scoped>
.panel {
    background: var(--bulma-background);
}

.panel-heading {
    background-color: inherit;
}
</style>
