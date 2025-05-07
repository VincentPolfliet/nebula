import * as dot from 'dot-object';

export interface Translations {
    [key: string]: string;
}

export class TranslationService {
    private translations: Translations = {};
    private lang: string;
    private readonly baseUrl: string;
    private readonly storageKeyPrefix: string;

    /**
     * @param defaultLang language to fall back to (and to load initially if nothing in storage)
     * @param baseUrl     where to fetch your JSON files, e.g. '/assets/i18n'
     * @param storageKeyPrefix prefix to use in localStorage (default: 'i18n_')
     */
    constructor(
        defaultLang: string = 'en',
        baseUrl: string = '/assets/i18n',
        storageKeyPrefix: string = 'i18n_',
    ) {
        this.baseUrl = baseUrl.replace(/\/$/, '');
        this.storageKeyPrefix = storageKeyPrefix;
        this.lang = defaultLang;
    }

    /**
     * Initialize the service: load either the embedded JSON or from storage/http.
     */
    async init(): Promise<void> {
        const cached = localStorage.getItem(this.storageKeyPrefix + this.lang);
        if (cached) {
            try {
                this.translations = JSON.parse(cached);
                return;
            } catch (e) {
                console.warn('Invalid i18n cache, refetching', e);
                localStorage.removeItem(this.storageKeyPrefix + this.lang);
            }
        }

        await this.load(this.lang);
    }

    /**
     * Fetch JSON for `lang` and cache it.
     */
    async load(lang: string): Promise<void> {
        const res = await fetch(`${this.baseUrl}/${lang}.json`, {
            headers: {'Content-Type': 'application/json'},
            cache: 'reload',
        });
        if (!res.ok) {
            throw new Error(`Failed to load translations: ${res.status} ${res.statusText}`);
        }

        this.translations = dot.dot(await res.json());
        this.lang = lang;
        this.persist(lang, this.translations);
    }

    /** Change language at runtime (reloads JSON if needed). */
    async setLanguage(lang: string): Promise<void> {
        if (lang === this.lang) {
            return;
        }

        await this.load(lang);
    }

    /** Current active language code. */
    getLanguage(): string {
        return this.lang;
    }

    /**
     * Translate a key; returns the key itself if missing.
     * Optionally, interpolate parameters in the form `{param}`.
     */
    translate(key: string): string {
         // "__" is indicating the resource hasn't been found
        return this.translations[key] ?? "__" + key + "__";
    }

    /** Shorthand */
    t(key: string): string {
        return this.translate(key);
    }

    /** Save to localStorage */
    private persist(lang: string, data: Translations) {
        try {
            localStorage.setItem(this.storageKeyPrefix + lang, JSON.stringify(data));
        } catch (e) {
            console.warn('Could not cache translations', e);
        }
    }
}

const i18nService = new TranslationService()

export async function initI18n(): Promise<TranslationService> {
    await i18nService.setLanguage(await getCurrentLang());
    await i18nService.init()
        .catch(e => {
            console.error(e);
        })
        .finally(() => {
            console.log("I18n initialized");
        });
    return i18nService;
}


/**
 * Fetches the Content-Language header for the current page.
 * Falls back to 'en' if unavailable.
 */
export async function getCurrentLang(): Promise<string> {
    try {
        const res = await fetch(window.location.href, {
            method: 'HEAD',
            // optionally skip CORS preflight by ensuring same-origin
            credentials: 'same-origin'
        });
        const lang = res.headers.get('Content-Language');
        return lang || 'en';
    } catch (e) {
        console.error('Failed to fetch Content-Language:', e);
        return 'en';
    }
}
