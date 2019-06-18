require "date"
require "base64"
require "digest/sha1"
require "java"

# If the Kinetic Task version is under 4, load the openssl and json libraries
# because they are not included in the ruby version
if KineticTask::VERSION.split(".").first.to_i < 4
  # Load the JRuby Open SSL library unless it has already been loaded.  This
  # prevents multiple handlers using the same library from causing problems.
  if not defined?(Jopenssl)
    # Load the Bouncy Castle library unless it has already been loaded.  This
    # prevents multiple handlers using the same library from causing problems.
    # Calculate the location of this file
    handler_path = File.expand_path(File.dirname(__FILE__))
    # Calculate the location of our library and add it to the Ruby load path
    library_path = File.join(handler_path, "vendor/bouncy-castle-java-1.5.0147/lib")
    $:.unshift library_path
    # Require the library
    require "bouncy-castle-java"


    # Calculate the location of this file
    handler_path = File.expand_path(File.dirname(__FILE__))
    # Calculate the location of our library and add it to the Ruby load path
    library_path = File.join(handler_path, "vendor/jruby-openssl-0.8.8/lib/shared")
    $:.unshift library_path
    # Require the library
    require "openssl"
    # Require the version constant
    require "jopenssl/version"
  end

  # Validate the the loaded openssl library is the library that is expected for
  # this handler to execute properly.
  if not defined?(Jopenssl::Version::VERSION)
    raise "The Jopenssl class does not define the expected VERSION constant."
  elsif Jopenssl::Version::VERSION != "0.8.8"
    raise "Incompatible library version #{Jopenssl::Version::VERSION} for Jopenssl.  Expecting version 0.8.8"
  end

  # Load the ruby json library unless
  # it has already been loaded.  This prevents multiple handlers using the same
  # library from causing problems.
  if not defined?(JSON)
    # Calculate the location of this file
    handler_path = File.expand_path(File.dirname(__FILE__))
    # Calculate the location of our library and add it to the Ruby load path
    library_path = File.join(handler_path, "vendor/json-1.8.0/lib")
    $:.unshift library_path
    # Require the library
    require "json"
  end

  # Validate the the loaded JSON library is the library that is expected for
  # this handler to execute properly.
  if not defined?(JSON::VERSION)
    raise "The JSON class does not define the expected VERSION constant."
  elsif JSON::VERSION.to_s != "1.8.0"
    raise "Incompatible library version #{JSON::VERSION} for JSON.  Expecting version 1.8.0."
  end
end


# Load the ruby Mime Types library unless it has already been loaded.  This prevents
# multiple handlers using the same library from causing problems.
if not defined?(MIME)
  # Calculate the location of this file
  handler_path = File.expand_path(File.dirname(__FILE__))
  # Calculate the location of our library and add it to the Ruby load path
  library_path = File.join(handler_path, 'vendor/mime-types-1.19/lib/')
  $:.unshift library_path
  # Require the library
  require 'mime/types'
end

# Validate the the loaded Mime Types library is the library that is expected for
# this handler to execute properly.
if not defined?(MIME::Types::VERSION)
  raise "The Mime class does not define the expected VERSION constant."
elsif MIME::Types::VERSION != '1.19'
  raise "Incompatible library version #{MIME::Types::VERSION} for Mime Types.  Expecting version 1.19."
end


# Load the ruby rest-client library (used by the Octokit library) unless
# it has already been loaded.  This prevents multiple handlers using the same
# library from causing problems.
if not defined?(RestClient)
  # Calculate the location of this file
  handler_path = File.expand_path(File.dirname(__FILE__))
  # Calculate the location of our library and add it to the Ruby load path
  library_path = File.join(handler_path, 'vendor/rest-client-1.6.7/lib')
  $:.unshift library_path
  # Require the library
  require 'rest-client'
end

# Validate the the loaded rest-client library is the library that is expected for
# this handler to execute properly.
if not defined?(RestClient.version)
  raise "The RestClient class does not define the expected VERSION constant."
elsif RestClient.version.to_s != '1.6.7'
  raise "Incompatible library version #{RestClient.version} for rest-client.  Expecting version 1.6.7."
end


begin
  org.apache.http.Header
# If JRuby was unable to load the Header class.
rescue NameError
  # Require the java package.
  handler_path = File.expand_path(File.dirname(__FILE__))
  require File.join(handler_path, 'vendor', 'httpcore-4.1.2.jar')
end

begin
  org.apache.commons.logging.LogFactory
# If JRuby was unable to load the LogFactory class.
rescue NameError
  # Require the java package.
  handler_path = File.expand_path(File.dirname(__FILE__))
  require File.join(handler_path, 'vendor', 'commons-logging-1.1.1.jar')
end

# Attempt to load a class from the http-client package.
begin
  org.apache.http.client.HttpClient
# If JRuby was unable to load the HttpClient class.
rescue NameError
  # Require the java package.
  handler_path = File.expand_path(File.dirname(__FILE__))
  require File.join(handler_path, 'vendor', 'httpclient-4.1.2.jar')
end

begin
  org.apache.http.entity.mime.MultipartEntity
rescue NameError
  handler_path = File.expand_path(File.dirname(__FILE__))
  require File.join(handler_path, 'vendor', 'httpmime-4.1.2.jar')
end

module JavaIO
  java_import java.io.File
end

java_import org.apache.http.client.HttpClient
java_import org.apache.http.impl.client.DefaultHttpClient
java_import org.apache.http.client.methods.HttpPost
java_import org.apache.http.util.EntityUtils
java_import org.apache.http.entity.mime.MultipartEntity
java_import org.apache.http.entity.mime.content.ByteArrayBody
