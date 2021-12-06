package com.example.adminmanager.network

import android.util.Log
import com.example.adminmanager.network.login.Token
import com.google.gson.Gson
import entity.PackageOrder
import java.io.BufferedReader
import java.io.InputStream
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest
import javax.net.ssl.HttpsURLConnection


object connection {

    private var token: String? = null

    private val cookieName = "hoduongvuong66666"

    private val host = "https://trumbien-main.herokuapp.com/"

    private fun sendRequest(sUrl: String, method: String): String? {
        val inputStream: InputStream
        var result: String? = null

        try {
            val url = URL(sUrl)
            val conn: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            conn.requestMethod = method
            conn.connect()
            inputStream = conn.inputStream
            result = inputStream?.bufferedReader()?.use(BufferedReader::readText)
        } catch (err: Error) {
            print("Error when executing get request: " + err.localizedMessage)
        } catch (err: Exception) {
            err.printStackTrace()
            result = null
        }

        return result
    }

    private fun sendRequest(sUrl: String, method: String, body: String): String? {
        val inputStream: InputStream
        var result: String?

        try {
            val url = URL(sUrl)
            val conn: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            conn.requestMethod = method
            conn.addRequestProperty("Content-Type", "application/json")
            conn.addRequestProperty("Accept", "application/json, text/plain, */*")
            token?.let {
                conn.addRequestProperty("Cookie", "$cookieName=$token")
            }
            conn.doOutput = true

            conn.outputStream.use { os ->
                val input: ByteArray = body.encodeToByteArray()
                os.write(input, 0, input.size)
            }

            conn.connect()
            inputStream = conn.inputStream
            result = inputStream?.bufferedReader()?.use(BufferedReader::readText)

            conn.headerFields.forEach {
                Log.d("Testing data", "${it.key}: ${it.value}")
            }
        } catch (err: Error) {
            print("Error when executing get request: " + err.localizedMessage)
            result = null
        } catch (err: Exception) {
            err.printStackTrace()
            result = null
        }

        return result
    }

    fun Post(path: String): String? {
        return sendRequest(host + path, "POST")
    }

    fun Login(userName: String, password: String): Boolean {
        val loginBody = "{\"username\":\"$userName\",\"password\":\"${md5(password)}\"}"
        val response = sendRequest(
            host + "auth/signin",
            "POST",
            loginBody
        )
        response?.let {
            try {
                val token = Gson().fromJson(it, Token::class.java)
                Log.d("Testing data", token.token)
                this.token = token.token
            } catch (ex: Exception) {
                this.token = null
            }
        }
        return this.token != null
    }

    fun GetAllOrder(status: PackageOrder.Status): String? {
        val queryBody = "{\"query\":\"${status.getName()}\",\"page\":0,\"size\":100,\"filter\":\"30\"}"
        return sendRequest(
            host + "order/admin/all",
            "POST",
            queryBody
        )
    }

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
            .uppercase()
    }
}