<template>
    <div class="field has-addons">
        <div class="control is-expanded">
            <input
                v-model="props.input"
                class="input"
                :type="currentInputType"
                :placeholder="placeholder"
            />
        </div>

        <div class="control">
            <button class="button toggle-button" @click.prevent="toggleView">
                {{ isVisible ? '🔒 ' + hideText : '🔓 ' + viewText }}
            </button>
        </div>

        <div class="control">
            <button class="button copy-button" @click="copyToClipboard">
                📋 {{ copyText }}
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

const isVisible = ref(false);

const props = defineProps({
    input: String,
    hideText: {
        type: String,
        default: "Hide"
    },
    copyText: {
        type: String,
        default: "Copy"
    },
    viewText: {
        type: String,
        default: "View"
    },
    inputType: {
        type: String,
        default: "password"
    },
    placeholder: String,
    onCopy: Function,
    onFailure: Function,
})

const currentInputType = ref(props.inputType);

function toggleView() {
    isVisible.value = !isVisible.value;
    currentInputType.value = isVisible.value ? "text" : props.inputType
}

function copyToClipboard() {
    navigator.clipboard.writeText(props.input!!)
        .then(() => {
                if (props.onCopy) {
                    props.onCopy();
                }
            }
        )
        .catch(err => {
            console.error('Could not copy text: ', err);
            if (props.onFailure) {
                props.onFailure();
            }
        });
}


</script>

<style scoped>
.copy-button,
.toggle-button {
    cursor: pointer;
}
</style>
