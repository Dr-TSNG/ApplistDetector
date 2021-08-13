cmake_minimum_required(VERSION 3.18.1)
project("applistdetector")

include_directories(external/linux_syscall_support)
add_library(native SHARED native.cpp)
target_link_libraries(native android log)