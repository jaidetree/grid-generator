import * as esbuild from 'esbuild'
import svgr from 'esbuild-plugin-svgr'

const ctx = await esbuild.context({
  entryPoints: ['build/js/app.js'],
  outfile: 'public/js/app.bundle.js',
  bundle: true,
  sourcemap: true,
  plugins: [
    svgr(),
  ],
})

await ctx.watch()
console.log('Watching...')
