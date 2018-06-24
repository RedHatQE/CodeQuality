#include <cppunit/TestCase.h>
#include <cppunit/ui/text/TextTestRunner.h>
#include <cppunit/extensions/HelperMacros.h>
#include <cppunit/TestResult.h>
#include <cppunit/TestResultCollector.h>
#include <cppunit/BriefTestProgressListener.h>
#include <cppunit/CompilerOutputter.h>
#include <cppunit/XmlOutputter.h>

#include "main.h"

using namespace CppUnit;

class HelloWorldTest : public CppUnit::TestFixture  {
public:

    void test()
    {
        CPPUNIT_ASSERT( testMe("Bob").compare("Hello Bob!") == 0 );
    }
};

int main(int argc, char* argv[]) {
    CppUnit::TestCaller<HelloWorldTest> test( "test", &HelloWorldTest::test );
    CppUnit::TestResult result;
    test.run( &result );
}