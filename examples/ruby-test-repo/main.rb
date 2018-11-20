# Run simplecov code coverage for sonar integration
if ENV['COVERAGE'] == 'on'

    # Include requirements
    require 'simplecov'
    require 'simplecov-rcov'
    require 'simplecov-console'

    # Set formatter as rcov to support third-party plugin
    SimpleCov.formatter = SimpleCov::Formatter::MultiFormatter[
      SimpleCov::Formatter::RcovFormatter,
      SimpleCov::Formatter::Console,
    ]
    SimpleCov.start
end

require_relative 'a'
require 'test/unit'

class TestHello < Test::Unit::TestCase
    def test_function
        assert_equal("Hello world, it's Bob", hello('Bob'))
    end
end
