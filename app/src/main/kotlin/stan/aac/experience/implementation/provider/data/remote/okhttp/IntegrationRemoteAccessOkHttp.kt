package stan.aac.experience.implementation.provider.data.remote.okhttp

import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.foundation.provider.converter.Converter
import stan.aac.experience.foundation.provider.data.remote.IntegrationRemoteAccess

class IntegrationRemoteAccessOkHttp(
    private val client: OkHttpClient,
    private val converterFrom: Converter.From
) : IntegrationRemoteAccess {
    override fun getFandoms(query: String?): List<Fandom> {
        val url = "https://www.wikia.com/api/v1/Wikis"
        val httpUrl = HttpUrl.parse(url) ?: error("wrong url $url")
        val httpUrlBuilder = httpUrl.newBuilder()
        if (query.isNullOrEmpty()) {
            httpUrlBuilder.addPathSegment("List")
        } else {
            httpUrlBuilder.addPathSegment("ByString")
            httpUrlBuilder.addQueryParameter("string", query)
            httpUrlBuilder.addQueryParameter("expand", "1")
        }
        val requestBuilder = Request.Builder()
            .cacheControl(CacheControl.FORCE_NETWORK)
            .url(httpUrlBuilder.build())
        val call = client.newCall(requestBuilder.build())
        val response = call.execute()
        val code = response.code()
        if (code != 200) error("Unknown response code $code!")
        val responseBody = response.body() ?: error("null response body with code $code")
        val body = responseBody.use { it.string() }
        return converterFrom.fandoms(body)
    }
}
