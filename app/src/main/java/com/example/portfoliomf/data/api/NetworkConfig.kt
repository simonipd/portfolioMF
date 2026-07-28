package com.example.portfoliomf.data.api

object NetworkConfig {
    // Change this to 'false' to use the local mock (interceptor)
    // Change this to 'true' to use the Postman mock server
    const val USE_REAL_NETWORK = false
    
    // URL for Mock Server in Postman
    // NOTE: Retrofit requires the base URL to end with a /
    const val BASE_URL = "https://-API-ABAQUS-CL/SOME-ROUTE/"
    
}
