# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.


set( CMAKE_CXX_STANDARD 14 )

add_definitions(-DTARGET_ANDROID)
add_definitions(-DEIGEN_DONT_ALIGN_STATICALLY)
add_definitions(-DEIGEN_HAS_CXX11_MATH=0)




set(BOOST_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../boost_1_70_0)

set(BOOST_GIL_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/boost-gil-extension)

set(ZIP_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/julienr-libzip-android/jni)

set(LIBPNG_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/libpng_1.4.1_android)

set(LIBJPEG_PATH "${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/jpeg-9")

set(EIGEN_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../eigen)

set(SOL2_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../sol2)

set(LUA_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs/lua-5.3.4/src)



include_directories(${CMAKE_CURRENT_SOURCE_DIR}/../../../tes-engine)

include_directories(${BOOST_PATH})
include_directories(${LIBPNG_PATH})
include_directories(${LIBJPEG_PATH})
include_directories(${ZIP_PATH})

include_directories(${BOOST_GIL_PATH})
include_directories(${EIGEN_PATH})
include_directories(${SOL2_PATH})
include_directories(${LUA_PATH})

add_library( engine
             SHARED
             IMPORTED )
			 
set_target_properties( # Specifies the target library.
                       engine

                       # Specifies the parameter you want to define.
                       PROPERTIES IMPORTED_LOCATION

                       # Provides the path to the library you want to import.
        ${CMAKE_CURRENT_SOURCE_DIR}/../../../tes-engine/SalmonEngineAndroid/app/build/intermediates/cmake/${CMAKE_BUILD_TYPE}/obj/${ANDROID_ABI}/libengine.so
        )
					   



add_library( # Sets the name of the library.
             template

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             ${CMAKE_CURRENT_SOURCE_DIR}/../../game/android_api.cpp
             ${CMAKE_CURRENT_SOURCE_DIR}/../../game/main_code.cpp
             ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsDrawer/GlDrawer.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsDrawer/GlProcess.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsDrawer/RulerClasses.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsHelper/stub/PointListStub.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsHelper/stub/UserSettingsStub.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsHelper/stub/FunctionsStub.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsDrawer/VisualControls/VAngleLine.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/../../game/legacy/ThreadsDrawer/VisualControls/VControl.cpp
             )
			 
			 

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
			  
find_library( # Sets the name of the path variable.
              GLESv2-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              GLESv2 )


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       template

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} ${GLESv2-lib} engine )
