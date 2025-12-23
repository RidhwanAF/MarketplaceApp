package com.raf.auth.presentation.viewmodel

data class AuthState(
    val isLoading: Boolean = false,
    val isLoginState: Boolean = true,
    val isLoginSuccess: String? = null,
    val uiMessage: String? = null,
)

data class AuthUserLogin(
    val id: Int,
    val username: String,
    val password: String,
) {
    // https://fakestoreapi.com/users
    companion object {
        val listAuthUserLogin = listOf(
            AuthUserLogin(1, "johnd", "m38rmF$"),
            AuthUserLogin(2, "david", "morrison"),
            AuthUserLogin(3, "kevinryan", "kev02937@"),
            AuthUserLogin(4, "donero", "ewedon"),
            AuthUserLogin(5, "derek", "jklg*_56"),
            AuthUserLogin(6, "david_r", "3478*#54"),
            AuthUserLogin(7, "snyder", "f238&@*$"),
            AuthUserLogin(8, "hopkins", $$"William56$hj"),
            AuthUserLogin(9, "kate_h", "kfejk@*_"),
            AuthUserLogin(10, "jimmie_k", "klein*#%*"),
        )
    }
}