import {defineConfig} from "vite";
import * as path from "path";
import vue from '@vitejs/plugin-vue'


export default defineConfig({
    root: path.resolve(__dirname, 'src'),
    server: {
        hot: true // 🥵
    },
    plugins: [vue()],
    build: {
        // generate .vite/manifest.json in outDir
        manifest: true,
        rollupOptions: {
            // overwrite default .html entry
            input: './src/main.js',
        },
    },
})
