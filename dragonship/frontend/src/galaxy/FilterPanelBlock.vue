<script setup>
import {onUpdated} from "vue";

const active = defineModel('active')

defineProps({
    iconClass: String,
    title: String
})

onUpdated(() => {
    console.log("updated: FilterPanelBlock")
})
</script>

<template>
    <a class="panel-block">
        <input type="checkbox" v-model="active">
        <div class="panel-icon" :class="iconClass"></div>
        {{ title }}
    </a>
</template>

<style scoped>

</style>
