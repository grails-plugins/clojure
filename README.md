# Grails Clojure Plugin

The clojure plugin provides easy access to execute clojure code from any Grails artifact (controllers, taglibs, services etc...)

A brief screencast is available at http://s3.amazonaws.com/jeffscreencasts/grails_clojure_demo.mov which demonstrates the basic usage of the plugin. A special "thanks" goes to Stu Halloway who let me borrow some fibonacci code from his Programming Clojure book for use in the video.


Installation

To install the clojure plug-in just add the following under dependencies block in build.gradle:

    compile 'org.grails.plugin:grails-clojure:1.0-SNAPSHOT'

## Usage

The clojure plugin expects to find clojure source files under the `src/main/clj/` directory. The directory is created for you when the plugin is installed. Clojure source files in a Grails project should have the `.clj` extension.

For example, `src/main/clj/demo.clj` might look like this:

    (ns grails)
    (def twenty 20)
    
    (defn add_numbers [a b]
        (+ a b))

With that code in place, the `add_numbers` function may be executed as a method call on a dynamic property named `clj` from any Grails artifact. See below:

    class MyMathService {
        def addNumbers(x, y) {
            // invoke the function as a method on the
            // clj dynamic property…
            clj.add_numbers(x, y)
        }
    
       def addTwenty(x) {
            // access a binding (twenty) in clojure via the dynamic
            // property and pass it as an argument to a clojure function
            clj.add_numbers(x, clj.twenty)
    
            // or use it from groovy
            assert clj.twenty == 20
       }
    
    }


##The Dynamic Property

The dynamic property will be named `clj` by default. The name of the property may be set explicitly in grails-app/conf/application.yml by assigning a value to the `grails.clojure.dynamicPropertyName` property.

    grails: 
          clojure: 
                dynamicPropertyName: clojurePropertyName
                
For most applications, the default name of `clj` should be fine.


##Parameters And Return Values

Any object may be passed to a clojure function including primitives, objects, collections… anything. When you pass Java/Groovy objects into a clojure function and start manipulating it, you are giving up all guarantees that clojure can make in terms of concurrency and immutability. That is just part of the deal with clojure. Clojure can't keep you from mutating mutable Java objects because clojure allows you to call any method you like on a Java object.

More interesting than the ability to pass anything in to a clojure function, you can get anything back from a clojure function. For example, the following returns a persistent list...

    (defn getit []
        (list "jeff" "zack" "jake"))
        
If you call that function from Java or Groovy, you will get back a clojure.lang.PersistentList. This is really interesting. Clojure has a great set of data structures. Some Java developers who don't want to write Clojure code may use the clojure libraries just for the data structures.


##Clojure Namespaces

The plugin expects all clojure functions defined in your project to be in the `grails` namespace. You may define functions in other namespaces and the plugin supports a syntax for accessing those functions. The following clojure source code defines a function in the `math` namespace:

    (ns math)
    (defn add_numbers [a b]
        (+ a b))

Since that function is not in the `grails` namespace, a special syntax must be used to access it. The syntax involves specifying the namespace like this:

    class MyMathService {
        def addNumbers(x, y) {
            // invoke the function as a method on the
            // clj dynamic property and specify the namespace…
            clj['math'].add_numbers(x, y)
        }
    
    }


##Feedback

Your feedback is welcome on the [Grails Dev mailing list](http://grails.org/Mailing%20lists). Please share what you would like to see provided by the plugin.


##References

Stuart Halloway's [Programming Clojure](http://www.pragprog.com/titles/shcloj/programming-clojure) from [The Pragmatic Bookshelf](http://www.pragprog.com/) is a fantastic reference on the language.

A number of useful videos on clojure are available at http://clojure.blip.tv/.


##Release Notes
**Version 1.0.SNAPSHOT**
* Upgrade to Grails-3.0.1

**Version 0.6**
* Upgrade to Clojure 1.2.0

**Version 0.5**
* Require Grails 1.2 or later
* Upgrade to Clojure 1.1.0
* Prohibit invoking clojure macros from Groovy
