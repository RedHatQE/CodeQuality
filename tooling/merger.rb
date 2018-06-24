require 'json'
require 'simplecov'

# initialize data members 
# and configure simplecov
coverage_results = []
SimpleCov.filters.clear

ARGV.each do |arg|

  # load json results from coverage folder
  file = File.join(arg, ".resultset.json")
  file_results = JSON.parse(File.read(file))

  # parse results from coverage file to array
  file_results.each do |command, data|
    result = SimpleCov::Result.from_hash(command => data)
    coverage_results << result
  end
end

# merge results from array to results object
merged_results = SimpleCov::ResultMerger.merge_results(*coverage_results)

# save results to file
File.open("./results.json","w") do |f|
  f.write(JSON.pretty_generate(merged_results.to_hash()))
end
