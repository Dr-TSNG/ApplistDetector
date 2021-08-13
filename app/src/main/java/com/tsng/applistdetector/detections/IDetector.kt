package com.tsng.applistdetector.detections

interface IDetector {

    enum class Results {
        FOUND, NOT_FOUND, PERMISSION_DENIED, SUSPICIOUS
    }

    val name: String
    var status: Results?
    var listGenerated: Set<String>?

    fun runDetection(packageName: String): Results
}