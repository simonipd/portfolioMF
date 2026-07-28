# portfolioMF
Portafolio Fondos mutuos, y activos

Implement proyect with this structure:

1. Red:
   ◦ RetrofitClient configured with OkHttpClient.

◦ MockInterceptor correctly routing requests to the Mock service.

◦ NetworkConfig allows switching to Postman with a single boolean.

2. Models (DTOs):
   ◦ All monetary fields and percentages are Strings (avoids floating-point precision issues).

◦ Paginated response structures implemented for Positions and Showcase.

3. User Interface (compose):
   ◦ Portfolio: Displays value, performance, and asset list.

Search: Real-time search using AssetViewModel.

Detail and Purchase: Handles loading statuses, success, and errors (including the closed market scenario).

4. Mocks:
   ◦ PACEMockAPIService integrated with the sample data you provided.
