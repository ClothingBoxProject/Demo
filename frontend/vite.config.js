import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
//  Vite 클라이언트에서 /api/...로 요청을 보내면
//  자동으로 Spring Boot의 http://localhost:8080/api/...로 프록시