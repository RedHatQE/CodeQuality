# Perform coverage conditionally
if( ENV['RUBY_COVERAGE_CONFIG'] )

  # Importing relavant libraries
  require 'yaml'
  require 'simplecov'
  require 'simplecov-rcov'

  # Load configuration file
  config = YAML.load_file( ENV['RUBY_COVERAGE_CONFIG'] )

  # Configuring and initializing analysis
  SimpleCov.start do

    # Setting workspace configuration
    root Dir.pwd
    coverage_dir config['report_directory']

    # Setting analysis formatting
    command_name ENV['RUBY_COVERAGE_NAME'] || config['analysis_name']
    formatters = SimpleCov::Formatter::RcovFormatter

    # Adding project filter
    # Without this we get every library called
    filters.clear
    add_filter do |src|
            !(src.filename =~ /^#{SimpleCov.root}/) unless src.filename =~ Regexp.new(config['project_folder_name'])
    end
  end
end

