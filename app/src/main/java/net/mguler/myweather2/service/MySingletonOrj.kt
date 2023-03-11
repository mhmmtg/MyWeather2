package net.mguler.myweather2.service

import android.content.Context

class MySingletonOrj private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: MySingletonOrj? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingletonOrj(context).also {
                    INSTANCE = it
                }
            }
    }

    /*
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
     */
}
