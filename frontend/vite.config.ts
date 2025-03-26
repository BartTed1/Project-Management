import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true
  },
  build: {
    outDir: '../dist/public' // Zmień 'dist' na dowolny folder docelowy
  }
})
