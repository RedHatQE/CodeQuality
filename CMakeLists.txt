cmake_minimum_required(VERSION 3.7)
project(testCPP)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_FLAGS -lcppunit)

set(SOURCE_FILES tests.cpp main.cpp main.h)
add_executable(testCPP ${SOURCE_FILES})