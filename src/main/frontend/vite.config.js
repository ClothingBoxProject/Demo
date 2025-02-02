import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
//export default defineConfig({
//  plugins: [react()],
//})
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
});
//  Vite 클라이언트에서 /api/...로 요청을 보내면
//  자동으로 Spring Boot의 http://localhost:8080/api/...로 프록시