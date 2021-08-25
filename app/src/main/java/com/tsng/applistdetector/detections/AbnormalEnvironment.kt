package com.tsng.applistdetector.detections

object AbnormalEnvironment : IDetector() {

    override val name = "abnormal environment"

    override fun execute() {
        results.clear()
        val hasXposedHook = xposedDetector()
        results.add(Pair("Xposed hooks", if (hasXposedHook) Results.FOUND else Results.NOT_FOUND))
    }

    private external fun xposedDetector(): Boolean
}