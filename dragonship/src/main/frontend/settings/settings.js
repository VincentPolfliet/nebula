import CopyInputWithToggle from "../lib/components/CopyInputWithToggle.vue";
import {createApp} from "vue";
import "../main.js"
import {showToast} from "../lib/components/Toast.ts";
import {initI18n} from "../lib/translations/translations.js";

async function initializeApp() {
    const dataJsonElement = document.getElementById('settings-data-json');

    const data = JSON.parse(dataJsonElement.innerText);
    const i18n = await initI18n();

    createApp(CopyInputWithToggle, {
        input: data['token'],
        hideText: i18n.t('hide'),
        copyText: i18n.t('copy'),
        viewText: i18n.t('view'),
        onCopy: () => {
            showToast(i18n.t('token.copied'), 'is-success');
        },
        onFailure: () => {
            showToast(i18n.t('token.copy-failed'), 'is-danger');
        }
    }).mount('#token-input-field');
}

await initializeApp();
