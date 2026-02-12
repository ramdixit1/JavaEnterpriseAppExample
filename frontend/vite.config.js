import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Vite config proxies UI API requests to gateway-service.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/accounts': 'http://localhost:8080',
      '/transactions': 'http://localhost:8080'
    }
  }
});
