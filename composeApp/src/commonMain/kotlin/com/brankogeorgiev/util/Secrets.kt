package com.brankogeorgiev.util

class Secrets {
    companion object {
        const val BASE_URL = "https://wgpjnmdbzdszbldmuqpd.supabase.co/functions/v1"
        const val SUPABASE_URL = "https://wgpjnmdbzdszbldmuqpd.supabase.co"
        const val SUPABASE_API_KEY =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndncGpubWRiemRzemJsZG11cXBkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njc5Mjg4NzQsImV4cCI6MjA4MzUwNDg3NH0.9s1aT1-lBOvhHlrCcJ0jOyhaT1JJcFg5Y7mjem6BJcA"
    }
}

fun getBaseUrl(): String {
    return Secrets.BASE_URL
}