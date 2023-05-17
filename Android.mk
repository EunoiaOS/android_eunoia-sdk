#
# Copyright (C) 2015 The CyanogenMod Project
#               2017-2023 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

# We have a special case here where we build the library's resources
# independently from its code, so we need to find where the resource
# class source got placed in the course of building the resources.
# Thus, the magic here.
# Also, this module cannot depend directly on the R.java file; if it
# did, the PRIVATE_* vars for R.java wouldn't be guaranteed to be correct.
# Instead, it depends on the R.stamp file, which lists the corresponding
# R.java file as a prerequisite.
eunoia_platform_res := APPS/com.eunoiaos.platform-res_intermediates/aapt

# List of packages used in eunoia-api-stubs
eunoia_stub_packages := eunoiaos.app:eunoiaos.content:eunoiaos.hardware:eunoiaos.media:eunoiaos.os:eunoiaos.preference:eunoiaos.profiles:eunoiaos.providers:eunoiaos.platform:eunoiaos.util:eunoiaos.trust

eunoia_framework_module := $(LOCAL_INSTALLED_MODULE)

# Make sure that R.java and Manifest.java are built before we build
# the source for this library.
eunoia_framework_res_R_stamp := \
    $(call intermediates-dir-for,APPS,com.eunoiaos.platform-res,,COMMON)/src/R.stamp
LOCAL_ADDITIONAL_DEPENDENCIES := $(eunoia_framework_res_R_stamp)

$(eunoia_framework_module): | $(dir $(eunoia_framework_module))com.eunoiaos.platform-res.apk

eunoia_framework_built := $(call java-lib-deps, com.eunoiaos.platform)

# ===========================================================
# Common Droiddoc vars
eunoia_platform_docs_src_files := \
    $(call all-java-files-under, $(eunoia_sdk_src)) \
    $(call all-html-files-under, $(eunoia_sdk_src))

eunoia_platform_docs_java_libraries := \
    com.eunoiaos.platform.sdk

# SDK version as defined
eunoia_platform_docs_SDK_VERSION := 15.1

# release version
eunoia_platform_docs_SDK_REL_ID := 9

eunoia_platform_docs_LOCAL_MODULE_CLASS := JAVA_LIBRARIES

eunoia_platform_docs_LOCAL_DROIDDOC_SOURCE_PATH := \
    $(eunoia_platform_docs_src_files)

eunoia_platform_docs_LOCAL_ADDITIONAL_JAVA_DIR := \
    $(call intermediates-dir-for,JAVA_LIBRARIES,com.eunoiaos.platform.sdk,,COMMON)

# ====  the api stubs and current.xml ===========================
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
    $(eunoia_platform_docs_src_files)
LOCAL_INTERMEDIATE_SOURCES:= $(eunoia_platform_LOCAL_INTERMEDIATE_SOURCES)
LOCAL_JAVA_LIBRARIES:= $(eunoia_platform_docs_java_libraries)
LOCAL_MODULE_CLASS:= $(eunoia_platform_docs_LOCAL_MODULE_CLASS)
LOCAL_DROIDDOC_SOURCE_PATH:= $(eunoia_platform_docs_LOCAL_DROIDDOC_SOURCE_PATH)
LOCAL_ADDITIONAL_JAVA_DIR:= $(eunoia_platform_docs_LOCAL_ADDITIONAL_JAVA_DIR)
LOCAL_ADDITIONAL_DEPENDENCIES:= $(eunoia_platform_docs_LOCAL_ADDITIONAL_DEPENDENCIES)

LOCAL_MODULE := eunoia-api-stubs

LOCAL_DROIDDOC_CUSTOM_TEMPLATE_DIR:= external/doclava/res/assets/templates-sdk

LOCAL_DROIDDOC_STUB_OUT_DIR := $(TARGET_OUT_COMMON_INTERMEDIATES)/JAVA_LIBRARIES/eunoia-sdk_stubs_current_intermediates/src

LOCAL_DROIDDOC_OPTIONS:= \
        -referenceonly \
        -stubpackages $(eunoia_stub_packages) \
        -exclude com.eunoiaos.platform.internal \
        -api $(INTERNAL_EUNOIA_PLATFORM_API_FILE) \
        -removedApi $(INTERNAL_EUNOIA_PLATFORM_REMOVED_API_FILE) \
        -nodocs

LOCAL_UNINSTALLABLE_MODULE := true

#include $(BUILD_DROIDDOC)

# $(gen), i.e. framework.aidl, is also needed while building against the current stub.
$(full_target): $(eunoia_framework_built) $(gen)
$(INTERNAL_EUNOIA_PLATFORM_API_FILE): $(full_target)
$(call dist-for-goals,sdk,$(INTERNAL_EUNOIA_PLATFORM_API_FILE))


# Documentation
# ===========================================================
include $(CLEAR_VARS)

LOCAL_MODULE := com.eunoiaos.platform.sdk
LOCAL_INTERMEDIATE_SOURCES:= $(eunoia_platform_LOCAL_INTERMEDIATE_SOURCES)
LOCAL_MODULE_CLASS := JAVA_LIBRARIES
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(eunoia_platform_docs_src_files)
LOCAL_ADDITONAL_JAVA_DIR := $(eunoia_platform_docs_LOCAL_ADDITIONAL_JAVA_DIR)

LOCAL_IS_HOST_MODULE := false
LOCAL_DROIDDOC_CUSTOM_TEMPLATE_DIR := vendor/eunoia/build/tools/droiddoc/templates-eunoia-sdk
LOCAL_ADDITIONAL_DEPENDENCIES := \
    services

LOCAL_JAVA_LIBRARIES := $(eunoia_platform_docs_java_libraries)

LOCAL_DROIDDOC_OPTIONS := \
        -android \
        -offlinemode \
        -exclude com.eunoiaos.platform.internal \
        -hidePackage com.eunoiaos.platform.internal \
        -hdf android.whichdoc offline \
        -hdf sdk.version $(eunoia_platform_docs_docs_SDK_VERSION) \
        -hdf sdk.rel.id $(eunoia_platform_docs_docs_SDK_REL_ID) \
        -hdf sdk.preview 0 \
        -since $(EUNOIA_SRC_API_DIR)/1.txt 1 \
        -since $(EUNOIA_SRC_API_DIR)/2.txt 2 \
        -since $(EUNOIA_SRC_API_DIR)/3.txt 3 \
        -since $(EUNOIA_SRC_API_DIR)/4.txt 4 \
        -since $(EUNOIA_SRC_API_DIR)/5.txt 5 \
        -since $(EUNOIA_SRC_API_DIR)/6.txt 6 \
        -since $(EUNOIA_SRC_API_DIR)/7.txt 7 \
        -since $(EUNOIA_SRC_API_DIR)/8.txt 8 \
        -since $(EUNOIA_SRC_API_DIR)/9.txt 9

$(full_target): $(eunoia_framework_built) $(gen)
#include $(BUILD_DROIDDOC)

include $(call first-makefiles-under,$(LOCAL_PATH))

# Cleanup temp vars
# ===========================================================
eunoia_platform_docs_src_files :=
eunoia_platform_docs_java_libraries :=
eunoia_platform_docs_LOCAL_ADDITIONAL_JAVA_DIR :=
