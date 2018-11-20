GCC_CMD=g++
GCC_ARGS=-g --coverage -lcppunit
PWD=$(shell pwd)
SRC_FILES=main.cpp tests.cpp main.h
EXEC_NAME=testcpp
TEMP_LIST=*.gcda *.gcno *.gcov testcpp

all: testcpp

testcpp: ${SRC_FILES}
	${GCC_CMD} $(filter %.cpp,$^) ${GCC_ARGS} -o ${EXEC_NAME}
	${PWD}/${EXEC_NAME}

.PHONY: clean gcov

clean:
	rm -rf ${TEMP_LIST}

gcov:
	gcov -o ${PWD} -f ${PWD}/$(firstword ${SRC_FILES})
	gcovr -r ${PWD} --xml-pretty --object-directory=${PWD} > report.xml
