LOCAL_PATH := $(call my-dir)
define walk
  $(wildcard $(1)) $(foreach e, $(wildcard $(1)/*), $(call walk, $(e)))
endef

include $(CLEAR_VARS)
LOCAL_MODULE           := applist_detector
LOCAL_C_INCLUDES       := $(LOCAL_PATH)/external/linux_syscall_support $(LOCAL_PATH)/src
FILE_LIST              := $(filter %.c %.cpp, $(call walk, $(LOCAL_PATH)/src))
LOCAL_SRC_FILES        := $(FILE_LIST:$(LOCAL_PATH)/%=%)
LOCAL_STATIC_LIBRARIES := cxx xposed_detector
include $(BUILD_SHARED_LIBRARY)

$(call import-module, prefab/cxx)
$(call import-module, prefab/xposeddetector)
