package com.aeterna.aeterna.utils

/**
 * Small helper to resize common YouTube/Google image URLs.
 * Inspired by public utilities in similar projects but implemented originally.
 */
object ImageUrlUtils {
    /**
     * Resize a YouTube / Google profile/thumbnail url. If both width and height are null, returns the original url.
     */
    fun resize(url: String, width: Int? = null, height: Int? = null): String {
        if (width == null && height == null) return url

        // Google user content urls (lh3)
        val ghRegex = Regex("https://lh3\\.googleusercontent\\.com/.*=w(\\d+)-h(\\d+).*")
        ghRegex.matchEntire(url)?.groupValues?.let { group ->
            val W = group[1].toInt()
            val H = group[2].toInt()
            var w = width
            var h = height
            if (w != null && h == null) h = (w * H) / W
            if (w == null && h != null) w = (h * W) / H
            return url.split("=w")[0] + "=w${'$'}w-h${'$'}h-p-l90-rj"
        }

        // yt3 urls with =s<digits>
        val yt3Regex = Regex("https://yt3\\.ggpht\\.com/.*=s(\\d+)")
        if (yt3Regex.containsMatchIn(url)) {
            val size = width ?: height ?: return url
            return "${'$'}url-s${'$'}size"
        }

        return url
    }
}
