import {defineConfig} from "vite";
import * as path from "path";
import vue from '@vitejs/plugin-vue'
import {globSync} from 'glob';
import {fileURLToPath} from 'url';

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
            // input: './src/main.js',
            input: resolveInput()
        },
    },
})

function resolveInput() {
    return Object
        .fromEntries(globSync('src/**/*.js').map(file => [
                // This remove `src/` as well as the file extension from each
                // file, so e.g. src/nested/foo.js becomes nested/foo
                path.relative('src', file.slice(0, file.length - path.extname(file).length)),
                // This expands the relative paths to absolute paths, so e.g.
                // src/nested/foo becomes /project/src/nested/foo.js
                fileURLToPath(new URL(file, import.meta.url))
            ])
        )
}
