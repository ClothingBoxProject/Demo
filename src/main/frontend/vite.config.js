import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
//export default defineConfig({
//  plugins: [react()],
//})

export default ({ mode }) => {
  // `loadEnv`를 사용하여 환경 변수 로드
  const env = loadEnv(mode, process.cwd());

  // 로드된 환경 변수를 로그로 출력하여 확인
  console.log(env);

  return defineConfig({
    plugins: [react()],
    server: {
      port: 3000,
      proxy: {
        "/back": {
          target: env.VITE_API_URL, // 환경 변수 사용
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/back/, ""),
        },
      },
    },
  });
};
//  Vite 클라이언트에서 /api/...로 요청을 보내면
//  자동으로 Spring Boot의 http://localhost:8080/api/...로 프록시